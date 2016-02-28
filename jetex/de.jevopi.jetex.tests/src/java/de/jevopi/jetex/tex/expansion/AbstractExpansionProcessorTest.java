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
package de.jevopi.jetex.tex.expansion;

import static org.junit.Assert.assertEquals;
import de.jevopi.jetex.ProcessorState;
import de.jevopi.jetex.tex.expansion.ExpansionProcessor;
import de.jevopi.jetex.tex.lexer.InputProcessor;
import de.jevopi.jetex.tex.tokens.Token;

public abstract class AbstractExpansionProcessorTest {

	public void assertExpansion(String expectedOutput, String input) {
		ProcessorState env = new ProcessorState();
		InputProcessor ip = new InputProcessor(input, env);
		ExpansionProcessor ep = new ExpansionProcessor(env, ip);
		StringBuilder strb = new StringBuilder();
		while (ep.hasNext()) {
			Token t = ep.next();
			String s = t.rawValue();
			strb.append(s);
		}

		assertEquals(expectedOutput, strb.toString());
	}

	public void assertExpansion(String expectedOutput, String macroDefinition, String input) {
		assertExpansion(expectedOutput, macroDefinition + input);
	}

}
