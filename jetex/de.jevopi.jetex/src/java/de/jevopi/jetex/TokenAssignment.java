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
package de.jevopi.jetex;

import java.util.Iterator;

import de.jevopi.jetex.tex.tokens.ITokenIterator;
import de.jevopi.jetex.tex.tokens.Token;

/**
 * \let assignment
 */
public class TokenAssignment extends Command {

	public final String name;
	public final Token token;

	public TokenAssignment(String name, Token token) {
		this.name = name;
		this.token =token;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Iterator<Token> expand(ProcessorState state, ITokenIterator tokens) {
		return token.iterator();
	}

}
