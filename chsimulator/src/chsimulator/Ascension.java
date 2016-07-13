package chsimulator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Ascension {
	
	BigDecimal HS = BigDecimal.valueOf(0);
	BigDecimal HScurr = BigDecimal.valueOf(0);
	BigDecimal HSlimit = BigDecimal.valueOf(0);
	
	BigDecimal Siya = BigDecimal.valueOf(0);
	BigDecimal Lib = BigDecimal.valueOf(0);
	BigDecimal Arg = BigDecimal.valueOf(0);
	BigDecimal Mimzee = BigDecimal.valueOf(0);
	BigDecimal Mammon = BigDecimal.valueOf(0);
	BigDecimal Morg = BigDecimal.valueOf(0);
	
	BigDecimal Solomon = BigDecimal.valueOf(0);
	long Atman = 0;
	long Kuma = 0;
	
	long Fortuna = 0;
	long Dora = 0;
	long Dogcog = 0;
	long Chronos = 0;
	long Bubos = 0;
	long Vaagur = 0;
	
	long skills = 0;
	
	BigDecimal Frags = BigDecimal.valueOf(0);
	BigDecimal Bhaal = BigDecimal.valueOf(0);
	BigDecimal Jugg = BigDecimal.valueOf(0);
	
	long gilds = 10;
	int zone = 200;
	
	double Xyl = 0;
	double Chor = 0;
	double Phan = 0;
	double Borb = 0;
	double Pony = 0;
	int AS = 0;
	
	double TP = 1.01;
	
	int hero = 0;
	int level = 0;
	
	// Idle - 0, Hybrid - 1, Active - 2
	int strat = 0;
	int currentstrat = 0;
	// Single - 0, 25 levels - 1
	int levelstrat = 0;
	
	double luckytime = 0;
	double luckychance = 0;
	double combo = 0;
	long endoftime = Long.MAX_VALUE;
	
	long hze = 0;
	boolean debug = false;
	
	ancBonus abonus;
	
	public Ascension(AscSettings set) {
		this.HS = set.currentHS;
		this.AS = set.AS;
		this.TP = set.TP;
		this.HSlimit = set.HSlimit;
		this.Xyl = set.Xyl;
		this.Chor = set.Chor;
		this.Phan = set.Phan;
		this.Borb = set.Borb;
		this.Pony = set.Pony;
		this.gilds = set.gilds;
		this.strat = set.strat;
		this.hze = set.hze;
	}
	
	public void allocateHS() {
		HS = HS.round(new MathContext(10, RoundingMode.HALF_DOWN));
		
		// Chor influence on actual souls affordable
		double choreffect = Math.pow((1.0 / 0.95), (double) Chor);
		BigDecimal availsouls = HS.multiply(BigDecimal.valueOf(choreffect));
		availsouls = availsouls.round(new MathContext(10, RoundingMode.HALF_DOWN));
		
		// Splitting available souls to various groups of ancients
		BigDecimal idlesouls = availsouls.multiply(BigDecimal.valueOf(0.45));
		BigDecimal solsouls = availsouls.multiply(BigDecimal.valueOf(0.45));
		BigDecimal exps = availsouls.multiply(BigDecimal.valueOf(0.1));
		idlesouls = idlesouls.round(new MathContext(10, RoundingMode.HALF_DOWN));
		solsouls = solsouls.round(new MathContext(10, RoundingMode.HALF_DOWN));
		
		// Dividing exp fraction on 8+8+8+1+1+1+1+1+6 parts - 35 parts
		exps = exps.divide(BigDecimal.valueOf(35), 10, RoundingMode.HALF_DOWN);
		exps = exps.round(new MathContext(10, RoundingMode.HALF_DOWN));

		// Log2 exps (optimized)
		double log10conv = -exps.scale() * 3.321928;
		long expbase = (long) Math.floor(Math.log(exps.unscaledValue().longValue()) / 0.6931471806 + log10conv);
		
		if (expbase > 0) {
			Atman = expbase + 3;
			Kuma = expbase + 3;
			Dora = expbase + 3;
		}
		if (expbase > 3) {
			Fortuna = expbase;
			Dogcog = expbase;
			Chronos = expbase;
			Bubos = expbase;
			Vaagur = expbase;
			skills = expbase;
		}
		
		// Solomon levels calculation (optimized)
		solsouls = solsouls.multiply(BigDecimal.valueOf(2.5));
		Solomon = Formulas.pow04(solsouls);
		
		// Idle/active levels calculation
		if (strat == 0) {
			allocateIdleHybrid(idlesouls, 0.0);
		}
		else if (strat == 1) {
			allocateIdleHybrid(idlesouls, 0.5);
		}
		else {
			allocateIdleHybrid(idlesouls, 1.0);
		}
		
		// Calculating actual costs
		// Idle
		BigDecimal siya2cost = Siya.multiply(Siya).round(new MathContext(10, RoundingMode.HALF_DOWN));
		BigDecimal lib3cost = Lib.multiply(Lib).round(new MathContext(10, RoundingMode.HALF_DOWN));
		lib3cost = lib3cost.multiply(BigDecimal.valueOf(1.5));
		BigDecimal morgcost = Morg;		
		BigDecimal costsidle = siya2cost.add(lib3cost).add(morgcost);
		
		// Active
		BigDecimal frags2cost = Frags.multiply(Frags);
		BigDecimal juggcost = Formulas.pow25(Jugg);
		juggcost = juggcost.divide(BigDecimal.valueOf(2.5), 5, RoundingMode.HALF_DOWN);
		juggcost = juggcost.round(new MathContext(10, RoundingMode.HALF_DOWN));
		BigDecimal activecosts = frags2cost.add(juggcost);
		
		// Solomon
		BigDecimal solcost = Formulas.pow25(Solomon);
		solcost = solcost.divide(BigDecimal.valueOf(2.5), 5, RoundingMode.HALF_DOWN);
		solcost = solcost.round(new MathContext(10, RoundingMode.HALF_DOWN));		
		
		// Exponentials
		BigDecimal atman3cost = BigDecimal.valueOf(2).pow((int) Atman);
		atman3cost = atman3cost.multiply(BigDecimal.valueOf(3));
		BigDecimal fortuna11cost = BigDecimal.valueOf(2).pow((int) Fortuna);
		fortuna11cost = fortuna11cost.multiply(BigDecimal.valueOf(11));
		BigDecimal expcosts = atman3cost.add(fortuna11cost);
		
		// Total
		BigDecimal totalcosts = costsidle.add(activecosts).add(solcost).add(expcosts);
		
		// Calculating HS left
		BigDecimal leftover = availsouls.subtract(totalcosts);
		HScurr = leftover.divide(BigDecimal.valueOf(choreffect), 5, RoundingMode.HALF_DOWN);
		HScurr = HScurr.round(new MathContext(10, RoundingMode.HALF_DOWN));
		
		// If you want to test a particular ascension, you could do this
		/*if (HS.intValue() == 15500) {
			debug = true;
			Siya = BigDecimal.valueOf(66.66);
			Arg = BigDecimal.valueOf(66.66);
			Lib = BigDecimal.valueOf(61.73);
			Mammon = BigDecimal.valueOf(61.73);
			Mimzee = BigDecimal.valueOf(61.73);
			Solomon = BigDecimal.valueOf(56.8);
			Morg = BigDecimal.valueOf(4869);
			Dora = 8;
			Atman = 11;
			Kuma = 7;
			Fortuna = 6;
			Dogcog = 7;
			HScurr = BigDecimal.valueOf(100);
		}*/
		
		// Various debug
		//System.out.println("availsouls: " + availsouls);
		//System.out.println("idle costs: " + costsidle);
		//System.out.println("active costs: " + activecosts);
		//System.out.println("sol costs: " + solcost);
		//System.out.println("exp costs: " + expcosts);
		//System.out.println("total costs: " + totalcosts);
		//System.out.println("leftover: " + leftover);
		
		// Some more
		//System.out.println("Initial HS: " + HS);
		//System.out.println("Idle ancients - Siya " + Siya + ", Lib " + Lib + ", Arg " + Arg + ", Mimzee " + Mimzee + ", Mammon " + Mammon);
		//System.out.println("Morg " + Morg);
		//System.out.println("Solomon " + Solomon);
		//System.out.println("Exponential ancients - Atman " + Atman + ", Kuma " + Kuma + ", Dora " + Dora + ", Fortuna " + Fortuna + ", Dogcog " + Dogcog + ", Chronos " + Chronos + ", Bubos " + Bubos);
		//System.out.println("Active ancients - Frags " + Frags + ", Bhaal " + Bhaal + ", Vaagur " + Vaagur + ", Jugg " + Jugg);
		
	}
	
	private void allocateIdleHybrid(BigDecimal idlesouls, double ratio) {
		// Total idle/active cost: 2.286n^2 + n^2 + 1.4n^2 * a^2
		double morgratio = 1;
		double idleratio = 2.286;
		double activeratio = 1.4;
		if (ratio > 1) {
			morgratio = ratio * ratio; 
		}
		double total = idleratio + morgratio + activeratio * ratio * ratio; 
		idlesouls = idlesouls.multiply(BigDecimal.valueOf(1 / total));
		idlesouls = idlesouls.round(new MathContext(10, RoundingMode.HALF_DOWN));
		idlesouls = Formulas.pow05(idlesouls);

		Siya = idlesouls.round(new MathContext(10, RoundingMode.HALF_DOWN));
		Arg = idlesouls.round(new MathContext(10, RoundingMode.HALF_DOWN));
		Lib = idlesouls.multiply(BigDecimal.valueOf(0.926)).round(new MathContext(10, RoundingMode.HALF_DOWN));
		Mimzee = idlesouls.multiply(BigDecimal.valueOf(0.926)).round(new MathContext(10, RoundingMode.HALF_DOWN));
		Mammon = idlesouls.multiply(BigDecimal.valueOf(0.926)).round(new MathContext(10, RoundingMode.HALF_DOWN));
		
		Morg = idlesouls.multiply(idlesouls).round(new MathContext(10, RoundingMode.HALF_DOWN));
		if (ratio > 1) {
			Morg = Morg.multiply(BigDecimal.valueOf(ratio));
		}
		
		Frags = idlesouls.multiply(BigDecimal.valueOf(ratio)).round(new MathContext(10, RoundingMode.HALF_DOWN));
		Bhaal = idlesouls.multiply(BigDecimal.valueOf(ratio)).round(new MathContext(10, RoundingMode.HALF_DOWN));
		Jugg = Formulas.pow08(Frags);
	}

	public Ascresults simulateAsc() {
		// Tracking current and best progress
		AscStats progress = new AscStats();
		AscStats bestprogress = new AscStats();
		abonus = new ancBonus(this);

		// Always start idle, fastest instakilling
		currentstrat = 0;
		
		// Assuming energize at the start-long wait-3-reload-1-2-wait-1-2-3 for hybrid
		// Assuming one Sniperino relic for hybrid/active builds, level guessed
		luckytime = 30 + Formulas.getskillsEffect(skills) + (int) ((double) hze / 50);
		luckytime = luckytime * 2;
		// Energized lucky strikes
		luckychance = 1.00;
		double cdr = 1. - Formulas.getVaagurEffect(skills).doubleValue();
		// If we dont have enough cdr/duration to get them two straight
		if ((1800 * cdr) > luckytime) {
			// Averaging 
			luckychance = luckychance * luckytime / (luckytime + (1800 * cdr - luckytime / 2));
			luckytime = luckytime + (1800 * cdr - luckytime / 2);
		}
		
		// If we are active - calculate skills and average crit chance
		double powersurge = 1;
		double superclick = 1;
		double doublegold = 1;
		if (strat == 2) {
			luckychance = (0.59 + 1) / 2;
			luckytime = 30 + Formulas.getskillsEffect(skills) + (int) ((double) hze / 50);
			// You really, _really_ supposed to have permanent lucky strikes for active, but if not...
			if ((1800 * cdr) > luckytime) {
				luckychance = luckychance * luckytime / (1800 * cdr);
			}
			powersurge = 1 + (30 + Formulas.getskillsEffect(skills)) / (600 * cdr);
			if (powersurge > 2) { powersurge = 2; }
			superclick = 1 + (30 + Formulas.getskillsEffect(skills)) / (3600 * cdr) * 2;
			if (superclick > 3) { superclick = 3; }
			doublegold = 1 + (30 + Formulas.getskillsEffect(skills)) / (1800 * cdr);
			if (doublegold > 2) { doublegold = 2; }
		}
		
		//System.out.println("luckytime: " + luckytime + ", luckychance: " + luckychance);
		
		// Dont farm zones for more than a hour, dont go more than 30 boss zones without improvement
		int zonetime = 33;
		int maxfailedZones = 0;
		// Also dont go in hybrid mode after your double energized lucky strikes ended
		while ((zonetime < 3600000) && (maxfailedZones < 30) && (progress.time < endoftime)) {
			// Do we need to switch to active/hybrid?
			if ((zonetime > 18000) && (strat != 0) && (currentstrat == 0)) {
				if (strat == 1) {
					endoftime = progress.time + (long) luckytime * 1000;
				}
				currentstrat = 2;
				if (strat == 2) {
					abonus.activeBonusDamage.multiply(BigDecimal.valueOf(powersurge * superclick));
					abonus.bonusGold.multiply(BigDecimal.valueOf(doublegold));
				}
			}
			
			// Checking zone farm length
			zonetime = Formulas.getZoneTime(progress.zone, progress.hero, progress.level, this);
			
			// Saving zone farm rewards
			progress.souls = progress.souls.add(Formulas.getSouls(progress.zone, this));
			progress.souls = progress.souls.round(new MathContext(5, RoundingMode.HALF_DOWN));
			progress.gold = progress.gold.add(Formulas.getGoldFromZone(progress.zone, this));
			
			// Leveling heroes
			progress.gold = progress.gold.round(new MathContext(10, RoundingMode.HALF_DOWN));
			if (levelstrat == 0) {
				levelHeroes(progress);
			}
			if (levelstrat == 1) {
				levelHeroes25(progress);
			}
			progress.gold = progress.gold.round(new MathContext(10, RoundingMode.HALF_DOWN));
			
			hero = progress.hero;
			level = progress.level;
			
			// Time saving
			progress.time += zonetime;
			
			// Souls/hr calculation
			if ((progress.zone % 5) == 0) {
				maxfailedZones = compareSouls(progress, bestprogress, maxfailedZones);
			}
			
			// Long zones debug, every boss zone
			if (((progress.zone % 5) == 0) && (zonetime > 2000) && debug) {
				System.out.println("Zone: " + progress.zone
						+ ", ttotal=" + progress.time / 1000
						+ "s, t=" + zonetime
						+ "ms, h_lvl: " + progress.level
						+ ", gold: " + progress.gold
						+ ", HScurr: " + Formulas.getSouls(progress.zone, this).round(new MathContext(5, RoundingMode.FLOOR))
						+ ", HStot: " + progress.souls
						+ ", HS/hr: " + progress.soulshr);
			}
			
			// Next zone!
			progress.zone++;
		}
		
		if (debug) {
			System.out.println("Total HS: " + HS.round(new MathContext(5, RoundingMode.HALF_DOWN)).doubleValue()
				+ ", Ascended at " + bestprogress.time / 1000
				+ "s, HS: " + bestprogress.souls
				+ ", HS/hr: " + bestprogress.soulshr
				+ ", zone: " + bestprogress.zone
				+ ", hero: " + bestprogress.hero
				+ ", level: " + bestprogress.level);
		}
		//System.out.println("Solomon: " + Solomon + ", Siya: " + Siya + ", Atman: " + Atman + ", Morg: " + Morg);
		//System.out.println("Mammon: " + Mammon + ", Mimzee: " + Mimzee + ", Dora: " + Dora + ", Fortuna: " + Fortuna);

		// Returning results
		Ascresults res = new Ascresults();
		res.HS = bestprogress.souls;
		res.time = bestprogress.time;
		res.gilds = (int) ((double) bestprogress.zone - 90) / 10;
		res.zone = bestprogress.zone;

		return res;
	}
	
	private int compareSouls(AscStats progress, AscStats bestprogress, int maxfailedZones) {
		double timediv = (double) progress.time / 1000. / 3600.;
		progress.soulshr = progress.souls.divide(BigDecimal.valueOf(timediv), new MathContext(10, RoundingMode.HALF_DOWN));
		
		// Are we improving?
		if (progress.soulshr.compareTo(bestprogress.soulshr) > 0) {
			bestprogress.copyStat(progress);
			maxfailedZones = 0;
		}
		else {
			maxfailedZones++;
		}
		return maxfailedZones;
	}

	private void levelHeroes(AscStats progress) {
		while ((levelstrat == 0) && (progress.gold.compareTo(Formulas.getHeroCost(progress.hero, progress.level + 1)) == 1)) {
			progress.gold = progress.gold.subtract(Formulas.getHeroCost(progress.hero, progress.level + 1));
			progress.level++;
			if ((progress.hero == 1) && (progress.level >= 500)) {
				// Switch to 25-levels recruiting to improve performance
				levelstrat = 1;
			}
		}
	}
	
	private void levelHeroes25(AscStats progress) {
		while (progress.gold.compareTo(Formulas.getHeroCost25(progress.hero, progress.level + 1)) == 1) {
			progress.gold = progress.gold.subtract(Formulas.getHeroCost25(progress.hero, progress.level + 1));
			progress.level += 25;
			if ((progress.hero == 1) && (progress.level >= 2500)) {
				progress.hero = 2;
				progress.level = 950;
				levelHeroes25(progress);
			}
			if (((progress.hero > 1) && (progress.hero < 14)) && (progress.level >= 1500)) {
				if (progress.hero == 11) {
					// Bomber Max gold bonus
					abonus.bonusGold = abonus.bonusGold.multiply(BigDecimal.valueOf(1.5));
				}
				if (progress.hero == 12) {
					// Gog damage bonus
					abonus.idleBonusDamage = abonus.idleBonusDamage.multiply(BigDecimal.valueOf(1.5));
					abonus.activeBonusDamage = abonus.activeBonusDamage.multiply(BigDecimal.valueOf(1.5));
				}
				progress.hero = progress.hero + 1;
				progress.level = 950;
				levelHeroes25(progress);
			}
			if ((progress.hero == 14) && (progress.level >= 1500) && (progress.level <= 2500)) {
				progress.hero = 15;
				progress.level = 9300;
				levelHeroes25(progress);
			}
			if ((progress.hero == 15) && (progress.level >= 9800)) {
				progress.hero = 16;
				progress.level = 9600;
				levelHeroes25(progress);
			}
			if ((progress.hero == 16) && (progress.level >= 12600)) {
				progress.hero = 14;
				progress.level = 4900;
				levelHeroes25(progress);
			}
		}		
	}

}
