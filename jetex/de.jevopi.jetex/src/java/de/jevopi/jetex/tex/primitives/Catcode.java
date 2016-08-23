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

import java.util.Optional;

import de.jevopi.jetex.ProcessorState;
import de.jevopi.jetex.tex.Category;
import de.jevopi.jetex.tex.execution.ExecutionError;
import de.jevopi.jetex.tex.tokens.IExpandableTokenIterator;
import de.jevopi.jetex.tex.tokens.TokenIterators;

/**
 * <pre>
 * \catcode«number»=«number»
 * \catcode'«char»=«number»
 * \catcode'\«char»=«number»
 * </pre>
 */
public class Catcode extends PrimitiveCommand {
	
	

	@Override
	public void execute(ProcessorState state, IExpandableTokenIterator tokens) {
		state.inhibitExpansion();
		Optional<Integer> characterCode = TokenIterators.getNumber(tokens);
		if (characterCode.isPresent()) {
			char character = (char) (characterCode.get().intValue());
			TokenIterators.consumeEquals(tokens);
			Optional<Integer> categoryCode = TokenIterators.getNumber(tokens);
			if (! categoryCode.isPresent()) {
				throw new ExecutionError(tokens.getLocation(), "Expected category number");
			}
			
//			System.out.println("old catcode for " + character +": " + state.getCatcodeMap().category(character));
			
			state.getCatcodeMap().catcode(character, Category.forCode(categoryCode.get()));
			
//			System.out.println("new catcode for " + character +": " + state.getCatcodeMap().category(character));
		} else {
			throw new ExecutionError(tokens.getLocation(), "Expected character number");
		}
		state.allowExpension();
	}

}
