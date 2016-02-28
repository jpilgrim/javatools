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
package de.jevopi.jetex.tex.primitives;

import java.util.Iterator;

import de.jevopi.jetex.ProcessorState;
import de.jevopi.jetex.tex.Category;
import de.jevopi.jetex.tex.tokens.CharacterToken;
import de.jevopi.jetex.tex.tokens.ITokenIterator;
import de.jevopi.jetex.tex.tokens.Token;

public class SpaceCommand extends PrimitiveCommand {
	
	/**
	 * TODO check for different solutions
	 */
	private final static CharacterToken SPACE_TOKEN = new CharacterToken(' ', Category.SPACE);
	
	@Override
	public String getName() {
		return " ";
	}

	@Override
	public Iterator<Token> expand(ProcessorState state, ITokenIterator tokens) {
		return SPACE_TOKEN.iterator();
	}
}
