package chsimulator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class TransStats {
	double AS;
	double ASday;
	double ASdayadjusted;
	double initialAS;
	double HS;
	
	long time;
	long zone;
	int ascensions;
	
	public TransStats(double AS, double ASday, double ASdayadjusted, long time, double initialAS, int ascensions, long zone, double HS) {
		this.AS = AS;
		this.ASday = ASday;
		this.ASdayadjusted = ASdayadjusted;
		this.time = time;
		this.initialAS = initialAS;
		this.ascensions = ascensions;
		this.zone = zone;
		this.HS = HS;
	}
	
	public void copyStat(TransStats stat) {
		this.AS = stat.AS;
		this.ASday = stat.ASday;
		this.ASdayadjusted = stat.ASdayadjusted;
		this.time = stat.time;
		this.initialAS = stat.initialAS;
		this.ascensions = stat.ascensions;
		this.zone = stat.zone;
		this.HS = stat.HS;
	}
	
	public double ASformat() {
		return BigDecimal.valueOf(AS).round(new MathContext(4, RoundingMode.HALF_DOWN)).doubleValue();
	}
	
	public double ASdiffformat() {
		return BigDecimal.valueOf(AS - initialAS).round(new MathContext(4, RoundingMode.HALF_DOWN)).doubleValue();
	}
	
	public double ASdayformat() {
		return BigDecimal.valueOf(ASday).round(new MathContext(4, RoundingMode.HALF_DOWN)).doubleValue();
	}
	
	public double ASdayadjustedformat() {
		return BigDecimal.valueOf(ASdayadjusted).round(new MathContext(4, RoundingMode.HALF_DOWN)).doubleValue();
	}
}
