package de.jevopi.jetex.tex;

public enum Mode {
	horizontal(0),
	restrictedHorizontal(0),
	vertical(1),
	internalVertical(1),
	math(2),
	displayMath(2);
	
	final int category;

	private Mode(int category) {
		this.category = category;
	}
	
	public boolean isHorizontal() {
		return category == 0;
	}
	public boolean isVertical() {
		return category == 1;
	}
	public boolean isMath() {
		return category == 2;
	}
	
	
}
