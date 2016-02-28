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
import de.jevopi.jetex.tex.Category;
import de.jevopi.jetex.tex.expansion.ExpansionError;
import de.jevopi.jetex.tex.tokens.ControlSequence;
import de.jevopi.jetex.tex.tokens.IExpandableTokenIterator;
import de.jevopi.jetex.tex.tokens.ITokenIterator;
import de.jevopi.jetex.tex.tokens.Token;

/**
 * <pre>
 * \let«cseq»= «token»
 * </pre>
 */
public class Let extends PrimitiveCommand {

	@Override
	public void execute(ProcessorState state, IExpandableTokenIterator tokens) {
		state.inhibitExpansion();
		String name = getCSName(tokens);
		consumeEquals(tokens);
		consumeOptionalSpace(tokens);
		Token token = tokens.next();
		state.allowExpension();
		TokenAssignment tokenAssignment = new TokenAssignment(name, token);
		state.addTokenAssignment(tokenAssignment);
	}

	private void consumeEquals(ITokenIterator tokens) {
		Token t = tokens.next();
		if ("=".equals(t.rawValue())){
			return;
		}
		throw new ExpansionError(tokens.getLocation(), "Didn't found equals character");
	}

	private void consumeOptionalSpace(ITokenIterator tokens) {
		if (tokens.peek().getCategory()==Category.SPACE) {
			tokens.next();
		}
	}
	
	

	/**
	 * @param exp
	 * @return
	 */
	private String getCSName(ITokenIterator tokens) {
		Token t = tokens.next();
		if (t instanceof ControlSequence) {
			return ((ControlSequence) t).getSequence();
		}
		throw new ExpansionError(tokens.getLocation(), "Expected Control Sequence, was " + t);
	}

}
