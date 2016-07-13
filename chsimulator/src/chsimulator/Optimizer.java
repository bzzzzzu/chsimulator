package chsimulator;

public class Optimizer {
	Transresults bestresult = new Transresults();
	ASbuild bestbuild = new ASbuild();
	
	Transresults midresult = new Transresults();
	ASbuild midbuild = new ASbuild();
	
	double AS = 0;
	
	double Xylsugg = 20;
	double Chorsugg = 20;
	double Phansugg = 20;
	double Borbsugg = 20;
	double Ponysugg = 20;
	
	public Optimizer(int AS) {
		this.AS = AS;
	}
	
	public void suggest(Suggestion sug) {
		this.Xylsugg = sug.Xylsugg;
		this.Chorsugg = sug.Chorsugg;
		this.Phansugg = sug.Phansugg;
		this.Borbsugg = sug.Borbsugg;
		this.Ponysugg = sug.Ponysugg;
		fixnegatives();
	}
	
	public void suggest(double Xylsugg, double Chorsugg, double Phansugg, double Borbsugg, double Ponysugg) {
		this.Xylsugg = Xylsugg;
		this.Chorsugg = Chorsugg;
		this.Phansugg = Phansugg;
		this.Borbsugg = Borbsugg;
		this.Ponysugg = Ponysugg;
		fixnegatives();
	}
	
	public double simulateAndCompare(ASbuild build, boolean needToSolvePhanChor) {
		Transcension trans = new Transcension(AS, build, needToSolvePhanChor);
		Transresults result = trans.simulateTrans();
		if (result.ASgaindayhuman > bestresult.ASgaindayhuman) {
			bestresult.copyResult(result);
			bestbuild.copyBuild(build);
		}
		return result.ASgaindayhuman;
	}
	
