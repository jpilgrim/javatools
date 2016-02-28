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

import org.junit.Test;

import de.jevopi.jetex.ProcessorState;
import de.jevopi.jetex.tex.lexer.InputProcessor;

public class InputProcessorTest extends AbstractInputProcessorTest {

	@Test
	public void testLetters() {
		InputProcessor p = new InputProcessor("abc", new ProcessorState());
		assertTokens("L(a),L(b),L(c)", p);
	}

	@Test
	public void testLettersAndSpaces() {
		InputProcessor p = new InputProcessor("ab  c", new ProcessorState());
		assertTokens("L(a),L(b),S( ),L(c)", p);
	}

	@Test
	public void testLettersAndSpacesAtEnd() {
		InputProcessor p = new InputProcessor("abc  ", new ProcessorState());
		assertTokens("L(a),L(b),L(c),S( )", p);
	}

	@Test
	public void testCommandAndLetters() {
		InputProcessor p = new InputProcessor("a\\bc", new ProcessorState());
		assertTokens("L(a),\\bc", p);
	}

	@Test
	public void testCommandAndLettersAndSpace() {
		InputProcessor p = new InputProcessor("a\\bc d", new ProcessorState());
		assertTokens("L(a),\\bc,L(d)", p);
	}

	@Test
	public void testCommandAndLettersAndSpace2() {
		InputProcessor p = new InputProcessor("a \\bc d", new ProcessorState());
		assertTokens("L(a),S( ),\\bc,L(d)", p);
	}

	@Test
	public void testLetterAndComment() {
		InputProcessor p = new InputProcessor("a%comment", new ProcessorState());
		assertTokens("L(a),CMT(comment)", p);
	}

	@Test
	public void testLetterAndCommentWithNewLine() {
		InputProcessor p = new InputProcessor("a%comment\nb", new ProcessorState());
		assertTokens("L(a),CMT(comment),L(b)", p);
	}

}
