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
package de.jevopi.jetex.tex.lexer;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.StringJoiner;

import de.jevopi.jetex.tex.tokens.Token;

public abstract class AbstractInputProcessorTest {

	protected void assertTokens(String expectedTokenString, Iterator<Token> iter) {
		assertEquals(expectedTokenString, tokenString(iter));
	}

	String tokenString(Iterator<Token> iter) {
		final StringJoiner joiner = new StringJoiner(",");
		iter.forEachRemaining(t -> {
			joiner.add(t.toString());
		});
		return joiner.toString();
	}

}
