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

public class ParToken extends Token {
	public final static ParToken INSTANCE = new ParToken();
	
	@Override
	public Category getCategory() {
		return Category.EOL;
	}
	
	@Override
	public TokenType getTokenType() {
		return TokenType.Character;
	}

	@Override
	public String rawValue() {
		return "\n\n";
	}
	
	@Override
	public String toString() {
		return "\\par";
	}
	
	@Override
	public String getSequence() {
		return "par";
	}
	
	@Override
	public boolean isExpandable() {
		return true;
	}
}
