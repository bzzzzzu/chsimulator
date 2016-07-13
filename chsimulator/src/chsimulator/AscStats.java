package chsimulator;

import java.math.BigDecimal;

public class AscStats {
	int zone = 1;
	int hero = 1;
	int level = 1;
	
	BigDecimal gold = BigDecimal.valueOf(1);
	
	long time = 0;
	
	BigDecimal souls = BigDecimal.valueOf(0);
	BigDecimal soulshr = BigDecimal.valueOf(0);
	
	public void copyStat(AscStats stat) {
		this.zone = stat.zone;
		this.hero = stat.hero;
		this.level = stat.level;
		this.gold = stat.gold;
		this.time = stat.time;
		this.souls = stat.souls;
		this.soulshr = stat.soulshr;
	}
}