	public Suggestion optimize() {
		// % of total AS by which to move ousiders, decreases gradually
		double certainity = 10;
		
		// Need more accuracy at high AS
		while (certainity > (15 / AS)) {
			// Baseline for future evaluations
			ASbuild build = new ASbuild();
			build = allocate(build);
			simulateAndCompare(build, true);
			
			// Increasing outsiders value and checking effects on AS/day
			// Could be used to adjust build after iteration on several outsiders at once
			// Althrough, that method was worse than double-optimizing with doubles>ints
			double Xylmove = outsiderMove(1, build, AS * certainity / 500) - bestresult.ASgainday;
			double Chormove = outsiderMove(2, build, AS * certainity / 500) - bestresult.ASgainday;
			double Phanmove = outsiderMove(3, build, AS * certainity / 500) - bestresult.ASgainday;
			double Borbmove = outsiderMove(4, build, AS * certainity / 500) - bestresult.ASgainday;
			double Ponymove = outsiderMove(5, build, AS * certainity / 500) - bestresult.ASgainday;
		
			// Checking effect of moving outsiders and checking total improvements/losses
			// Not really used anymore
			double movepos = 0;
			double moveneg = 0;
			if (Xylmove > 0) { movepos += Xylmove; } else { moveneg += Xylmove; }
			if (Chormove > 0) { movepos += Chormove; } else { moveneg += Chormove; }
			if (Phanmove > 0) { movepos += Phanmove; } else { moveneg += Phanmove; }
			if (Borbmove > 0) { movepos += Borbmove; } else { moveneg += Borbmove; }
			if (Ponymove > 0) { movepos += Ponymove; } else { moveneg += Ponymove; }
		
			if (Xylmove > 0) { Xylsugg = Xylsugg + certainity / movepos * Xylmove; }
			else { Xylsugg = Xylsugg - certainity / moveneg * Xylmove; }
			if (Chormove > 0) { Chorsugg = Chorsugg + certainity / movepos * Chormove; }
			else { Chorsugg = Chorsugg - certainity / moveneg * Chormove; }
			if (Phanmove > 0) { Phansugg = Phansugg + certainity / movepos * Phanmove; }
			else { Phansugg = Phansugg - certainity / moveneg * Phanmove; }
			if (Borbmove > 0) { Borbsugg = Borbsugg + certainity / movepos * Borbmove; }
			else { Borbsugg = Borbsugg - certainity / moveneg * Borbmove; }
			if (Ponymove > 0) { Ponysugg = Ponysugg + certainity / movepos * Ponymove; }
			else { Ponysugg = Ponysugg - certainity / moveneg * Ponymove; }
			
			// Copying best result from moving outsiders to working results
			if (midresult.ASgaindayhuman > bestresult.ASgaindayhuman) {
				bestresult.copyResult(midresult);
				bestbuild.copyBuild(midbuild);
			}
			
			// If we dont have any improvement, decrease step more aggressively
			if (movepos == 0) {
				movepos = ((Xylmove + Chormove + Phanmove + Borbmove + Ponymove) / -5) / bestresult.ASgaindayhuman;
				//System.out.println("movepos: " + movepos);
				if (movepos > 1) { movepos = 1; }
				if (movepos < 0) { movepos = 0; }
				certainity = certainity * (1 - movepos);
			}
		
			// Fix anything bad that could happen to build to avoid broken AS count
			fixnegatives();
			
			// Translate best build after iteration into suggestions for the next one
			tobestbuild();
			
			// Decrease step size and continue
			certainity = certainity * 0.85;	
		}
		
		//bestresult.print();
		bestresult.write(1);
		
		// Prepare to step 2 - integer optimization
		// Round double-value outsiders
		double loss;
		loss = bestbuild.Xyl - Math.round(bestbuild.Xyl); bestbuild.Xyl = Math.round(bestbuild.Xyl);
		bestbuild.Chor = bestbuild.Chor + loss;	loss = 0;
		loss = bestbuild.Chor - Math.round(bestbuild.Chor); bestbuild.Chor = Math.round(bestbuild.Chor);
		bestbuild.Phan = bestbuild.Phan + loss; loss = 0;
		loss = bestbuild.Phan - Math.round(bestbuild.Phan); bestbuild.Phan = Math.round(bestbuild.Phan);
		bestbuild.Borb = bestbuild.Borb + loss; loss = 0;
		loss = bestbuild.Borb - Math.round(bestbuild.Borb); bestbuild.Borb = Math.round(bestbuild.Borb);
		bestbuild.Pony = bestbuild.Pony + loss; loss = 0;
		
		//System.out.println("Xyl: " + bestbuild.Xyl + ", Chor: " + bestbuild.Chor + ", Phan: " + bestbuild.Phan + ", Borb: " + bestbuild.Borb + ", Pony: " + bestbuild.Pony);
		
		// Translating them to actual levels
		bestbuild.Chor = Transcension.solveChor(bestbuild.Chor);
		bestbuild.Phan = Transcension.solvePhan(bestbuild.Phan);
		
		//System.out.println("After solving - Xyl: " + bestbuild.Xyl + ", Chor: " + bestbuild.Chor + ", Phan: " + bestbuild.Phan + ", Borb: " + bestbuild.Borb + ", Pony: " + bestbuild.Pony);
		
		// We probable have some leftover again, dump into Pony
		loss = (bestbuild.Chor - Math.round(bestbuild.Chor)) * Math.ceil(bestbuild.Chor / 10);
		loss = loss + (bestbuild.Phan - Math.round(bestbuild.Phan)) * Math.ceil(bestbuild.Phan);
		bestbuild.Chor = Math.round(bestbuild.Chor);
		bestbuild.Phan = Math.round(bestbuild.Phan);
		bestbuild.Pony = Math.round(bestbuild.Pony + loss);
		
		//System.out.println("After dumping - Xyl: " + bestbuild.Xyl + ", Chor: " + bestbuild.Chor + ", Phan: " + bestbuild.Phan + ", Borb: " + bestbuild.Borb + ", Pony: " + bestbuild.Pony);
		
		// Starting fresh round of optimization
		bestresult.ASgaindayhuman = 0;
		optimizeInt(bestbuild);
		
		// If we use previous AS optimizer results as a base to next AS optimization - return them
		// It is pretty risky, though, local maximums are everywhere and function is very uneven
		tobestbuild();
		return new Suggestion(Xylsugg, Chorsugg, Phansugg, Borbsugg, Ponysugg);
	}
	
	public void optimizeInt(ASbuild build) {
		// Integer optimization, limited Phan
		double progress = 1;
		while (progress > 0) {
			// Use provided build as a baseline
			simulateAndCompare(build, false);
			
			progress = bestresult.ASgaindayhuman;
			
			// Phan is excluded
			outsiderAdjustInt(1, 2, build);
			outsiderAdjustInt(1, 4, build);
			outsiderAdjustInt(1, 5, build);
			outsiderAdjustInt(2, 4, build);
			outsiderAdjustInt(2, 5, build);
			outsiderAdjustInt(4, 5, build);
			// Phan/Pony
			outsiderAdjustInt(3, 5, build);
			// TODO: Phan/rest of the outsiders, active build uses all of them
			// and it cant optimize Phan correctly based only on Phan/Pony or
			// Phan/any other single outsider
			
			build.copyBuild(bestbuild);
			progress = bestresult.ASgaindayhuman - progress;
			System.out.println("Progress: " + progress);
		}
		
		bestresult.print();
		bestresult.write(2);
	}

