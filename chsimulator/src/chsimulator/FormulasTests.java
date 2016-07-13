package chsimulator;

import java.math.BigDecimal;

import org.junit.Test;

import static org.junit.Assert.*;

public class FormulasTests {
	@Test
	public void testHeroCost() {
		Tables.precalcHeroCost();
		assertEquals(4.597323501E19, Formulas.getHeroCost(1, 500).doubleValue(), 1e14);
	}
	
	@Test
	public void testHeroCost2() {
		Tables.precalcHeroCost();
		assertEquals(5.377e40, Formulas.getHeroCost(1, 1217).doubleValue(), 1e38);
	}
	
	@Test
	public void testZoneHP() {
		Tables.precalcZoneHP();
		assertEquals(3.936613411E217, Formulas.getHp(3112).doubleValue(), 1e212);
	}
	
	@Test
	public void testHeroDamage() {
		Tables.precalcHeroDamage();
		Tables.precalcExpAncients();
		//1.669e91 / 3096 sam
		//8.801e50 / 1 atlas
		AscSettings set = new AscSettings(BigDecimal.valueOf(1000));
		Ascension asc = new Ascension(set);
		asc.allocateHS();
		asc.Arg = BigDecimal.valueOf(414);
		asc.Morg = BigDecimal.valueOf(200000);
		asc.Siya = BigDecimal.valueOf(400);
		asc.HScurr = BigDecimal.valueOf(73254);
		asc.gilds = 81;
		asc.abonus = new ancBonus(asc);
		
		//asc.gilds = 0;
		//assertEquals(Formulas.getDamage(2, 1, asc).doubleValue(), 5.617e52, 1e50);
		assertEquals(1.092e93, Formulas.getDamage(1, 3205, asc).doubleValue(), 1e91);
	}
	
	@Test
	public void testHeroDamage2() {
		Tables.precalcHeroDamage();
		Tables.precalcExpAncients();
		AscSettings set = new AscSettings(BigDecimal.valueOf(1000000));
		Ascension asc = new Ascension(set);
		asc.allocateHS();
		asc.Arg = BigDecimal.valueOf(1270000);
		asc.Morg = BigDecimal.valueOf(2e12);
		asc.Siya = BigDecimal.valueOf(1300000);
		asc.HScurr = BigDecimal.valueOf(7.106e9);
		asc.gilds = 352;
		asc.Xyl = 5;
		asc.abonus = new ancBonus(asc);

		//7.955e111 / 3372 sam, idle
		assertEquals(7.955e111, Formulas.getDamage(1, 3372, asc).doubleValue(), 1e107);
		//4.990e117 / 1358 terra, idle
		assertEquals(4.990e117, Formulas.getDamage(3, 1358, asc).doubleValue(), 1e114);
	}
	
	@Test
	public void testBettyDamage() {
		Tables.precalcHeroDamage();
		Tables.precalcExpAncients();
		AscSettings set = new AscSettings(BigDecimal.valueOf(1000));
		Ascension asc = new Ascension(set);
		asc.allocateHS();
		asc.Arg = BigDecimal.valueOf(1270000);
		asc.Morg = BigDecimal.valueOf(2e12);
		asc.Siya = BigDecimal.valueOf(1300000);
		asc.HScurr = BigDecimal.valueOf(7.106e9);
		asc.Frags = BigDecimal.valueOf(0);
		asc.Bhaal = BigDecimal.valueOf(0);
		asc.Jugg = BigDecimal.valueOf(0);
		asc.Lib = BigDecimal.valueOf(0);
		asc.Xyl = 5;
		asc.gilds = 352;
		asc.abonus = new ancBonus(asc);

		//1.103e268 / 9532 betty, idle
		assertEquals(1.103e268, Formulas.getDamage(15, 9532, asc).doubleValue() * 1.5, 1e265); //1.5 because gog
	}
	
	@Test
	public void testMidasDamage() {
		Tables.precalcHeroDamage();
		Tables.precalcExpAncients();
		AscSettings set = new AscSettings(BigDecimal.valueOf(1000));
		Ascension asc = new Ascension(set);
		asc.allocateHS();
		asc.Arg = BigDecimal.valueOf(1270000);
		asc.Morg = BigDecimal.valueOf(2e12);
		asc.Siya = BigDecimal.valueOf(1300000);
		asc.HScurr = BigDecimal.valueOf(7.106e9);
		asc.Xyl = 5;
		asc.gilds = 352;
		asc.abonus = new ancBonus(asc);
		//7.091e278 / 9673 midas, idle
		assertEquals(7.091e278, Formulas.getDamage(16, 9673, asc).doubleValue() * 1.5, 1e276); //1.5 because gog
	}
	
