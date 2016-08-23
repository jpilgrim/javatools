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
import de.jevopi.jetex.tex.tokens.ParToken;
import de.jevopi.jetex.tex.tokens.Token;

/**
 * cf 2.5.1 State N
 */
class StateNewline extends AbstractInputProcessorState {

	StateNewline(InputProcessor inputProcessor) {
		super(inputProcessor);
	}

	@Override
	protected Token caseEOL(Character input) {
		return ParToken.INSTANCE;
	}

	@Override
	public String toString() {
		return "newline";
	}

	@Override
	protected Token defaultCase(Category category, Character input) {
		inputProcessor.changeToStateMiddleOfLine();
		return inputProcessor.state.switchCategory(category, input);
	}
	
	

}