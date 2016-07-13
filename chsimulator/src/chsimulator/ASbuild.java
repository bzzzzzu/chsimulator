package chsimulator;

public class ASbuild {
	double Xyl = 0;
	double Chor = 0;
	double Phan = 0;
	double Borb = 0;
	double Pony = 0;
	
	public ASbuild() {
	}
	
	public ASbuild(double Xyl, double Chor, double Phan, double Borb, double Pony) {
		this.Xyl = Xyl;
		this.Chor = Chor;
		this.Phan = Phan;
		this.Borb = Borb;
		this.Pony = Pony;
	}
	
	public void print() {
		System.out.println("Xyl: " + Xyl + ", Chor: " + Transcension.solveChor(Chor) + ", Phan: " + Transcension.solvePhan(Phan) + ", Borb: " + Borb + ", Pony: " + Pony);
	}
	
	public void copyBuild(ASbuild build) {
		this.Xyl = build.Xyl;
		this.Chor = build.Chor;
		this.Phan = build.Phan;
		this.Borb = build.Borb;
		this.Pony = build.Pony;
	}
	
	public ASbuild raiseOutsider(int out, double move) {
		// Used in doubles optimizer, probably pretty bad
		// Tries to raise every outsider spent AS at the expense of all others
		ASbuild newbuild = this;
		double lost = 0;
		if (newbuild.Xyl > move) { newbuild.Xyl -= move; lost += move; }
		else { lost += newbuild.Xyl; newbuild.Xyl = 0; }
		if (newbuild.Chor > move) { newbuild.Chor -= move; lost += move; }
		else { lost += newbuild.Chor; newbuild.Chor = 0; }
		if (newbuild.Phan > move) { newbuild.Phan -= move; lost += move; }
		else { lost += newbuild.Phan; newbuild.Phan = 0; }
		if (newbuild.Borb > move) { newbuild.Borb -= move; lost += move; }
		else { lost += newbuild.Borb; newbuild.Borb = 0; }
		if (newbuild.Pony > move) { newbuild.Pony -= move; lost += move; }
		else { lost += newbuild.Pony; newbuild.Pony = 0; }

		if (out == 1) { newbuild.Xyl += lost; }
		if (out == 2) { newbuild.Chor += lost; }
		if (out == 3) { newbuild.Phan += lost; }
		if (out == 4) { newbuild.Borb += lost; }
		if (out == 5) { newbuild.Pony += lost; }
		return newbuild;
	}
	
	public ASbuild moveOutsiderInt(int out1, int out2, double mov) {
		// Used in actual build optimizer
		// Tries to raise/lower outsider level at the expense of other outsider
		// Phan/Chor move is not supported
		ASbuild newbuild = this;
		for (int i = 0; i < (int) mov; i++) {
			double move = 1;
			double lost = 0;
			double movechor = move;
			double movephan = move;
			if (out1 == 2) { move = Math.ceil((newbuild.Chor - 1) / 10); }
			if (out2 == 2) { move = Math.ceil((newbuild.Chor + 1) / 10); }
			if (out1 == 3) { move = newbuild.Phan - 1; }
			if (out2 == 3) { move = newbuild.Phan + 1; }
			if (move < 1) { move = 1; }
			if (out1 == 1) {
				if (newbuild.Xyl >= move) { newbuild.Xyl -= move; lost += move; }
			}
			if (out1 == 2) {
				if (newbuild.Chor >= move) { newbuild.Chor -= movechor; lost += move; }
			}
			if (out1 == 3) {
				if (newbuild.Phan >= move) { newbuild.Phan -= movephan; lost += move; }
			}
			if (out1 == 4) {
				if (newbuild.Borb >= move) { newbuild.Borb -= move; lost += move; }
			}
			if (out1 == 5) {
				if (newbuild.Pony >= move) { newbuild.Pony -= move; lost += move; }
			}
		
			if (out2 == 1) { newbuild.Xyl += lost; }
			if ((out2 == 2) && (lost == move)) { newbuild.Chor += movechor; }
			if ((out2 == 3) && (lost == move)) { newbuild.Phan += movephan; }
			if (out2 == 4) { newbuild.Borb += lost; }
			if (out2 == 5) { newbuild.Pony += lost; }
		}
		return newbuild;
	}
}
