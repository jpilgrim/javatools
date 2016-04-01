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

import java.util.Iterator;
import java.util.NoSuchElementException;

import de.jevopi.jetex.tex.Category;

/**
 * Base class for tokens, basically a pair of {@link Category} and concrete (raw) value.
 */
public abstract class Token {

	public abstract Category getCategory();
	public abstract TokenType getTokenType();

	public int characterCode() {
		return -1;
	}

	public abstract String rawValue();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Token) {
			Token other = (Token) obj;
			return getCategory() == other.getCategory() && rawValue().equals(other.rawValue());
		}
		return false;
	}

	/**
	 * Returns true if token has same category as other, and if it has the same
	 * raw value in case of letter or other.
	 */
	public boolean similar(Token other) {
		Category cat = getCategory();
		if (cat == other.getCategory()) {
			if (cat == Category.LETTER || cat == Category.OTHER) {
				return rawValue().equals(other.rawValue());
			}
			return true;
		}
		return false;
	}

	/**
	 * Returns singleton iterator.
	 */
	public Iterator<Token> iterator() {
		return new Iterator<Token>() {

			private boolean next = true;

			@Override
			public boolean hasNext() {
				return next;
			}

			@Override
			public Token next() {
				if (next) {
					next = false;
					return Token.this;
				}
				throw new NoSuchElementException();
			}

		};

	}

	/**
	 * Returns true if token is expandable. This base implementation always return false, subclasses 
	 * have to override it accordingly.
	 */
	public boolean isExpandable() {
		return false;
	}
	
	/**
	 * Returns null for non-macros.
	 */
	public String getSequence() {
		return null;
	}
}
