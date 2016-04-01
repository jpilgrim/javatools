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

import de.jevopi.jetex.ConditionResult;
import de.jevopi.jetex.ProcessorState;
import de.jevopi.jetex.tex.Category;
import de.jevopi.jetex.tex.expansion.ExpansionError;
import de.jevopi.jetex.tex.tokens.ITokenIterator;
import de.jevopi.jetex.tex.tokens.Token;
import de.jevopi.jetex.tex.tokens.TokenIterators;

/**
 * See {@link AbstractConditional}
 */
public class Ifnum extends AbstractConditional {
	
	@Override
	protected ConditionResult evaluateTest(ProcessorState state, ITokenIterator tokens) {
		Optional<Integer> left = TokenIterators.getNumber(tokens);
		if (!left.isPresent()) {
			throw new ExpansionError(tokens.getLocation(), "Expected number");
		}
		Token op = tokens.next();
		if (op.getCategory()!=Category.OTHER || op.rawValue().length()!=1) {
			throw new ExpansionError(tokens.getLocation(), "Expected operand");
		}
		Optional<Integer> right = TokenIterators.getNumber(tokens);
		if (!right.isPresent()) {
			throw new ExpansionError(tokens.getLocation(), "Expected number");
		}
		boolean test;
		switch ("<>=".indexOf(op.rawValue())) {
			case 0: test=left.get()<right.get(); break;
			case 1: test=left.get()>right.get(); break;
			case 2: test=left.get()==right.get(); break;
			default: 
				throw new ExpansionError(tokens.getLocation(), "Expected operand <, >, or =");
		}
		return new ConditionResult(test);
	}
	
}
