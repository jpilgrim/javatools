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

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

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
	
	List<InputSource> inputSources = new LinkedList<>();
	InputSource is = null;
	
	public InputProcessor(ProcessorState state) {
		super(state);
		
		stateN = new StateNewline(this);
		stateS = new StateSkippingSpaces(this);
		stateM = new StateMiddleOfLine(this);
	}
	
	public void addInputSource(InputSource inputSource) {
		inputSources.add(inputSource);
	}
	

	public TexLocation getLocation() {
		if (is==null) {
			return new TexLocation(0, 0, 0, "no source loaded");
		}
		return new TexLocation(is.index(), is.lineNumber() + 1, is.lineOffset() - 1, is.getName());
	}

	@Override
	public Token next() {
		char c = nextChar();
		return state.nextToken(c);
	}

	@Override
	public boolean hasNext() {
		if (is==null) {
			nextInputSource();
		}
		if (is==null || !is.hasNext()) {
			if (!nextInputSource()) {
				return false;
			}
		}
		return is.hasNext();
	}
	
	private boolean nextInputSource() {
		if (! inputSources.isEmpty()) {
			is = inputSources.get(0);
			inputSources.remove(0);
			changeToStateNewline();
			return true;
		}
		return false;
	}

	private boolean hasNext(int lookahead) {
		if (is==null) {
			if (!nextInputSource()) {
				return false;
			}
		}
		return is.hasNext(lookahead);
	}
	

	char nextChar() {
		if (is==null) {
			if(!nextInputSource()) {
				throw new NoSuchElementException("No more input sources");
			}
		}
		char c = is.nextChar();
		// 2.6 Accessing the full character set
		c = accessFullCharacterSet(c);
		return c;
	}
	
	char peekChar() {
		if (is==null) {
			if(!nextInputSource()) {
				throw new NoSuchElementException("No more input sources");
			}
		}
		return is.peek();
	}
	

	/**
	 * cf. 2.6
	 */
	private char accessFullCharacterSet(final char c) {
		if (getCatcodeMap().isCategory(c, SUPER) && hasNext() && is.peek() == c) {
			if (hasNext(2)) {
				char[] hex = { is.peek(1), is.peek(2) };
				if (isLowerCaseHexDigit(hex[0]) && isLowerCaseHexDigit(hex[1])) {
					is.nextChar();is.nextChar();is.nextChar();
					return (char) Integer.parseInt(new String(hex), 16);
				}
			}
			char offest64 = (char) (is.peek(1) - 64);
			is.nextChar();is.nextChar();
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
			char c = is.peek();
			if (!getCatcodeMap().isCategory(c, SPACE)) {
				return;
			}
			is.nextChar();
		}
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
