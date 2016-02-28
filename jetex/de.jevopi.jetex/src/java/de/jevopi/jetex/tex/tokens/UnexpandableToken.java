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

/**
 * Proxy for temporarily disabling expansion.
 */
public class UnexpandableToken extends Token {

	final Token token;

	public UnexpandableToken(Token token) {
		this.token = token;
	}

	@Override
	public boolean isExpandable() {
		return false;
	}

	@Override
	public Category getCategory() {
		return token.getCategory();
	}

	@Override
	public String rawValue() {
		return token.rawValue();
	}

}
