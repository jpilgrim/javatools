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

import de.jevopi.jetex.ConditionResult;
import de.jevopi.jetex.ProcessorState;
import de.jevopi.jetex.tex.tokens.ITokenIterator;

/**
 * See {@link AbstractConditional}
 */
public class Iffalse extends AbstractConditional {
	
	@Override
	protected ConditionResult evaluateTest(ProcessorState state, ITokenIterator tokens) {
		return new ConditionResult(false);
	}
	
}
