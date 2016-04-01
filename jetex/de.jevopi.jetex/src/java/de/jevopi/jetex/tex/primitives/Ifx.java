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

import de.jevopi.jetex.Command;
import de.jevopi.jetex.ConditionResult;
import de.jevopi.jetex.ProcessorState;
import de.jevopi.jetex.tex.tokens.ControlSequence;
import de.jevopi.jetex.tex.tokens.ITokenIterator;
import de.jevopi.jetex.tex.tokens.ParameterToken;
import de.jevopi.jetex.tex.tokens.Token;

/**
 * See {@link AbstractConditional}
 */
public class Ifx extends AbstractConditional {
	
	@Override
	protected ConditionResult evaluateTest(ProcessorState state, ITokenIterator tokens) {
		state.inhibitExpansion();
		Token left = tokens.next();
		Token right = tokens.next();
		state.allowExpension();
		boolean test = false;
		if (left.getTokenType()==right.getTokenType()) {
			switch (left.getTokenType()) {
			case Character: 
				test = left.getCategory()==right.getCategory() && left.characterCode()==right.characterCode();
				break;
			case ControlSequence:
				Command cLeft = state.getCommand(((ControlSequence) left).getSequence());
				Command cRight = state.getCommand(((ControlSequence) right).getSequence());
				test = cLeft.similar(cRight);
				break;
			case Parameter:
				test = ((ParameterToken) left).getIndex()==((ParameterToken) right).getIndex();
				break;
			}
		}
			
		return new ConditionResult(test);
	}
	
}