	@Test
	public void testActiveDamage() {
		Tables.precalcHeroDamage();
		Tables.precalcExpAncients();
		AscSettings set = new AscSettings(BigDecimal.valueOf(1000));
		Ascension asc = new Ascension(set);
		asc.allocateHS();
		asc.Arg = BigDecimal.valueOf(60000000);
		asc.Morg = BigDecimal.valueOf(4e15);
		asc.Siya = BigDecimal.valueOf(50000000);
		asc.Frags = BigDecimal.valueOf(60000000);
		asc.Bhaal = BigDecimal.valueOf(60000000);
		asc.Jugg = BigDecimal.valueOf(2000000);
		asc.HScurr = BigDecimal.valueOf(4.07e14);
		asc.gilds = 443;
		asc.abonus = new ancBonus(asc);

		//5.814e141 click / 4385 sam, bhaal bonus 9e8% - 9e6 times - 5.232e148, crit *18 - 9.418e149
		assertEquals(9.418e149, Formulas.getActiveDamage(1, 4385, asc).doubleValue(), 1e146);
	}
	
	@Test
	public void testMonsterGold() {
		Tables.precalcZoneHP();
		Tables.precalcExpAncients();
		AscSettings set = new AscSettings(BigDecimal.valueOf(1000));
		Ascension asc = new Ascension(set);
		asc.allocateHS();
		asc.Dora = 35;
		asc.Fortuna = 32;
		asc.Lib = BigDecimal.valueOf(1300000);
		asc.Mammon = BigDecimal.valueOf(1300000);
		asc.Mimzee = BigDecimal.valueOf(1300000);
		asc.Xyl = 5;
		asc.abonus = new ancBonus(asc);

		//6.384e146 * 4 / idle / 1911
		//+6.69% dora
		//+7.68% fortuna
		//*6.5e5 mimzee
		// (1 - 0.0769) + (0.0769 * 650000) = 49985
		// (1 - 0.0768) + (0.0768 * 10) = 1.6913
		// 6.384e146 * 4 * 49985 * 1.6913 = 2.1588e152
		assertEquals(2.1588e152, Formulas.getGold(1911, asc).doubleValue(), 1e150);
	}
	
	//@Test
	public void testMonsterGold2() {
		Tables.precalcZoneHP();
		Tables.precalcExpAncients();
		AscSettings set = new AscSettings(BigDecimal.valueOf(15500));
		Ascension asc = new Ascension(set);
		asc.allocateHS();
		asc.Xyl = 5;
		asc.abonus = new ancBonus(asc);

		//~9e61 from normal mob at 639
		assertEquals(9e61, Formulas.getGold(639, asc).doubleValue(), 1e59);
	}
	
	//@Test
	public void testZoneHP2() {
		Tables.precalcZoneHP();
		//~4.57e59 from normal mob at 639
		assertEquals(4.57e59, Formulas.getHp(639).doubleValue(), 1e58);
	}
	
	@Test
	public void testHeroSouls() {
		AscSettings set = new AscSettings(BigDecimal.valueOf(1000));
		Ascension asc = new Ascension(set);
		asc.Solomon = BigDecimal.valueOf(3500);
		asc.Pony = 26;
		asc.Phan = 8;
		asc.AS = 77;
		double power = 77. / 10000. * -1;
		asc.TP = 1 + 0.01 * (50 - 49 * Math.pow(Math.E, power));
		asc.HSlimit = BigDecimal.valueOf(1.31e14);
		Tables.precalcTransPower(asc.TP, asc.Phan);
		asc.abonus = new ancBonus(asc);
		// 1.0177425 TP% total
		
		//23011k / 2100
		assertEquals(2.3011e7, Formulas.getSouls(2100, asc).doubleValue(), 1e6); //weak
		//480B / 3600
		asc.Solomon = BigDecimal.valueOf(400000);
		asc.abonus = new ancBonus(asc);
		assertEquals(4.8e11, Formulas.getSouls(3600, asc).doubleValue(), 1e10); //weak
	}
	
