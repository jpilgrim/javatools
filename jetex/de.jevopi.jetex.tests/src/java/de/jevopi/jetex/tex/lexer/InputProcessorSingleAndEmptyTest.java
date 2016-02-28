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
import de.jevopi.jetex.SyntaxError;
import de.jevopi.jetex.tex.lexer.InputProcessor;

public class InputProcessorSingleAndEmptyTest extends AbstractInputProcessorTest {

	@Test
	public void testEmpty() {
		InputProcessor p = new InputProcessor(new ProcessorState());
		p.addInputSource(new InputSource("test", ""));
		assertTokens("", p);
	}

	@Test
	public void testSingleLetter() {
		InputProcessor p = new InputProcessor(new ProcessorState());
		p.addInputSource(new InputSource("test", "a"));
		assertTokens("L(a)", p);
	}

	@Test
	public void testSingleCommandWord() {
		InputProcessor p = new InputProcessor(new ProcessorState());
		p.addInputSource(new InputSource("test", "\\a"));
		assertTokens("\\a", p);
	}

	@Test
	public void testSingleCommandSymbol() {
		InputProcessor p = new InputProcessor(new ProcessorState());
		p.addInputSource(new InputSource("test", "\\"));
		assertTokens("\\\\", p);
	}

	@Test
	public void testSingleCommandSymbolWithSpace() {
		InputProcessor p = new InputProcessor(new ProcessorState());
		p.addInputSource(new InputSource("test", "\\ "));
		assertTokens("\\ ", p);
	}

	@Test
	public void testSingleSpace() {
		InputProcessor p = new InputProcessor(new ProcessorState());
		p.addInputSource(new InputSource("test", " "));
		assertTokens("", p);
	}

	@Test
	public void testSingleSuper() {
		InputProcessor p = new InputProcessor(new ProcessorState());
		p.addInputSource(new InputSource("test", "^"));
		assertTokens("SUP(^)", p);
	}

	@Test
	public void testSingleSub() {
		InputProcessor p = new InputProcessor(new ProcessorState());
		p.addInputSource(new InputSource("test", "_"));
		assertTokens("SUB(_)", p);
	}

	@Test
	public void testSingleActive() {
		InputProcessor p = new InputProcessor(new ProcessorState());
		p.addInputSource(new InputSource("test", "~"));
		assertTokens("ACT(~)", p);
	}

	@Test
	public void testSingleEOL() {
		InputProcessor p = new InputProcessor(new ProcessorState());
		p.addInputSource(new InputSource("test", "\n"));
		assertTokens("\\par", p);
	}

	@Test
	public void testSingleCmt() {
		InputProcessor p = new InputProcessor(new ProcessorState());
		p.addInputSource(new InputSource("test", "%"));
		assertTokens("CMT()", p);
	}

	@Test
	public void testSingleCmtSpace() {
		InputProcessor p = new InputProcessor(new ProcessorState());
		p.addInputSource(new InputSource("test", "% "));
		assertTokens("CMT( )", p);
	}

	@Test
	public void testSingleMathShift() {
		InputProcessor p = new InputProcessor(new ProcessorState());
		p.addInputSource(new InputSource("test", "$"));
		assertTokens("MS($)", p);
	}

	@Test
	public void testSingleOther() {
		InputProcessor p = new InputProcessor(new ProcessorState());
		p.addInputSource(new InputSource("test", "."));
		assertTokens("O(.)", p);
	}

	@Test
	public void testSingleBeginGroup() {
		InputProcessor p = new InputProcessor(new ProcessorState());
		p.addInputSource(new InputSource("test", "{"));
		assertTokens("BG({)", p);
	}

	@Test
	public void testSingleEndGroup() {
		InputProcessor p = new InputProcessor(new ProcessorState());
		p.addInputSource(new InputSource("test", "}"));
		assertTokens("EG(})", p);
	}

	@Test
	public void testSingleTab() {
		InputProcessor p = new InputProcessor(new ProcessorState());
		p.addInputSource(new InputSource("test", "&"));
		assertTokens("TAB(&)", p);
	}

	@Test
	public void testSingleParameterEmpty() {
		InputProcessor p = new InputProcessor(new ProcessorState());
		p.addInputSource(new InputSource("test", "#"));
		assertTokens("PAR(#)", p);
	}

	@Test
	public void testSingleParameter0() {
		InputProcessor p = new InputProcessor(new ProcessorState());
		p.addInputSource(new InputSource("test", "#0"));
		assertTokens("#0", p);
	}

	@Test
	public void testSingleParameter9() {
		InputProcessor p = new InputProcessor(new ProcessorState());
		p.addInputSource(new InputSource("test", "#9"));
		assertTokens("#9", p);
	}

	@Test
	public void testSingleAllCharactersLatex3() {
		InputProcessor p = new InputProcessor(new ProcessorState());
		p.addInputSource(new InputSource("test", "^^57"));
		assertTokens("L(W)", p);
	}

	@Test
	public void testSingleAllCharacters() {
		InputProcessor p = new InputProcessor(new ProcessorState());
		p.addInputSource(new InputSource("test", "^^z"));
		assertTokens("O(:)", p);
	}

	@Test(expected = SyntaxError.class)
	public void testSingleParameterLetter() {
		InputProcessor p = new InputProcessor(new ProcessorState());
		p.addInputSource(new InputSource("test", "#a"));
		tokenString(p);
	}
}
