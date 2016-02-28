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
import de.jevopi.jetex.tex.tokens.IExpandableTokenIterator;
import de.jevopi.jetex.tex.tokens.Token;
import de.jevopi.jetex.tex.tokens.TokenIterators;

/**
 * <pre>
 * \let«cseq»= «token»
 * </pre>
 */
public class Let extends PrimitiveCommand {

	@Override
	public void execute(ProcessorState state, IExpandableTokenIterator tokens) {
		state.inhibitExpansion();
		String name = TokenIterators.getCSName(tokens);
		TokenIterators.consumeEquals(tokens);
		Token token = tokens.next();
		state.allowExpension();
		TokenAssignment tokenAssignment = new TokenAssignment(name, token);
		state.addTokenAssignment(tokenAssignment);
	}
	

}
