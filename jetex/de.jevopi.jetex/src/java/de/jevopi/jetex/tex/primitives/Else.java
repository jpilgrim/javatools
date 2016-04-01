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
import java.util.Optional;

import de.jevopi.jetex.ConditionResult;
import de.jevopi.jetex.ProcessorState;
import de.jevopi.jetex.tex.expansion.ExpansionError;
import de.jevopi.jetex.tex.tokens.ITokenIterator;
import de.jevopi.jetex.tex.tokens.Token;

/**
 * See {@link If}.
 */
public class Else extends PrimitiveCommand {

	@Override
	public Iterator<Token> expand(ProcessorState state, ITokenIterator tokens) {
		Optional<ConditionResult> optCr = state.peekConditionResult();
		if (! optCr.isPresent()) {
			throw new ExpansionError(tokens.getLocation(), "Found \\else without preceeding \\if");
		}
		if (! optCr.get().selectElse()) {
			state.popConditionResult();
			skipToFi(state, tokens);
		}
		return null; // else itself is not expanded
	}

	private void skipToFi(ProcessorState state, ITokenIterator tokens) {
		state.inhibitExpansion();
		try {
			while (tokens.hasNext()) {
				Token token = tokens.next();
				if ("fi".equals(token.getSequence())) {
					return;
				}
			}
			throw new ExpansionError(tokens.getLocation(), "Incomplete \\else, all code was ignored.");
		} finally {
			state.allowExpension();
		}
	}

}
