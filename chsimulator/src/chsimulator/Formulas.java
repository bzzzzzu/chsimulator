package chsimulator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Formulas {
	
	public static BigDecimal pow04(BigDecimal x) {
		// Round before use to 10 decimals if neccesary
		int scaleback = ((int) (x.scale() / 5)) * 2;
		int restscale = x.scale() - (int) (x.scale() / 5) * 5;
		double tempvalue = x.unscaledValue().longValue() * Math.pow(10, -restscale);
		tempvalue = Math.pow(tempvalue, 0.4);
		return BigDecimal.valueOf(tempvalue).scaleByPowerOfTen(-scaleback);
	}
	
	public static BigDecimal pow08(BigDecimal x) {
		// Round before use to 10 decimals if neccesary
		int scaleback = ((int) (x.scale() / 5)) * 4;
		int restscale = x.scale() - (int) (x.scale() / 5) * 5;
		double tempvalue = x.unscaledValue().longValue() * Math.pow(10, -restscale);
		tempvalue = Math.pow(tempvalue, 0.8);
		return BigDecimal.valueOf(tempvalue).scaleByPowerOfTen(-scaleback);
	}
	
	public static BigDecimal pow05(BigDecimal x) {
		// Round before use to 10 decimals if neccesary
		int scaleback = ((int) (x.scale() / 2)) * 1;
		int restscale = x.scale() - (int) (x.scale() / 2) * 2;
		double tempvalue = x.unscaledValue().longValue() * Math.pow(10, -restscale);
		tempvalue = Math.pow(tempvalue, 0.5);
		return BigDecimal.valueOf(tempvalue).scaleByPowerOfTen(-scaleback);
	}
	
	public static BigDecimal pow25(BigDecimal x) {
		// Round before use to 10 decimals if neccesary
		int scaleback = ((int) (x.scale() / 2)) * 5;
		int restscale = x.scale() - (int) (x.scale() / 2) * 2;
		double tempvalue = x.unscaledValue().longValue() * Math.pow(10, -restscale);
		tempvalue = Math.pow(tempvalue, 2.5);
		return BigDecimal.valueOf(tempvalue).scaleByPowerOfTen(-scaleback);
	}
	
	public static BigDecimal getDamage(int id, long level, Ascension asc) {
		BigDecimal damage = getHeroDamage(id, level);
		
		// Everything is calculated per ascension to increase performance
		damage = damage.multiply(asc.abonus.idleBonusDamage);
		damage = damage.round(new MathContext(10, RoundingMode.HALF_DOWN));
		
		return damage;
	}
	
	public static BigDecimal getActiveDamage(int id, long level, Ascension asc) {
		BigDecimal damage = getHeroDamage(id, level);

		// Everything is calculated per ascension to increase performance
		damage = damage.multiply(asc.abonus.activeBonusDamage);
		
		// Jugg combo is dynamic, though
		BigDecimal juggcombo = asc.abonus.juggBonus.multiply(BigDecimal.valueOf(asc.combo));
		juggcombo = juggcombo.add(BigDecimal.valueOf(1));
		damage = damage.multiply(juggcombo);
		
		damage = damage.round(new MathContext(5, RoundingMode.HALF_DOWN));
		
		return damage;
	}
	
	public static BigDecimal getHeroDamage(int id, long level) {
		// TODO: work with increasing Wep level on-the-fly to support high AS simulations
		return Tables.herodamage[id][(int) level];
	}
	
	public static BigDecimal getHeroCost25(int id, int level) {
		// TODO: work with increasing Wep level on-the-fly to support high AS simulations
		return Tables.herocosts25[id][level];
	}
	
	public static BigDecimal getHeroCost(int id, int level) {
		return Tables.herocosts[id][level];
	}
	
	public static BigDecimal getHp(int zone) {
		// TODO: work with increasing zone hp on-the-fly to support high AS simulations
		return Tables.zoneHP[zone];
	}
	
	public static BigDecimal getTotalZoneHp(int zone, Ascension asc) {
		BigDecimal hp = getHp(zone);
		
		// Just multiply by average actual amount of monsters
		BigDecimal mobskuma = BigDecimal.valueOf(10).subtract(getKumaEffect(asc.Kuma));
		hp = hp.multiply(mobskuma);
		hp = hp.round(new MathContext(10, RoundingMode.HALF_DOWN));
		
		return hp;
	}
	
	public static int getZoneTime(int zone, int hero, int level, Ascension asc) {
		if (asc.currentstrat == 0) {
			BigDecimal currentdamage = Formulas.getDamage(hero, (long) level, asc);
			BigDecimal totalhp = Formulas.getTotalZoneHp(zone, asc);
			currentdamage = currentdamage.round(new MathContext(10, RoundingMode.HALF_DOWN));
			totalhp = totalhp.round(new MathContext(10, RoundingMode.HALF_DOWN));
			
			// Probably the most time-consuming thing in the simulation
			// Cant see how i can avoid divides here
			BigDecimal zonetime = BigDecimal.ONE;
			if ((totalhp.scale() + 5) < currentdamage.scale()) {
				// surely instakill
			}
			else {
				zonetime = totalhp.divide(currentdamage, 20, RoundingMode.HALF_DOWN);
				zonetime = zonetime.scaleByPowerOfTen(3);
			}
		
			// Not 5 so debug could see proper amount of time spent on normal zones
			// Chronos/Bubos not simulated, it is not optimal to farm zones for that long anyway
			// Bosses are just magically farmed like normal zones
			// with the exception of no extra time from mobs animation
			if ((zone % 5) != 4) {
				zonetime = zonetime.add(asc.abonus.mobsKuma);
			}
		
			// One frame of switching between zones
			zonetime = zonetime.add(BigDecimal.valueOf(33));
			return zonetime.intValue();
		}
		else {
			BigDecimal currentdamage = Formulas.getActiveDamage(hero, (long) level, asc);
			BigDecimal totalhp = Formulas.getTotalZoneHp(zone, asc);
			
			// We calculate in amount of critical clicks you need to kill the mob
			double clicks = totalhp.divide(currentdamage, 20, RoundingMode.HALF_DOWN).doubleValue();
			clicks = Math.ceil(clicks);
			
			// Not every click is critical, increasing average clicks accordingly
			// 100ms as for 10 clicks per second
			double timepermob = clicks / asc.luckychance * 100;
			double totalclicks = clicks / asc.luckychance;
			
			// TODO: this probably deserves its own spot in ancBonus
			BigDecimal mobskuma = BigDecimal.valueOf(10).subtract(getKumaEffect(asc.Kuma));
			BigDecimal zonetime = mobskuma.multiply(BigDecimal.valueOf(timepermob));
			
			// Increasing Juggernaut combo for average zone clicks
			totalclicks = totalclicks * mobskuma.doubleValue();
			asc.combo = asc.combo + totalclicks;
			
			// TODO: optimize with ancBonus
			mobskuma = mobskuma.subtract(BigDecimal.valueOf(1));
			mobskuma = mobskuma.multiply(BigDecimal.valueOf(500));
			if ((zone % 5) != 4) {
				zonetime = zonetime.add(mobskuma);
			}
			
			// Single frame of animation again
			zonetime = zonetime.add(BigDecimal.valueOf(33));
			return zonetime.intValue();
		}
		
	}
	
	public static BigDecimal getSouls(int zone, Ascension asc) {
		// Two part of souls gain
		double souls = 0;
		BigDecimal trans = BigDecimal.valueOf(0);
		
		// Hardcoded results
		if (zone < 100) { return BigDecimal.valueOf(0); }
		if ((zone % 5) != 0) { return BigDecimal.valueOf(0); }
		if (zone == 100) { return BigDecimal.valueOf(1); }
		
		// Normal souls gain
		double zonepow = ((double) zone - 80) / 25;
		souls = Math.pow(zonepow, 1.3);

		// Precalculated at the start of ascension
		trans = Tables.transsouls[zone];
		
		// Ponyboy bonus
		souls = souls * asc.abonus.ponyDouble;
		trans = trans.multiply(asc.abonus.ponyBonus);

		// Transcended souls limit and Borb bonus
		if (trans.compareTo(asc.abonus.borbLimit) > 0) {
			trans = asc.abonus.borbLimit;
		}
		
		// Total souls gain
		if (souls != Double.POSITIVE_INFINITY) {
			trans = trans.add(BigDecimal.valueOf(souls));
		}

		// Guaranteed primals, otherwise multiply by Atman bonus
		if ((zone % 100) == 0 || (zone == 110) || (zone == 120) || (zone == 130)) {
		}
		else {
			trans = trans.multiply(asc.abonus.atmanMult);
		}
		trans.round(new MathContext(5, RoundingMode.FLOOR));
		
		return trans;
	}
	
	public static BigDecimal getGold(int zone, Ascension asc) {
		BigDecimal gold = getHp(zone);
		
		// Idle bonuses only for idle
		if (asc.currentstrat == 0) {
			gold = gold.multiply(asc.abonus.idleBonusGold);
		}
		
		gold = gold.multiply(asc.abonus.bonusGold);
		gold = gold.round(new MathContext(10, RoundingMode.FLOOR));
		
		return gold;
	}
	
	public static BigDecimal getGoldFromZone(int zone, Ascension asc) {
		BigDecimal gold = getGold(zone, asc);
		
		// Yeah, this place could use some optimization also
		BigDecimal mobskuma = BigDecimal.valueOf(10).subtract(getKumaEffect(asc.Kuma));
		gold = gold.multiply(mobskuma);
		
		gold = gold.round(new MathContext(10, RoundingMode.HALF_DOWN));
		
		return gold;
	}
	
	public static BigDecimal getZoneMult(int zone) {
		if (zone < 140) {
			return BigDecimal.valueOf(1.55);
		}
		else if (zone < 500) {
			return BigDecimal.valueOf(1.145);
		}
		else {
			return BigDecimal.valueOf(1.145 + 0.005 * Math.floor(zone / 500));
		}
	}
	
	public static BigDecimal getSiyaEffect(BigDecimal level) {
		BigDecimal effect = level.subtract(BigDecimal.valueOf(100));
		effect = effect.multiply(BigDecimal.valueOf(15));
		effect = effect.add(BigDecimal.valueOf(2040));
		
		// Initial 100 levels goes from table
		if (level.compareTo(BigDecimal.valueOf(100)) < 0) {
			effect = Tables.siyalib[level.intValue()];
		}
		
		BigDecimal bigeffect = effect.setScale(10, RoundingMode.HALF_DOWN);
		bigeffect = bigeffect.divide(BigDecimal.valueOf(100).setScale(2));
		
		return bigeffect;
	}
	
	public static BigDecimal getLibEffect(BigDecimal level) {
		BigDecimal effect = level.subtract(BigDecimal.valueOf(100));
		effect = effect.multiply(BigDecimal.valueOf(15));
		effect = effect.add(BigDecimal.valueOf(2040));
		
		// Initial 100 levels goes from table
		if (level.compareTo(BigDecimal.valueOf(100)) < 0) {
			effect = Tables.siyalib[level.intValue()];
		}
		
		BigDecimal bigeffect = effect.setScale(10, RoundingMode.HALF_DOWN);
		bigeffect = bigeffect.divide(BigDecimal.valueOf(100).setScale(2));
		
		return bigeffect;
	}
	
	public static BigDecimal getTinEffect(BigDecimal level) {
		BigDecimal effect = level.multiply(BigDecimal.valueOf(10));
		
		BigDecimal bigeffect = effect.setScale(10, RoundingMode.HALF_DOWN);
		bigeffect = bigeffect.divide(BigDecimal.valueOf(100).setScale(2));
		
		return bigeffect;
	}
	
	public static BigDecimal getSolomonEffect(BigDecimal level) {
		BigDecimal effect = level.subtract(BigDecimal.valueOf(80));
		effect = effect.add(BigDecimal.valueOf(200));
		
		// Initial 80 levels goes from table
		if (level.compareTo(BigDecimal.valueOf(80)) < 0) {
			effect = Tables.sol[level.intValue()];
		}
		
		BigDecimal bigeffect = effect.setScale(10, RoundingMode.HALF_DOWN);
		bigeffect = bigeffect.divide(BigDecimal.valueOf(100).setScale(2));
		
		return bigeffect;
	}
	
	public static BigDecimal getMammonEffect(BigDecimal level) {
		BigDecimal effect = level.multiply(BigDecimal.valueOf(5));
		
		BigDecimal bigeffect = effect.setScale(10, RoundingMode.HALF_DOWN);
		bigeffect = bigeffect.divide(BigDecimal.valueOf(100).setScale(2));
		
		return bigeffect;
	}
	
	public static BigDecimal getArgaivEffect(BigDecimal level) {
		BigDecimal effect = level.multiply(BigDecimal.valueOf(2));
		
		BigDecimal bigeffect = effect.setScale(10, RoundingMode.HALF_DOWN);
		bigeffect = bigeffect.divide(BigDecimal.valueOf(100).setScale(2));
		
		return bigeffect;
	}
	
	public static BigDecimal getMimzeeEffect(BigDecimal level) {
		BigDecimal effect = level.multiply(BigDecimal.valueOf(50));
		
		BigDecimal bigeffect = effect.setScale(10, RoundingMode.HALF_DOWN);
		bigeffect = bigeffect.divide(BigDecimal.valueOf(100).setScale(2));
		
		return bigeffect;
	}
	
	public static BigDecimal getFragsEffect(BigDecimal level) {
		BigDecimal effect = level.multiply(BigDecimal.valueOf(20));
		
		BigDecimal bigeffect = effect.setScale(10, RoundingMode.HALF_DOWN);
		bigeffect = bigeffect.divide(BigDecimal.valueOf(100).setScale(2));
		
		return bigeffect;
	}
	
	public static BigDecimal getBhaalEffect(BigDecimal level) {
		BigDecimal effect = level.multiply(BigDecimal.valueOf(15));
		
		BigDecimal bigeffect = effect.setScale(10, RoundingMode.HALF_DOWN);
		bigeffect = bigeffect.divide(BigDecimal.valueOf(100).setScale(2));
		
		return bigeffect;
	}
	
	public static BigDecimal getJuggEffect(BigDecimal level) {
		BigDecimal effect = level.multiply(BigDecimal.valueOf(0.01));
		
		BigDecimal bigeffect = effect.setScale(10, RoundingMode.HALF_DOWN);
		bigeffect = bigeffect.divide(BigDecimal.valueOf(100).setScale(2));
		
		return bigeffect;
	}
	
	public static long getskillsEffect(long level) {
		return level * 2;
	}
	
	public static BigDecimal getDoraEffect(long level) {
		// Precalculated
		return Tables.doraeffect[(int) level];
	}
	
	public static BigDecimal getAtmanEffect(long level) {
		double effect = 75 * (1 - Math.pow(Math.E, (level * 0.013 * -1)));
		
		BigDecimal bigeffect = BigDecimal.valueOf(effect).setScale(10, RoundingMode.HALF_DOWN);
		bigeffect = bigeffect.divide(BigDecimal.valueOf(1).setScale(2));
		
		return bigeffect;
	}
	
	public static BigDecimal getBubosEffect(long level) {
		double effect = 50 * (1 - Math.pow(Math.E, (level * 0.02 * -1)));
		
		BigDecimal bigeffect = BigDecimal.valueOf(effect).setScale(10, RoundingMode.HALF_DOWN);
		bigeffect = bigeffect.divide(BigDecimal.valueOf(100).setScale(2));
		
		return bigeffect;
	}
	
	public static BigDecimal getChronosEffect(long level) {
		double effect = 30 * (1 - Math.pow(Math.E, (level * 0.034 * -1)));
		
		BigDecimal bigeffect = BigDecimal.valueOf(effect).setScale(10, RoundingMode.HALF_DOWN);
		bigeffect = bigeffect.divide(BigDecimal.valueOf(1).setScale(2));
		
		return bigeffect;
	}
	
	public static BigDecimal getDogcogEffect(long level) {
		// Precalculated
		return Tables.dogcogeffect[(int) level];
	}
	
	public static BigDecimal getRevolcEffect(long level) {
		double effect = 100 * (1 - Math.pow(Math.E, (level * 0.01 * -1)));
		
		BigDecimal bigeffect = BigDecimal.valueOf(effect).setScale(10, RoundingMode.HALF_DOWN);
		bigeffect = bigeffect.divide(BigDecimal.valueOf(100).setScale(2));
		
		return bigeffect;
	}
	
	public static BigDecimal getFortunaEffect(long level) {
		// Precalculated
		return Tables.fortunaeffect[(int) level];
	}
	
	public static BigDecimal getKumaEffect(long level) {
		// Precalculated
		return Tables.kumaeffect[(int) level];
	}
	
	public static BigDecimal getVaagurEffect(long level) {
		double effect = 75 * (1 - Math.pow(Math.E, (level * 0.026 * -1)));
		
		BigDecimal bigeffect = BigDecimal.valueOf(effect).setScale(10, RoundingMode.HALF_DOWN);
		bigeffect = bigeffect.divide(BigDecimal.valueOf(100).setScale(2));
		
		return bigeffect;
	}
}
