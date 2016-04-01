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

import de.jevopi.jetex.tex.TexLocation;

public interface ITokenIterator extends Iterator<Token>{

	/**
	 * Similar to peek(0), peeks the next token in the input stream.
	 * Peeking never expands the token stream!
	 */
	public abstract Token peek();

	/**
	 * Returns the n-th token ahead without removing it, 0 is the next token.
	 * Peeking never expands the token stream!
	 */
	public abstract Token peek(int lookAhead);

	public abstract TexLocation getLocation();

}