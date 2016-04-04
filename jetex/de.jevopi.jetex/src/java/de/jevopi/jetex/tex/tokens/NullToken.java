package de.jevopi.jetex.tex.tokens;

import de.jevopi.jetex.tex.Category;

public class NullToken extends Token {
	
	public final static NullToken NULL_TOKEN = new NullToken();

	@Override
	public Category getCategory() {
		return Category.IGNORE;
	}

	@Override
	public TokenType getTokenType() {
		return TokenType.Character;
	}

	@Override
	public String rawValue() {
		return "";
	}

}
