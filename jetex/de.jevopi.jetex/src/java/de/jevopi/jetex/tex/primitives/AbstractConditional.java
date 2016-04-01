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

import de.jevopi.jetex.ConditionResult;
import de.jevopi.jetex.ProcessorState;
import de.jevopi.jetex.tex.expansion.ExpansionError;
import de.jevopi.jetex.tex.tokens.ITokenIterator;
import de.jevopi.jetex.tex.tokens.Token;

/**
 * 
 * 
 * 
 * Condition false: - skip until else or fi without expansion but if-nesting -
 * fi: consume fi, done - else: consume else, require fi
 * 
 * Condition true: -
 * 
 * 
 * 
 * 
 * 
 */

public abstract class AbstractConditional extends PrimitiveCommand {

	@Override
	public Iterator<Token> expand(ProcessorState state, ITokenIterator tokens) {
		ConditionResult conditionResult = evaluateTest(state, tokens);
		if (conditionResult.isTest()) {
			if (conditionResult.selectElse()) {
				boolean elseBranchFound = skipToElseOrFi(state, tokens);
				if (elseBranchFound) {
					state.pushConditionResult(conditionResult);
				}
			} else {
				state.pushConditionResult(conditionResult);
			}
		} else { // case
			if (conditionResult.getCase() != 0) {
				boolean caseOrElseFound = skipToCaseOrFi(state, tokens, conditionResult);
				if (caseOrElseFound) {
					state.pushConditionResult(conditionResult);
				}
			} else {
				state.pushConditionResult(conditionResult);
			}
		}
		return null; // if itself is not expanded
	}

	protected abstract ConditionResult evaluateTest(ProcessorState state, ITokenIterator tokens);

	private boolean skipToElseOrFi(ProcessorState state, ITokenIterator tokens) {
		state.inhibitExpansion();
		try {
			while (tokens.hasNext()) {
				Token token = tokens.next();
				if ("fi".equals(token.getSequence())) {
					return false;
				}
				if ("else".equals(token.getSequence())) {
					return true;
				}
			}
			throw new ExpansionError(tokens.getLocation(), "Incomplete \\if, all code was ignored.");
		} finally {
			state.allowExpension();
		}
	}

	private boolean skipToCaseOrFi(ProcessorState state, ITokenIterator tokens, ConditionResult conditionResult) {
		state.inhibitExpansion();
		int caseNum = 0;
		try {
			while (tokens.hasNext()) {
				Token token = tokens.next();
				if ("fi".equals(token.getSequence())) {
					return false;
				} else if ("or".equals(token.getSequence())) {
					caseNum++;
					if (caseNum == conditionResult.getCase()) {
						return true;
					}
				} else if ("else".equals(token.getSequence())) {
					return true;
				}
			}
			throw new ExpansionError(tokens.getLocation(), "Incomplete \\if, all code was ignored.");
		} finally {
			state.allowExpension();
		}
	}

}
