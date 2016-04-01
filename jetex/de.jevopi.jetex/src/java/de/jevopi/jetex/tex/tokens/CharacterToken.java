/**
 * Copyright (c) 2016 Jens von Pilgrim
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *   Jens von Pilgrim - Initial API and implementation
 */
package de.jevopi.jetex.tex.tokens;

import de.jevopi.jetex.tex.Category;

public class CharacterToken extends Token {
	final char character;
	final Category category;
	
	public CharacterToken(char character, Category category) {
		this.character = character;
		this.category = category;
	}
	
	@Override
	public Category getCategory() {
		return category;
	}
	
	@Override
	public TokenType getTokenType() {
		return TokenType.Character;
	}
	
	@Override
	public int characterCode() {
		return character;
	}

	
	@Override
	public String rawValue() {
		return String.valueOf(character);
	}
	
	@Override
	public String toString() {
		return category.shortID+"("+character+")";
	}
}
