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

import static de.jevopi.jetex.tex.Category.LETTER;
import de.jevopi.jetex.SyntaxError;
import de.jevopi.jetex.tex.Category;
import de.jevopi.jetex.tex.CategorySwitch;
import de.jevopi.jetex.tex.TexLocation;
import de.jevopi.jetex.tex.tokens.CharacterToken;
import de.jevopi.jetex.tex.tokens.CommentToken;
import de.jevopi.jetex.tex.tokens.ControlSymbol;
import de.jevopi.jetex.tex.tokens.ControlWord;
import de.jevopi.jetex.tex.tokens.ParameterToken;
import de.jevopi.jetex.tex.tokens.Token;

abstract class AbstractInputProcessorState extends CategorySwitch<Character, Token> {

	/**
	 * The owning input processor
	 */
	protected final InputProcessor inputProcessor;

	/**
	 * @param inputProcessor
	 */
	AbstractInputProcessorState(InputProcessor inputProcessor) {
		this.inputProcessor = inputProcessor;
	}

	Token nextToken(char c) {
		Category cat = this.inputProcessor.getCatcodeMap().category(c);
		return switchCategory(cat, c);
	}

	/**
	 * cf. 2.7.1
	 */
	@Override
	protected Token caseESC(Character input) {

		if (!this.inputProcessor.hasNext()) {
			/*
			 * This is not TeX-like, but we interpret a last ESC character in
			 * the file as if it has been doubled to be more robust.
			 */
			Token t = new ControlSymbol(input);
			inputProcessor.changeToStateMiddleOfLine();
			return t;
		}
		char c = this.inputProcessor.peekChar();
		Category cat = this.inputProcessor.getCatcodeMap().category(c);
		switch (cat) {
		case LETTER: {
			/*
			 * If the character following the escape is of category 11, letter,
			 * then TEX combines the escape, that character and all following
			 * characters of category 11, into a control word. After that TEX
			 * goes into state S, skipping spaces.
			 */
			StringBuilder sequence = new StringBuilder();
			do {
				sequence.append(this.inputProcessor.nextChar());
				if (this.inputProcessor.hasNext()) {
					c = this.inputProcessor.peekChar();
				}
			} while (this.inputProcessor.getCatcodeMap().isCategory(c, LETTER) && this.inputProcessor.hasNext());
			Token t = new ControlWord(sequence);
			inputProcessor.changeToStateSkippingSpaces();
			return t;
		}
		case SPACE: {
			/*
			 * With a character of category 10, space, a control symbol called
			 * control space results, and TEX goes into state S.
			 */
			Token t = new ControlSymbol(' ');
			inputProcessor.changeToStateSkippingSpaces();
			return t;
		}
		case COMMENT: // escape % -- TODO check
		case SUB: // escape _ -- TODO check
		case SUPER: // escape ^ -- TODO check
		case ESC: // escape \ -- TODO check
		case PAR: { // escaped # -- TODO check
			Token t = new CharacterToken(this.inputProcessor.nextChar(), Category.LETTER);
			return t;
		}
		default: {
			Token t = new ControlSymbol(c);
			inputProcessor.changeToStateMiddleOfLine();
			return t;
		}
		} // end switch
	}

	/**
	 * cf 2.7.4 6
	 */
	@Override
	protected Token casePar(Character input) {
		Token t;
		if (!this.inputProcessor.hasNext()) {
			t = new CharacterToken(input, Category.PAR);
		} else {
			char next = this.inputProcessor.nextChar();
			if ('0' <= next && next <= '9') {
				t = new ParameterToken(next);

			} else if (this.inputProcessor.getCatcodeMap().isCategory(next, Category.PAR)) {
				t = new CharacterToken(next, Category.PAR);
			} else {
				throw new SyntaxError(getLocation(), "Did not expect "
						+ this.inputProcessor.getCatcodeMap().category(next) + " after " + input);
			}
		}
		inputProcessor.changeToStateMiddleOfLine();
		return t;
	}

	/**
	 * Delegate method
	 * 
	 * @see de.jevopi.jetex.tex.lexer.InputProcessor#getLocation()
	 */
	public TexLocation getLocation() {
		return inputProcessor.getLocation();
	}

	/**
	 * Characters of category 9 are ignored; TEX remains in the same state.
	 */
	@Override
	protected Token caseIgnore(Character input) {
		return this.inputProcessor.next();
	}

	/**
	 * A token with category code 10 (space token) is ignored in states N and S
	 * (and the state does not change);
	 */
	@Override
	protected Token caseSpace(Character input) {
		return nextToken(this.inputProcessor.nextNonSpace());
	}

	/**
	 * cf 2.7.8 14
	 */
	@Override
	protected Token caseComment(Character input) {
		char c;
		final StringBuilder strb = new StringBuilder();
		while (this.inputProcessor.hasNext()) {
			c = this.inputProcessor.nextChar();
			if (this.inputProcessor.getCatcodeMap().isCategory(c, Category.EOL)) {
				break;
			}
			strb.append(c);
		}
		inputProcessor.changeToStateNewline();
		return new CommentToken(strb.toString());
	}

	/**
	 * cf 2.7.9 15
	 */
	@Override
	protected Token caseInvalid(Character input) {
		throw new IllegalStateException("Invalid character at " + this.inputProcessor.getLocation());
	}

	/**
	 * cf. 2.7.2 1–4, 7–8, 11–13
	 */
	@Override
	protected Token defaultCase(Category category, Character input) {
		return new CharacterToken(input, category);
	}

}