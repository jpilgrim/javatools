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

import static de.jevopi.jetex.tex.Category.SPACE;
import de.jevopi.jetex.tex.tokens.CharacterToken;
import de.jevopi.jetex.tex.tokens.Token;

/**
 * cf 2.5.3 State M
 */
class StateMiddleOfLine extends AbstractInputProcessorState {

	StateMiddleOfLine(InputProcessor inputProcessor) {
		super(inputProcessor);
	}

	@Override
	protected Token caseEOL(Character input) {
		Token t = new CharacterToken(input, SPACE);
		inputProcessor.changeToStateNewline();
		return t;
	}

	/**
	 * cf 2.7.7 10
	 */
	@Override
	protected Token caseSpace(Character input) {
		Token t = new CharacterToken(input, SPACE);
		inputProcessor.changeToStateSkippingSpaces();
		return t;
	}

	@Override
	public String toString() {
		return "middle of line";
	}
}