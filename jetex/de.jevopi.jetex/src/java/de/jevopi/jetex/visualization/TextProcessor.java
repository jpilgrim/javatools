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
package de.jevopi.jetex.visualization;

import java.util.Iterator;

import de.jevopi.jetex.tex.tokens.Token;

public class TextProcessor extends AbstractVisualProcessor {

	public TextProcessor(Iterator<Token> tokens) {
		super(tokens);
	}

	@Override
	protected String space(Token token) {
		return " ";
	}

	@Override
	protected String other(Token token) {
		return token.rawValue();
	}

	@Override
	protected String letter(Token token) {
		return token.rawValue();
	}

	@Override
	protected String comment(Token token) {
		return null;
	}

	@Override
	protected String eol(Token token) {
		return "\n";
	}

	@Override
	protected String nbsp(Token token) {
		return " ";
	}

}
