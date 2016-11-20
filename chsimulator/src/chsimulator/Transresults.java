package chsimulator;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Transresults {
	long time = 0;
	long zone = 0;
	int ascensions = 0;
	
	double ASgain = 0;
	double ASgainday = 0;
	
	long timehuman = 0;
	long zonehuman = 0;
	int ascensionshuman = 0;
	
	double ASgainhuman = 0;
	double ASgaindayhuman = 0;
	
	double AS = 0;
	double Xyl = 0;
	double Chor = 0;
	double Phan = 0;
	double Borb = 0;
	double Pony = 0;
	
	double HS = 0;
	double HShuman = 0;

	public void copyResult(Transresults result) {
		this.time = result.time;
		this.ASgain = result.ASgain;
		this.ASgainday = result.ASgainday;
		this.timehuman = result.timehuman;
		this.ASgainhuman = result.ASgainhuman;
		this.ASgaindayhuman = result.ASgaindayhuman;
		this.ascensions = result.ascensions;
		this.ascensionshuman = result.ascensionshuman;
		this.zone = result.zone;
		this.zonehuman = result.zonehuman;
		this.AS = result.AS;
		this.Xyl = result.Xyl;
		this.Chor = result.Chor;
		this.Phan = result.Phan;
		this.Borb = result.Borb;
		this.Pony = result.Pony;
		this.HS = result.HS;
		this.HShuman = result.HShuman;
	}
	
	public void print() {
		System.out.println("Transcension final resuls (optimal) - AS: " + ASgain + ", time: " + (time / 1000 / 3600) + "h, AS/day: " + ASgainday + ", Ascensions: " + ascensions + ", HZE: " + zone);
		System.out.println("Transcension final resuls (human) - AS: " + ASgainhuman + ", time: " + (timehuman / 1000 / 3600) + "h, AS/day: " + ASgaindayhuman + ", Ascensions: " + ascensionshuman + ", HZE: " + zonehuman);
		System.out.println("Build - AS: " + AS + ", Xyl: " + Xyl + ", Chor: " + Chor + ", Phan: " + Phan + ", Borb: " + Borb + ", Pony: " + Pony + ", Total spent: " + (Xyl + ((Math.floor(Chor / 10) + 1) * (Math.floor(Chor / 10) * 5 + (Chor % 10))) + Phan * (Phan + 1) / 2 + Borb + Pony));
	}

	public void write(int i) {
		// Results output from double-optimizator (1) and actual-build-optimizator (2)
		String path = "res.txt";
		String path2 = "resint.txt";
		try {
			double timeh = (double) (int) ((double) timehuman / 3600) / 1000;
			if (i == 1) {
				String data = (int) AS
					+ "\t" + BigDecimal.valueOf(Xyl).round(new MathContext(5, RoundingMode.HALF_DOWN)).doubleValue()
					+ "\t" + BigDecimal.valueOf(Chor).round(new MathContext(5, RoundingMode.HALF_DOWN)).doubleValue()
					+ "\t" + BigDecimal.valueOf(Phan).round(new MathContext(5, RoundingMode.HALF_DOWN)).doubleValue()
					+ "\t" + BigDecimal.valueOf(Borb).round(new MathContext(5, RoundingMode.HALF_DOWN)).doubleValue()
					+ "\t" + BigDecimal.valueOf(Pony).round(new MathContext(5, RoundingMode.HALF_DOWN)).doubleValue()
					+ "\t" + BigDecimal.valueOf(ASgaindayhuman).round(new MathContext(5, RoundingMode.HALF_DOWN)).doubleValue()
					+ "\t" + BigDecimal.valueOf(timeh).round(new MathContext(5, RoundingMode.HALF_DOWN)).doubleValue()
					+ "\t" + BigDecimal.valueOf(HShuman).round(new MathContext(5, RoundingMode.HALF_DOWN)).doubleValue()
					+ "\t" + BigDecimal.valueOf(ASgainhuman).round(new MathContext(5, RoundingMode.HALF_DOWN)).doubleValue()
					+ "\t" + zonehuman
					+ "\t" + ascensionshuman
					+ "\n"
					;
				Files.write(Paths.get(path), data.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
			}
			if (i == 2) {
				String dataint = (int) AS
					+ "\t" + BigDecimal.valueOf(Xyl).round(new MathContext(5, RoundingMode.HALF_DOWN)).longValue()
					+ "\t" + BigDecimal.valueOf(Chor).round(new MathContext(5, RoundingMode.HALF_DOWN)).longValue()
					+ "\t" + BigDecimal.valueOf(Phan).round(new MathContext(5, RoundingMode.HALF_DOWN)).longValue()
					+ "\t" + BigDecimal.valueOf(Borb).round(new MathContext(5, RoundingMode.HALF_DOWN)).longValue()
					+ "\t" + BigDecimal.valueOf(Pony).round(new MathContext(5, RoundingMode.HALF_DOWN)).longValue()
					+ "\t" + BigDecimal.valueOf(ASgaindayhuman).round(new MathContext(5, RoundingMode.HALF_DOWN)).doubleValue()
					+ "\t" + BigDecimal.valueOf(timeh).round(new MathContext(5, RoundingMode.HALF_DOWN)).doubleValue()
					+ "\t" + BigDecimal.valueOf(HShuman).round(new MathContext(5, RoundingMode.HALF_DOWN)).doubleValue()
					+ "\t" + BigDecimal.valueOf(ASgainhuman).round(new MathContext(5, RoundingMode.HALF_DOWN)).doubleValue()
					+ "\t" + zonehuman
					+ "\t" + ascensionshuman
					+ "\n"
					;
				Files.write(Paths.get(path2), dataint.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
