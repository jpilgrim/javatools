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
import static de.jevopi.jetex.tex.Category.SUPER;
import de.jevopi.jetex.AbstractProcessor;
import de.jevopi.jetex.ProcessorState;
import de.jevopi.jetex.tex.TexLocation;
import de.jevopi.jetex.tex.tokens.Token;

/**
 * The input processor.
 */
public class InputProcessor extends AbstractProcessor {

	private AbstractInputProcessorState stateN, stateS, stateM;

	AbstractInputProcessorState state;

	final CharSequence input;

	int index;

	private int lineNumber = 0;

	private int lineOffset;

	private String filename;

	public InputProcessor(CharSequence input, ProcessorState env) {
		this(input, env, "unknown");
	}

	public InputProcessor(CharSequence input, ProcessorState env, String filename) {
		super(env);
		this.filename = filename; // only for error messages
		this.input = input;
		this.index = 0;
		stateN = new StateNewline(this);
		stateS = new StateSkippingSpaces(this);
		stateM = new StateMiddleOfLine(this);
		changeToStateNewline();
	}

	public TexLocation getLocation() {
		return new TexLocation(index, lineNumber + 1, lineOffset - 1, filename);
	}

	@Override
	public Token next() {
		char c = nextChar();
		return state.nextToken(c);
	}

	@Override
	public boolean hasNext() {
		return index < input.length();
	}

	char nextChar() {

		// 2.6 Accessing the full character set
		char c = input.charAt(index++);
		if (c == '\n') {
			lineNumber++;
			lineOffset = 0;
		} else {
			lineOffset++;
		}
		c = accessFullCharacterSet(c);
		return c;
	}

	/**
	 * cf. 2.6
	 */
	private char accessFullCharacterSet(final char c) {
		if (getCatcodeMap().isCategory(c, SUPER) && index + 1 < input.length() && input.charAt(index) == c) {
			if (index + 2 < input.length()) {
				char[] hex = { input.charAt(index + 1), input.charAt(index + 2) };
				if (isLowerCaseHexDigit(hex[0]) && isLowerCaseHexDigit(hex[1])) {
					index += 3;
					return (char) Integer.parseInt(new String(hex), 16);
				}
			}
			char offest64 = (char) (input.charAt(index + 1) - 64);
			index += 2;
			return offest64;
		}

		return c;

	}

	char nextNonSpace() {
		char c;
		do {
			c = nextChar();
		} while (getCatcodeMap().isCategory(c, SPACE));
		return c;
	}

	void skipSpaces() {
		while (hasNext()) {
			int oldIndex = index;
			char c = nextChar();
			if (!getCatcodeMap().isCategory(c, SPACE)) {
				index = oldIndex;
				return;
			}
		}
	}

	char peekChar() {
		int oldIndex = index;
		char c = nextChar();
		index = oldIndex;
		return c;
	}

	private boolean isLowerCaseHexDigit(char c) {
		return ('a' <= c && 'f' >= c) || ('0' <= c && '9' >= c);
	}

	void changeToStateNewline() {
		state = stateN;
		skipSpaces();
	}

	void changeToStateSkippingSpaces() {
		state = stateS;
		skipSpaces();
	}

	void changeToStateMiddleOfLine() {
		state = stateM;
	}

}