	@Test
	public void testHeroSouls2() {
		AscSettings set = new AscSettings(BigDecimal.valueOf(1000));
		Ascension asc = new Ascension(set);
		asc.Solomon = BigDecimal.valueOf(150000);
		asc.Pony = 26;
		asc.Phan = 9;
		asc.AS = 86;
		double power = 86. / 10000. * -1;
		asc.TP = 1 + 0.01 * (50 - 49 * Math.pow(Math.E, power));
		asc.HSlimit = BigDecimal.valueOf(1.31e14);
		Tables.precalcTransPower(asc.TP, asc.Phan);
		asc.abonus = new ancBonus(asc);
		// 1.018676 TP% total
		
		//19452k / 900
		assertEquals(1.9452e7, Formulas.getSouls(900, asc).doubleValue(), 1e5);
		//27077k / 1000
		assertEquals(2.7077e7, Formulas.getSouls(1000, asc).doubleValue(), 1e5);
	}
	
	@Test
	public void testDoraEffect() {
		Tables.precalcDoraEffect();
		assertEquals(Formulas.getDoraEffect(17).doubleValue(), 0.033094, 1e-5);
	}
	
	@Test
	public void testKumaEffect() {
		Tables.precalcKumaEffect();
		assertEquals(Formulas.getKumaEffect(28).doubleValue(), 1.9537, 1e-3);
	}
	
	@Test
	public void testFortunaEffect() {
		Tables.precalcFortunaEffect();
		assertEquals(Formulas.getFortunaEffect(18).doubleValue(), 0.044, 1e-3);
	}
	
	@Test
	public void testAtmanEffect() {
		assertEquals(Formulas.getAtmanEffect(33).doubleValue(), 26.14, 1);
	}
	
	@Test
	public void testArgaivEffect() {
		assertEquals(2.54e4, Formulas.getArgaivEffect(BigDecimal.valueOf(1270000)).doubleValue(), 1);
	}
	
	@Test
	public void testBubosEffect() {
		assertEquals(0.2363, Formulas.getBubosEffect(32).doubleValue(), 1e-4);
	}
	
	@Test
	public void testskillsEffect() {
		assertEquals(64, Formulas.getskillsEffect(32), 1e-4);
	}
	
	@Test
	public void testSiyaEffect() {
		assertEquals(1.95e5, Formulas.getSiyaEffect(BigDecimal.valueOf(1300000)).doubleValue(), 1e1);
	}
	
	@Test
	public void testLibEffect() {
		assertEquals(1.95e5, Formulas.getLibEffect(BigDecimal.valueOf(1300000)).doubleValue(), 1e4);
	}
	
	@Test
	public void testLibEffectLow() {
		assertEquals(15.33, Formulas.getLibEffect(BigDecimal.valueOf(70)).doubleValue(), 0.01);
	}
	
	@Test
	public void testSolomonEffect() {
		assertEquals(1.5e3, Formulas.getSolomonEffect(BigDecimal.valueOf(150000)).doubleValue(), 1e1);
	}
	
	@Test
	public void testMammonEffect() {
		assertEquals(6.5e4, Formulas.getMammonEffect(BigDecimal.valueOf(1300000)).doubleValue(), 1e2);
	}
	
	@Test
	public void testMimzeeEffect() {
		assertEquals(6.5e5, Formulas.getMimzeeEffect(BigDecimal.valueOf(1300000)).doubleValue(), 1e3);
	}

	@Test
	public void testBhaalEffect() {
		assertEquals(1.5e5, Formulas.getBhaalEffect(BigDecimal.valueOf(1000000)).doubleValue(), 1e1);
	}
	
	@Test
	public void testFragsEffect() {
		assertEquals(2e5, Formulas.getFragsEffect(BigDecimal.valueOf(1000000)).doubleValue(), 1e1);
	}
	
	@Test
	public void testJuggEffect() {
		assertEquals(6, Formulas.getJuggEffect(BigDecimal.valueOf(60000)).doubleValue(), 1e-4);
	}
	
	@Test
	public void testChronosEffect() {
		assertEquals(19.89, Formulas.getChronosEffect(32).doubleValue(), 1e1);
	}
	
	@Test
	public void testRevolcEffect() {
		assertEquals(0.2738, Formulas.getRevolcEffect(32).doubleValue(), 1e1);
	}
	
	@Test
	public void testVaagurEffect() {
		assertEquals(0.4236, Formulas.getVaagurEffect(32).doubleValue(), 1e1);
	}
	
	@Test
	public void testDogcogEffect() {
		Tables.precalcDogcogEffect();
		assertEquals(0.2711, Formulas.getDogcogEffect(32).doubleValue(), 1e-4);
	}
}
