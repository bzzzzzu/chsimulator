package chsimulator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class ancBonus {
	BigDecimal argBonus = BigDecimal.valueOf(0);
	BigDecimal mimzeeBonus = BigDecimal.valueOf(0);
	BigDecimal mammonBonus = BigDecimal.valueOf(0);
	BigDecimal siyaBonus = BigDecimal.valueOf(0);
	BigDecimal libBonus = BigDecimal.valueOf(0);
	BigDecimal solomonBonus = BigDecimal.valueOf(0);
	BigDecimal fragsBonus = BigDecimal.valueOf(0);
	BigDecimal bhaalBonus = BigDecimal.valueOf(0);
	BigDecimal juggBonus = BigDecimal.valueOf(0);
	BigDecimal idleBonusDamage = BigDecimal.valueOf(0);
	BigDecimal idleBonusGold = BigDecimal.valueOf(0);
	BigDecimal bonusGold = BigDecimal.valueOf(0);
	BigDecimal atmanMult = BigDecimal.valueOf(0);
	BigDecimal ponyBonus = BigDecimal.valueOf(0);
	double ponyDouble = 0;
	BigDecimal borbLimit = BigDecimal.valueOf(0);
	BigDecimal mobsKuma = BigDecimal.valueOf(0);
	BigDecimal activeBonusDamage = BigDecimal.valueOf(0);
	
	public ancBonus(Ascension a) {
		// Optimization: store all ancient effects which are static per ascension here
		argBonus = Formulas.getArgaivEffect(a.Arg);
		mimzeeBonus = Formulas.getMimzeeEffect(a.Mimzee);
		mammonBonus = Formulas.getMammonEffect(a.Mammon);
		siyaBonus = Formulas.getSiyaEffect(a.Siya);
		libBonus = Formulas.getLibEffect(a.Lib);
		solomonBonus = Formulas.getSolomonEffect(a.Solomon);
		fragsBonus = Formulas.getFragsEffect(a.Frags);
		bhaalBonus = Formulas.getBhaalEffect(a.Bhaal);
		juggBonus = Formulas.getJuggEffect(a.Jugg);
		
		idleBonusDamage = siyaBonus;
		idleBonusDamage = idleBonusDamage.multiply(BigDecimal.valueOf(1 + a.Xyl));
		
		BigDecimal gildbonus = argBonus.add(BigDecimal.valueOf(0.5));
		gildbonus = gildbonus.multiply(BigDecimal.valueOf(a.gilds));
		gildbonus = gildbonus.add(BigDecimal.valueOf(1));
		idleBonusDamage = idleBonusDamage.multiply(gildbonus);
		
		BigDecimal soulbonus = a.HScurr.multiply(BigDecimal.valueOf(0.1));
		BigDecimal morgbonus = a.Morg.multiply(BigDecimal.valueOf(0.11));
		BigDecimal totalbonus = soulbonus.add(morgbonus);
		idleBonusDamage = idleBonusDamage.multiply(totalbonus);
		
		double heroesdamage = 2 * //double damage
				1.25 * //fisherman
				2.0736 * //betty
				1.25 * //leon
				1.25 * //broyle
				1.44 * //amenhotep
				1.1 * //beastlord
				1.1 * //shinatobe
				1.5625 * //grant
				1.25 * //frostleaf
				1.1; //dark ritual
		idleBonusDamage = idleBonusDamage.multiply(BigDecimal.valueOf(heroesdamage));
		idleBonusDamage = idleBonusDamage.round(new MathContext(10, RoundingMode.HALF_DOWN));
		
		idleBonusGold = libBonus;
		idleBonusGold = idleBonusGold.multiply(BigDecimal.valueOf(1 + a.Xyl));
		idleBonusGold = idleBonusGold.round(new MathContext(10, RoundingMode.HALF_DOWN));
		
		// Chests are averaging
		bonusGold = mimzeeBonus.add(BigDecimal.valueOf(10));
		double dora = Formulas.getDoraEffect(a.Dora).doubleValue() + 0.01;
		bonusGold = bonusGold.multiply(BigDecimal.valueOf(dora).setScale(4, RoundingMode.HALF_DOWN));
		double dorano = 0.99 - Formulas.getDoraEffect(a.Dora).doubleValue();
		bonusGold = bonusGold.add(BigDecimal.valueOf(dorano).setScale(4, RoundingMode.HALF_DOWN));
		
		// Fortuna, Midas and gold formula (20% from HP at high levels)
		double fort = Formulas.getFortunaEffect(a.Fortuna).doubleValue();
		fort = fort * 10 + (1 - fort);
		double heroesgold = 1.25 * 1.25 * 1.25 * 1.5 / 5;
		fort = fort * heroesgold;
		bonusGold = bonusGold.multiply(BigDecimal.valueOf(fort));
		
		bonusGold = bonusGold.multiply(mammonBonus.add(BigDecimal.valueOf(1)));
		
		// To increase performance, Dogcog is calculated as an gold increase ancient
		double dogbonus = 1 / (1. - Formulas.getDogcogEffect(a.Dogcog).doubleValue());
		bonusGold = bonusGold.multiply(BigDecimal.valueOf(dogbonus));
		bonusGold = bonusGold.round(new MathContext(10, RoundingMode.HALF_DOWN));
		
		ponyBonus = solomonBonus.multiply(BigDecimal.valueOf((a.Pony + 1)));
		ponyBonus = ponyBonus.add(BigDecimal.valueOf(1));
		ponyDouble = ponyBonus.doubleValue();
		
		atmanMult = Formulas.getAtmanEffect(a.Atman).add(BigDecimal.valueOf(25));
		atmanMult = atmanMult.divide(BigDecimal.valueOf(100), new MathContext(10, RoundingMode.HALF_DOWN));
		
		borbLimit = a.HSlimit.multiply(BigDecimal.valueOf((1 + a.Borb * 0.1)));
		borbLimit = borbLimit.round(new MathContext(10, RoundingMode.HALF_DOWN));
		
		mobsKuma = BigDecimal.valueOf(10).subtract(Formulas.getKumaEffect(a.Kuma));
		mobsKuma = mobsKuma.subtract(BigDecimal.valueOf(1));
		mobsKuma = mobsKuma.multiply(BigDecimal.valueOf(500));
		
		activeBonusDamage = gildbonus.multiply(BigDecimal.valueOf(heroesdamage));
		activeBonusDamage = activeBonusDamage.multiply(totalbonus);
		activeBonusDamage = activeBonusDamage.multiply(fragsBonus);
		activeBonusDamage = activeBonusDamage.multiply(BigDecimal.valueOf(0.035 * 18));
		activeBonusDamage = activeBonusDamage.multiply(bhaalBonus);
		activeBonusDamage = activeBonusDamage.round(new MathContext(10, RoundingMode.HALF_DOWN));
	}
}
