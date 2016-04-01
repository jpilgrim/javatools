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
import de.jevopi.jetex.tex.expansion.ExpansionError;
import de.jevopi.jetex.tex.tokens.ITokenIterator;
import de.jevopi.jetex.tex.tokens.TokenIterators;

/**
 * See {@link AbstractConditional}
 */
public class Ifcase extends AbstractConditional {
	
	@Override
	protected ConditionResult evaluateTest(ProcessorState state, ITokenIterator tokens) {
		Optional<Integer> num = TokenIterators.getNumber(tokens);
		if (!num.isPresent()) {
			throw new ExpansionError(tokens.getLocation(), "Expected number");
		}
		return new ConditionResult(num.get());
	}
	
}
