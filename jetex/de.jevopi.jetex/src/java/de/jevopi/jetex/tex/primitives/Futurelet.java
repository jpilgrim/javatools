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

import de.jevopi.jetex.ProcessorState;
import de.jevopi.jetex.TokenAssignment;
import de.jevopi.jetex.tex.expansion.ExpansionError;
import de.jevopi.jetex.tex.tokens.ControlSequence;
import de.jevopi.jetex.tex.tokens.IExpandableTokenIterator;
import de.jevopi.jetex.tex.tokens.ITokenIterator;
import de.jevopi.jetex.tex.tokens.Token;

/**
 * <pre>
 * \futurelet«cseq»«token1»«token2»
 * </pre>
 */
public class Futurelet extends PrimitiveCommand {

	
	@Override
	public void execute(ProcessorState state, IExpandableTokenIterator tokens) {
		state.inhibitExpansion();
		String name = getCSName(tokens);
		Token token2 = tokens.peek(1);
		state.allowExpension();
		TokenAssignment tokenAssignment = new TokenAssignment(name, token2);
		state.addTokenAssignment(tokenAssignment);
	}

	private String getCSName(ITokenIterator tokens) {
		Token t = tokens.next();
		if (t instanceof ControlSequence) {
			return ((ControlSequence) t).getSequence();
		}
		throw new ExpansionError(tokens.getLocation(), "Expected Control Sequence, was " + t);
	}

}
