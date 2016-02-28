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
package de.jevopi.jetex.tex.lexer;

import de.jevopi.jetex.tex.Category;
import de.jevopi.jetex.tex.tokens.Token;

/**
 * cf 2.5.2 State S
 */
class StateSkippingSpaces extends AbstractInputProcessorState {

	StateSkippingSpaces(InputProcessor inputProcessor) {
		super(inputProcessor);
	}

	@Override
	protected Token caseEOL(Character input) {
		inputProcessor.changeToStateNewline();
		return inputProcessor.next();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Token defaultCase(Category category, Character input) {
		inputProcessor.changeToStateMiddleOfLine();
		return inputProcessor.state.switchCategory(category, input);
	}

	@Override
	public String toString() {
		return "skipping spaces";
	}

}