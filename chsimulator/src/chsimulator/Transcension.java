package chsimulator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Transcension {
	
	int AS = 0;
	double TP = 1.01;
	
	BigDecimal HS = BigDecimal.valueOf(0);
	BigDecimal HSlimit = BigDecimal.valueOf(0);
	
	double Xyl = 0;
	double Chor = 0;
	double Phan = 0;
	double Borb = 0;
	double Pony = 0;
	
	public Transcension(int asouls) {
		// Calculating trandescence data from given AS
		AS = asouls;
		TP = 1 + 0.01 * (50 - 49 * Math.pow(Math.E, ((double) asouls / 10000 * -1)));
		
		// Calculating HS amounts from given AS
		HS = BigDecimal.valueOf(Math.pow(10., 0.2));
		HS = HS.pow(asouls, new MathContext(10, RoundingMode.HALF_DOWN));
		HSlimit = HS.divide(BigDecimal.valueOf(20));
	}
	
	public Transcension(double asouls, ASbuild build, boolean solve) {
		// Calculating trandescence data from given AS
		AS = (int) Math.round(asouls);
		TP = 1 + 0.01 * (50 - 49 * Math.pow(Math.E, ((double) asouls / 10000 * -1)));
		
		// Calculating HS amounts from given AS
		HS = BigDecimal.valueOf(Math.pow(10., 0.2));
		HS = HS.pow(AS, new MathContext(10, RoundingMode.HALF_DOWN));
		HSlimit = HS.divide(BigDecimal.valueOf(20));
		
		// Handling of spent AS optimizing or actual builds with actual levels
		this.Xyl = build.Xyl;
		if (solve == true) {
			this.Chor = solveChor(build.Chor);
			this.Phan = solvePhan(build.Phan);
		}
		else {
			this.Chor = build.Chor;
			this.Phan = build.Phan;
		}
		this.Borb = build.Borb;
		this.Pony = build.Pony;
	}
	
	public static double solvePhan(double phan) {
		double endphan = 0;
		if (phan < 0) { phan = 0; }
		
		int i = 1;
		while (phan > 0) {
			if (phan > i) {
				endphan = endphan + 1;
				phan = phan - i;
			}
			else {
				endphan = endphan + phan / i;
				phan = 0;
			}
			i++;
		}
		
		return endphan;
	}
	
	public static double solveChor(double chor) {
		double endchor = 0;
		if (chor < 0) { chor = 0; }
		
		int i = 1;
		while (chor > 0) {
			if (chor > (i * 10)) {
				endchor = endchor + 10;
				chor = chor - i * 10;
			}
			else {
				endchor = endchor + chor / i;
				chor = 0;
			}
			i++;
		}
		
		return endchor;
	}
	
	public Transresults simulateTrans() {
		Tables.precalcTransPower(TP, Phan);
		
		// Base starting time for simulation from actual practice (3 hours)
		long totaltime = (long) ((3600000 * 15) / (Xyl + 2));
		
		// Starting parameters for first ascension in simulation, assuming 2 ascensions before that
		BigDecimal currentHS = BigDecimal.valueOf(1000);
		int gilds = 10;
		int numberOfAscensions = 2;
		
		// Current, best and human-recommended transcension statistic
		TransStats currentstat = new TransStats(AS, 0, 0, totaltime, AS, 0, 0, 0);
		TransStats beststat = new TransStats(AS, 0, 0, totaltime, AS, 0, 0, 0);
		TransStats adjustedstat = new TransStats(AS, 0, 0, totaltime, AS, 0, 0, 0);
		
		// Stop simulation when AS/hour does not improve over 10 ascensions 
		int failedASimprovements = 0;
		
		// Experimental setting to favor longer transcensions for humans
		double timevalue = 1.05;
		
		while ((failedASimprovements < 5) && (numberOfAscensions < 10000)) {
			// Initializing ascension
			AscSettings set = new AscSettings(currentHS, AS, TP, HSlimit, Xyl, Chor, Phan, Borb, Pony, gilds, 0, currentstat.zone);
			// If you dont want to use idle/active builds, change to 0 or comment
			if (currentHS.compareTo(BigDecimal.valueOf(1e7)) > 0) {
				set.strat = 1;
			}
			if (currentHS.compareTo(BigDecimal.valueOf(1e14)) > 0) {
				//set.strat = 2;
			}
			Ascension asc = new Ascension(set);
			
			// Ascending
			asc.allocateHS();
			Ascresults res = asc.simulateAsc();
			numberOfAscensions++;
			
			// Processing results
			currentHS = currentHS.add(res.HS);
			totaltime += res.time;
			currentstat.time = totaltime;
			currentstat.ascensions = numberOfAscensions;
			currentstat.zone = res.zone;
			currentstat.HS = currentHS.doubleValue();
			gilds = res.gilds;

			// Calculating AS/day statistic
			currentstat.AS = Math.log10((currentHS.doubleValue() + HS.doubleValue())) * 5;
			currentstat.ASday = (currentstat.AS - AS) / totaltime * 3600 * 1000 * 24;
			currentstat.ASdayadjusted = currentstat.ASday * timevalue;
			
			// Saving stats and checking if we are losing efficiency
			if (currentstat.ASdayadjusted > beststat.ASday) {
				adjustedstat.copyStat(currentstat);
				failedASimprovements = 0;
			}
			else {
				if (beststat.ASday > 0.001) {
					failedASimprovements++;
				}
			}
			
			if (currentstat.ASday > beststat.ASday) {
				beststat.copyStat(currentstat);
			}
			
			//System.out.println("Trans now - AS: " + currentstat.ASdiffformat() + ", time: " + (currentstat.time / 1000 / 3600) + "h, AS/day: " + currentstat.ASdayformat());
		}
		
		Transresults res = new Transresults();
		
		res.time = beststat.time;
		res.ASgain = beststat.AS - AS;
		res.ascensions = beststat.ascensions;
		res.ASgainday = beststat.ASday;
		res.zone = beststat.zone;
		res.HS = beststat.HS;
		
		res.timehuman = adjustedstat.time;
		res.ASgainhuman = adjustedstat.AS - AS;
		res.ascensionshuman = adjustedstat.ascensions;
		res.ASgaindayhuman = adjustedstat.ASday;
		res.zonehuman = adjustedstat.zone;
		res.HShuman = adjustedstat.HS;
		
		res.AS = AS;
		res.Xyl = Xyl;
		res.Chor = Chor;
		res.Phan = Phan;
		res.Borb = Borb;
		res.Pony = Pony;
		
		res.print();
		
		return(res);
	}
	
}