	private void tobestbuild() {
		// Translate current best build to a Suggestion
		Xylsugg = (Xylsugg * 0 + bestbuild.Xyl / AS * 100 * 2) / 2;
		Chorsugg = (Chorsugg * 0 + bestbuild.Chor / AS * 100 * 2) / 2;
		Phansugg = (Phansugg * 0 + bestbuild.Phan / AS * 100 * 2) / 2;
		Borbsugg = (Borbsugg * 0 + bestbuild.Borb / AS * 100 * 2) / 2;
		Ponysugg = (Ponysugg * 0 + bestbuild.Pony / AS * 100 * 2) / 2;
	}

	private void fixnegatives() {
		// In case of bad suggestion data, do that
		// It just works, i dont remember why
		double total1 = Xylsugg + Chorsugg + Phansugg + Borbsugg + Ponysugg;
		if (total1 < 100) {
			Xylsugg *= 100 / total1;
			Chorsugg *= 100 / total1;
			Phansugg *= 100 / total1;
			Borbsugg *= 100 / total1;
			Ponysugg *= 100 / total1;
		}
		
		double total = 0;
		if (Xylsugg > 0) { total += Xylsugg; }
		if (Chorsugg > 0) { total += Chorsugg; }
		if (Phansugg > 0) { total += Phansugg; }
		if (Borbsugg > 0) { total += Borbsugg; }
		if (Ponysugg > 0) { total += Ponysugg; }
		
		if (total > 100) {
			if (Xylsugg > 0) { Xylsugg *= 100 / total; } else { Xylsugg = 0; }
			if (Chorsugg > 0) { Chorsugg *= 100 / total; } else { Chorsugg = 0; }
			if (Phansugg > 0) { Phansugg *= 100 / total; } else { Phansugg = 0; }
			if (Borbsugg > 0) { Borbsugg *= 100 / total; } else { Borbsugg = 0; }
			if (Ponysugg > 0) { Ponysugg *= 100 / total; } else { Ponysugg = 0; }
		}
	}

	public double outsiderMove(int outsider, ASbuild test, double move) {
		// Doubles optimization
		ASbuild build1 = new ASbuild();
		build1.copyBuild(test);
		build1.raiseOutsider(outsider, move);
		Transcension trans = new Transcension(AS, build1, true);
		Transresults result = trans.simulateTrans();
		if (result.ASgainday > bestresult.ASgainday) {
			midresult.copyResult(result);
			midbuild.copyBuild(build1);
		}
		return result.ASgainday;
	}
	
	public void outsiderAdjustInt(int out1, int out2, ASbuild test) {
		double move = 1;
		ASbuild build1 = new ASbuild();
		
		// Move to the one side
		build1.copyBuild(test);
		build1.moveOutsiderInt(out1, out2, move);
		simulateAndCompare(build1, false);

		// And the other side
		build1.copyBuild(test);
		build1.moveOutsiderInt(out2, out1, move);
		simulateAndCompare(build1, false);
		
		// If not Phan, try to avoid local maximum by increasing the range of moves
		if (out1 != 3) {
			move = 2;
			build1.copyBuild(test);
			build1.moveOutsiderInt(out1, out2, move);
			simulateAndCompare(build1, false);
			
			build1.copyBuild(test);
			build1.moveOutsiderInt(out2, out1, move);
			simulateAndCompare(build1, false);
			
			move = 3;
			build1.copyBuild(test);
			build1.moveOutsiderInt(out1, out2, move);
			simulateAndCompare(build1, false);
			
			build1.copyBuild(test);
			build1.moveOutsiderInt(out2, out1, move);
			simulateAndCompare(build1, false);
		}
		
		// Chor/Phan are the especially prone to that, need even more movements
		if ((out1 == 2) && (out2 == 5)) {
			for (int i = 4; i < 7; i++) {
				build1.copyBuild(test);
				build1.moveOutsiderInt(out1, out2, i);
				simulateAndCompare(build1, false);
				
				build1.copyBuild(test);
				build1.moveOutsiderInt(out2, out1, move);
				simulateAndCompare(build1, false);
			}
		}
	}
	
	public ASbuild allocate(ASbuild build) {
		build.Xyl = AS * Xylsugg / 100;
		build.Chor = AS * Chorsugg / 100;
		build.Phan = AS * Phansugg / 100;
		build.Borb = AS * Borbsugg / 100;
		build.Pony = AS * Ponysugg / 100;		
		return build;
	}

}
