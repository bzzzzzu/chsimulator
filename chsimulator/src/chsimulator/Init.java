package chsimulator;

public class Init {

	public static void main(String[] args) {
		// Precalculating static data
		System.out.println("Calculating tables...");
		Tables.precalcHeroCost();
		Tables.precalcHeroDamage();
		Tables.precalcZoneHP();
		Tables.precalcExpAncients();
		System.out.println("Tables populated.");
		
		// Main optimization loop
		// Hybrid enabled by default, change in Transcension
		Suggestion sugg = new Suggestion();
		for (int i = 80; i < 201; i++) {
			if (i == 30) { sugg = new Suggestion(25, 0, 0, 0, 75); }
			if (i == 40) { sugg = new Suggestion(20, 10, 10, 0, 60); }
			if (i == 60) { sugg = new Suggestion(12, 13, 30, 0, 45); }
			if (i == 80) { sugg = new Suggestion(5, 10, 55, 0, 30); }
			if (i == 100) { sugg = new Suggestion(5, 10, 55, 5, 25); }
			if (i == 120) { sugg = new Suggestion(5, 10, 55, 10, 20); }
			if (i == 160) { sugg = new Suggestion(5, 10, 50, 15, 20); }
			if (i == 200) { sugg = new Suggestion(5, 10, 50, 20, 15); }
			Optimizer opt = new Optimizer(i);
			opt.suggest(sugg);
			opt.optimize();
		}
		
		// Single AS optimizing, starts from suggested build
		//Optimizer opt = new Optimizer(133); opt.optimizeInt(new ASbuild(8, 9, 13, 7, 18));
		
		// Another version, starts from rough suggestion, prone to being stuck in local maximums
		Optimizer opt = new Optimizer(255);
		opt.suggest(new Suggestion(10,10,30,35,15));
		opt.optimize();
		
		// Simulating a Transcension with required parameters to check how a single build performs
		/*Transcension trans = new Transcension(77);
		trans.Xyl = 5;
		trans.Chor = 10;
		trans.Phan = 8;
		trans.Borb = 0;
		trans.Pony = 26;
		
		trans.simulateTrans();*/
	}

}
