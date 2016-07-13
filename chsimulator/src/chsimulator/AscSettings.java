package chsimulator;

import java.math.BigDecimal;

public class AscSettings {
	int AS = 0;
	double TP = 1.01;
	
	BigDecimal currentHS;
	BigDecimal HSlimit;
	
	double Xyl = 0;
	double Chor = 0;
	double Phan = 0;
	double Borb = 0;
	double Pony = 0;
	
	int strat = 0;
	
	int gilds = 10;
	
	long hze = 0;
	
	public AscSettings(BigDecimal currentHS, int AS, double TP, BigDecimal HSlimit, double Xyl, double Chor, double Phan, double Borb, double Pony, int gilds, int strat, long hze) {
		this.currentHS = currentHS;
		this.AS = AS;
		this.TP = TP;
		this.HSlimit = HSlimit;
		this.Xyl = Xyl;
		this.Chor = Chor;
		this.Phan = Phan;
		this.Borb = Borb;
		this.Pony = Pony;
		this.gilds = gilds;
		this.strat = strat;
		this.hze = hze;
	}
	
	public AscSettings(BigDecimal currentHS) {
		this.currentHS = currentHS;
		this.AS = 0;
		this.TP = 1.01;
		this.HSlimit = BigDecimal.valueOf(0);
		this.Xyl = 0;
		this.Chor = 0;
		this.Phan = 0;
		this.Borb = 0;
		this.Pony = 0;
		this.gilds = 10;
		this.strat = 0;
		this.hze = 100;
	}
}
