package chsimulator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Tables {
	
	public static BigDecimal[][] herocosts = new BigDecimal[22][300000];
	public static BigDecimal[][] herocosts25 = new BigDecimal[22][300000];
	public static BigDecimal[] zoneHP = new BigDecimal[300000];
	public static BigDecimal[][] herodamage = new BigDecimal[22][300000];
	
	public static BigDecimal[] doraeffect = new BigDecimal[10000];
	public static BigDecimal[] kumaeffect = new BigDecimal[10000];
	public static BigDecimal[] fortunaeffect = new BigDecimal[10000];
	public static BigDecimal[] dogcogeffect = new BigDecimal[10000];
	
	public static BigDecimal[] siyalib = new BigDecimal[120];
	public static BigDecimal[] sol = new BigDecimal[120];
	
	public static BigDecimal[] transsouls = new BigDecimal[100000];
	
	public static void precalcHeroCost() {
		calcHeroCost(1, BigDecimal.valueOf(100000));
		calcHeroCost(2, BigDecimal.valueOf(1e55));
		calcHeroCost(3, BigDecimal.valueOf(1e70));
		calcHeroCost(4, BigDecimal.valueOf(1e85));
		calcHeroCost(5, BigDecimal.valueOf(1e100));
		calcHeroCost(6, BigDecimal.valueOf(1e115));
		calcHeroCost(7, BigDecimal.valueOf(1e130));
		calcHeroCost(8, BigDecimal.valueOf(1e145));
		calcHeroCost(9, BigDecimal.valueOf(1e160));
		calcHeroCost(10, BigDecimal.valueOf(1e175));
		calcHeroCost(11, BigDecimal.valueOf(1e190));
		calcHeroCost(12, BigDecimal.valueOf(1e205));
		calcHeroCost(13, BigDecimal.valueOf(1e220));
		calcHeroCost(14, BigDecimal.valueOf(1e235));
		calcHeroCost(15, BigDecimal.valueOf(20000));
		calcHeroCost(16, BigDecimal.valueOf(4e12));
		
		calcHeroCost(17, BigDecimal.valueOf(1, -500));
		calcHeroCost(18, BigDecimal.valueOf(1, -1000));
		calcHeroCost(19, BigDecimal.valueOf(1, -2000));
		calcHeroCost(20, BigDecimal.valueOf(1, -4000));
		calcHeroCost(21, BigDecimal.valueOf(1, -8000));
	}
	
	public static void calcHeroCost(int id, BigDecimal base) {
		BigDecimal cost = base;
		BigDecimal costscale = BigDecimal.valueOf(1.07);

		int i = 1;
		while (i < 300000) {
			herocosts[id][i] = cost;
			
			// Also populating 25 levels hiring table
			if ((i % 25) == 1) {
				herocosts25[id][i] = cost.multiply(BigDecimal.valueOf(63.249));
			}
			
			cost = cost.multiply(costscale);
			cost = cost.round(new MathContext(10, RoundingMode.HALF_DOWN));
			i++;
		}
	}
	
	public static void precalcZoneHP() {
		BigDecimal hp = BigDecimal.valueOf(10);
		
		int i = 1;
		while (i < 300000) {
			zoneHP[i] = hp;
			hp = hp.multiply(Formulas.getZoneMult(i));
			hp = hp.round(new MathContext(10, RoundingMode.HALF_DOWN));
			i++;
		}
	}
	
	public static void precalcHeroDamage() {
		// Samurai
		BigDecimal damage = BigDecimal.valueOf(3725).setScale(10);
		herodamage[1][1] = damage;
		int i = 1;
		while (i < 299999) {
			i++;
			if ((i == 10) || (i == 25) || (i == 50)) {
				damage = damage.multiply(BigDecimal.valueOf(2));
			}
			if (i == 75) {
				damage = damage.multiply(BigDecimal.valueOf(2.5));
			}
			if (i > 199) {
				if ((i % 25) == 0) {
					damage = damage.multiply(BigDecimal.valueOf(4));
				}
				if (((i % 1000) == 0) && (i > 999) && (i < 8001)) {
					damage = damage.multiply(BigDecimal.valueOf(2.5));
				}
			}
			damage = damage.round(new MathContext(10, RoundingMode.HALF_DOWN));
			BigDecimal finaldamage = damage.multiply(BigDecimal.valueOf(i));
			herodamage[1][i] = finaldamage.round(new MathContext(10, RoundingMode.HALF_DOWN));
		}
		
		// Betty
		damage = BigDecimal.valueOf(976).setScale(10);
		herodamage[15][1] = damage;
		i = 1;
		while (i < 299999) {
			i++;
			if (i > 199) {
				if ((i % 25) == 0) {
					damage = damage.multiply(BigDecimal.valueOf(4));
				}
				if (((i % 1000) == 0) && (i > 999) && (i < 8001)) {
					damage = damage.multiply(BigDecimal.valueOf(2.5));
				}
				if (i == 9001) {
					damage = damage.multiply(BigDecimal.valueOf(5e6));
				}
			}
			damage = damage.round(new MathContext(10, RoundingMode.HALF_DOWN));
			BigDecimal finaldamage = damage.multiply(BigDecimal.valueOf(i));
			herodamage[15][i] = finaldamage.round(new MathContext(10, RoundingMode.HALF_DOWN));
		}
		
		// Midas
		damage = BigDecimal.valueOf(3.017e9).setScale(10);
		herodamage[16][1] = damage;
		i = 1;
		while (i < 299999) {
			i++;
			if (i > 199) {
				if ((i % 25) == 0) {
					damage = damage.multiply(BigDecimal.valueOf(4));
				}
				if (((i % 1000) == 0) && (i > 999) && (i < 8001)) {
					damage = damage.multiply(BigDecimal.valueOf(2.5));
				}
				if (i == 9001) {
					damage = damage.multiply(BigDecimal.valueOf(1e8));
				}
			}
			damage = damage.round(new MathContext(10, RoundingMode.HALF_DOWN));
			BigDecimal finaldamage = damage.multiply(BigDecimal.valueOf(i));
			herodamage[16][i] = finaldamage.round(new MathContext(10, RoundingMode.HALF_DOWN));
		}
		
		precalcRangerDamage(2, BigDecimal.valueOf(9.66e44)); //atlas
		precalcRangerDamage(3, BigDecimal.valueOf(7.113e57)); //terra
		precalcRangerDamage(4, BigDecimal.valueOf(5.24e70)); //phthalo
		precalcRangerDamage(5, BigDecimal.valueOf(3.861e83)); //banana
		precalcRangerDamage(6, BigDecimal.valueOf(2.845e96)); //lilin
		precalcRangerDamage(7, BigDecimal.valueOf(2.096e109)); //cadmia
		precalcRangerDamage(8, BigDecimal.valueOf(1.544e122)); //alabaster
		precalcRangerDamage(9, BigDecimal.valueOf(1.137e135)); //cadmia
		
		precalcRangerDamage(10, BigDecimal.valueOf(8.376e147)); //chiron
		precalcRangerDamage(11, BigDecimal.valueOf(6.171e160)); //moloch
		precalcRangerDamage(12, BigDecimal.valueOf(4.546e173)); //max
		precalcRangerDamage(13, BigDecimal.valueOf(3.349e186)); //gog
		precalcRangerDamage(14, BigDecimal.valueOf(2.467e199)); //wep
		
		precalcRangerDamage(17, BigDecimal.valueOf(1820, -422)); //tsuchi
		precalcRangerDamage(18, BigDecimal.valueOf(1341, -843)); //skogur
		precalcRangerDamage(19, BigDecimal.valueOf(9885, -1675)); //moeru
		precalcRangerDamage(20, BigDecimal.valueOf(7283, -3330)); //zilar
		precalcRangerDamage(21, BigDecimal.valueOf(5366, -6627)); //madzi
	}
	
	public static void precalcRangerDamage(int id, BigDecimal initial) {
		BigDecimal damage = initial;
		
		herodamage[id][1] = damage;
		int i = 1;
		while (i < 299999) {
			i++;
			if ((id == 12) || (id == 13)) {
				if ((i == 10) || (i == 25)) {
					damage = damage.multiply(BigDecimal.valueOf(2));
				}
				if (i == 50) {
					damage = damage.multiply(BigDecimal.valueOf(3.5));
				}
			}
			else if (id == 14) {
				if (i == 25) {
					damage = damage.multiply(BigDecimal.valueOf(3));
				}
				if (i == 100) {
					damage = damage.multiply(BigDecimal.valueOf(3.5));
				}
			}
			else if (id == 17) {
				if (i == 1000) {
					damage = damage.multiply(BigDecimal.valueOf(3.5));
				}
				if (i == 2000) {
					damage = damage.multiply(BigDecimal.valueOf(2.5));
				}
			}
			else if (id == 18) {
				if (i == 1000) {
					damage = damage.multiply(BigDecimal.valueOf(2));
				}
				if (i == 2000) {
					damage = damage.multiply(BigDecimal.valueOf(2));
				}
				if (i == 4000) {
					damage = damage.multiply(BigDecimal.valueOf(2));
				}
				if (i == 8000) {
					damage = damage.multiply(BigDecimal.valueOf(2.5));
				}
			}
			else if (id == 19) {
				if (i == 1000) {
					damage = damage.multiply(BigDecimal.valueOf(2));
				}
				if (i == 2000) {
					damage = damage.multiply(BigDecimal.valueOf(2.5));
				}
				if (i == 4000) {
					damage = damage.multiply(BigDecimal.valueOf(3));
				}
			}
			else if (id == 20) {
				if (i == 1000) {
					damage = damage.multiply(BigDecimal.valueOf(11));
				}
			}
			else if (id == 21) {
				if (i == 1000) {
					damage = damage.multiply(BigDecimal.valueOf(2));
				}
				if (i == 2000) {
					damage = damage.multiply(BigDecimal.valueOf(2.5));
				}
				if (i == 4000) {
					damage = damage.multiply(BigDecimal.valueOf(3));
				}
				if (i == 8000) {
					damage = damage.multiply(BigDecimal.valueOf(3.5));
				}
			}
			else {
				if ((i == 10) || (i == 25) || (i == 50)) {
					damage = damage.multiply(BigDecimal.valueOf(2));
				}
				if (i == 100) {
					damage = damage.multiply(BigDecimal.valueOf(2.5));
				}
			}
			if (i > 199) {
				if ((i % 25) == 0) {
					damage = damage.multiply(BigDecimal.valueOf(4));
					damage = damage.round(new MathContext(10, RoundingMode.HALF_DOWN));
				}
				if (((i % 1000) == 0) && (i > 999) && (i < 8001)) {
					damage = damage.multiply(BigDecimal.valueOf(2.5));
				}
				if ((i == 525) || (i == 550) || (i == 575) || (i == 600)
					|| (i == 625) || (i == 650) || (i == 675) || (i == 700)
					|| (i == 725)) {
					damage = damage.multiply(BigDecimal.valueOf(1.25));
				}
			}
			damage = damage.round(new MathContext(10, RoundingMode.HALF_DOWN));
			BigDecimal finaldamage = damage.multiply(BigDecimal.valueOf(i));
			herodamage[id][i] = finaldamage.round(new MathContext(10, RoundingMode.HALF_DOWN));
		}
	}
	
	public static void precalcExpAncients() {
		precalcDoraEffect();
		precalcDogcogEffect();
		precalcKumaEffect();
		precalcFortunaEffect();
		precalcSiyaLibSolstarting();
	}
	
	public static void precalcDoraEffect() {
		for (int i = 0; i < 10000; i++) {
			BigDecimal bigeffect = BigDecimal.valueOf(9900 * (1 - Math.pow(Math.E, (i * 0.002 * -1))));
			bigeffect = bigeffect.divide(BigDecimal.valueOf(10000));
			Tables.doraeffect[i] = bigeffect.round(new MathContext(10, RoundingMode.HALF_DOWN));
		}
	}
	
	public static void precalcDogcogEffect() {
		for (int i = 0; i < 10000; i++) {
			BigDecimal bigeffect = BigDecimal.valueOf(99 * (1 - Math.pow(Math.E, (i * 0.01 * -1))));
			bigeffect = bigeffect.divide(BigDecimal.valueOf(100));
			Tables.dogcogeffect[i] = bigeffect.round(new MathContext(10, RoundingMode.HALF_DOWN));
		}
	}
	
	public static void precalcKumaEffect() {
		for (int i = 0; i < 10000; i++) {
			BigDecimal bigeffect = BigDecimal.valueOf(8 * (1 - Math.pow(Math.E, (i * 0.01 * -1))));
			bigeffect = bigeffect.divide(BigDecimal.valueOf(1));
			Tables.kumaeffect[i] = bigeffect.round(new MathContext(10, RoundingMode.HALF_DOWN));
		}
	}
	
	public static void precalcFortunaEffect() {
		for (int i = 0; i < 10000; i++) {
			BigDecimal bigeffect = BigDecimal.valueOf(100 * (1 - Math.pow(Math.E, (i * 0.0025 * -1))));
			bigeffect = bigeffect.divide(BigDecimal.valueOf(100));
			Tables.fortunaeffect[i] = bigeffect.round(new MathContext(10, RoundingMode.HALF_DOWN));
		}
	}

	public static void precalcSiyaLibSolstarting() {
		// Ugly, but whatever
		for (int i = 0; i < 10; i++) {
			siyalib[i] = BigDecimal.valueOf(25 * i);
		}
		for (int i = 0; i < 10; i++) {
			siyalib[10 + i] = siyalib[9].add(BigDecimal.valueOf(24 * (i + 1)));
		}
		for (int i = 0; i < 10; i++) {
			siyalib[20 + i] = siyalib[19].add(BigDecimal.valueOf(23 * (i + 1)));
		}
		for (int i = 0; i < 10; i++) {
			siyalib[30 + i] = siyalib[29].add(BigDecimal.valueOf(22 * (i + 1)));
		}
		for (int i = 0; i < 10; i++) {
			siyalib[40 + i] = siyalib[39].add(BigDecimal.valueOf(21 * (i + 1)));
		}
		for (int i = 0; i < 10; i++) {
			siyalib[50 + i] = siyalib[49].add(BigDecimal.valueOf(20 * (i + 1)));
		}
		for (int i = 0; i < 10; i++) {
			siyalib[60 + i] = siyalib[59].add(BigDecimal.valueOf(19 * (i + 1)));
		}
		for (int i = 0; i < 10; i++) {
			siyalib[70 + i] = siyalib[69].add(BigDecimal.valueOf(18 * (i + 1)));
		}
		for (int i = 0; i < 10; i++) {
			siyalib[80 + i] = siyalib[79].add(BigDecimal.valueOf(17 * (i + 1)));
		}
		for (int i = 0; i < 10; i++) {
			siyalib[90 + i] = siyalib[89].add(BigDecimal.valueOf(16 * (i + 1)));
		}
		for (int i = 0; i < 10; i++) {
			siyalib[100 + i] = siyalib[99].add(BigDecimal.valueOf(15 * (i + 1)));
		}
		
		for (int i = 0; i < 21; i++) {
			sol[i] = BigDecimal.valueOf(5 * i);
		}
		for (int i = 1; i < 21; i++) {
			sol[20 + i] = sol[19].add(BigDecimal.valueOf(4 * (i + 1)));
		}
		for (int i = 1; i < 21; i++) {
			sol[40 + i] = sol[39].add(BigDecimal.valueOf(3 * (i + 1)));
		}
		for (int i = 1; i < 21; i++) {
			sol[60 + i] = sol[59].add(BigDecimal.valueOf(2 * (i + 1)));
		}
		for (int i = 1; i < 21; i++) {
			sol[80 + i] = sol[79].add(BigDecimal.valueOf(1 * (i + 1)));
		}
	}

	public static void precalcTransPower(double TP, double Phan) {
		double phanpower = (double) Phan / 1000. * -1;
		double usedTP = TP + 0.5 * (1 - Math.pow(Math.E, phanpower));
		
		BigDecimal souls = BigDecimal.valueOf(20);
		for (int i = 105; i < 100000; i+=5) {
			souls = souls.multiply(BigDecimal.valueOf(usedTP));
			souls = souls.round(new MathContext(10, RoundingMode.HALF_DOWN));
			transsouls[i] = souls;
		}
	}
}
