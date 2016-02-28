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
package de.jevopi.jetex.tex.expansion;

import java.util.Iterator;
import java.util.List;

import de.jevopi.jetex.tex.tokens.ExpandableTokenIterator;
import de.jevopi.jetex.tex.tokens.ParameterToken;
import de.jevopi.jetex.tex.tokens.Token;

public class MacroExpansion extends ExpandableTokenIterator {

	private final List<? extends Iterable<Token>> arguments;

	public MacroExpansion(Iterator<Token> replacement, 
			List<? extends Iterable<Token>> arguments) {
		this.arguments = arguments;
		add(replacement);
	}
	
	@Override
	public Token next() {
		Token token = super.next();
		if (token instanceof ParameterToken) {
			int index = ((ParameterToken) token).getIndex();
			Iterable<Token> arg = arguments.get(index-1);
			add(arg.iterator());
			return next();
		}
		return token;
	}

}
