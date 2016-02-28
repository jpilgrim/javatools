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

public abstract class AbstractVisualProcessor implements Iterator<String> {

	final Iterator<Token> tokens;

	String next;

	public AbstractVisualProcessor(Iterator<Token> tokens) {
		this.tokens = tokens;
		fetchNext();
	}

	private void fetchNext() {
		next = null;
		while (tokens.hasNext()) {
			Token token = tokens.next();
			switch (token.getCategory()) {// @formatter:off
			case ACTIVE: next = nbsp(token); break;
			case EOL: next = eol(token); break;
			case COMMENT: next = comment(token); break;
			case LETTER: next = letter(token) ; break;
			case OTHER: next = other(token); break;
			case SPACE: next = space(token); break;
			default: // do nothing;
			} // @formatter:on
			if (next != null) {
				break;
			}
		}
	}

	protected abstract String space(Token token);

	protected abstract String other(Token token);

	protected abstract String letter(Token token);

	protected abstract String comment(Token token);

	protected abstract String eol(Token token);

	protected abstract String nbsp(Token token);

	@Override
	public boolean hasNext() {
		return next != null;
	}

	@Override
	public String next() {
		String s = next;
		fetchNext();
		return s;
	}

}
