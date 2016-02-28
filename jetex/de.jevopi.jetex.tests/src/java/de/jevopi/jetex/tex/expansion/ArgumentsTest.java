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

import java.util.List;

import org.junit.Test;

import de.jevopi.jetex.ProcessorState;
import de.jevopi.jetex.tex.Macro;
import de.jevopi.jetex.tex.execution.ExecutionProcessor;
import de.jevopi.jetex.tex.expansion.ExpansionProcessor;
import de.jevopi.jetex.tex.lexer.InputProcessor;
import de.jevopi.jetex.tex.lexer.InputSource;
import de.jevopi.jetex.tex.tokens.ExpandableTokenIterator;
import de.jevopi.jetex.tex.tokens.Token;

public class ArgumentsTest extends AbstractExpansionProcessorTest {

	@Test
	public void undelimited1() {
		assertMacroArguments(new String[] { "1", "2", "3" }, "\\def\\foo#1#2#3{}", "123");
	}

	@Test
	public void undelimited2() {
		assertMacroArguments(new String[] { "1", "2", "3" }, "\\def\\foo#1#2#3{}", " 1 2 3");
	}

	@Test
	public void undelimited3() {
		assertMacroArguments(new String[] { "1", "23", "4" }, "\\def\\foo#1#2#3{}", " 1{23}4");
	}

	@Test
	public void e1() {
		assertMacroArguments(new String[] { "1" }, "\\def\\foo#1{}", "1");
		assertMacroArguments(new String[] { "1" }, "\\def\\foo#1{}", " 1");
	}

	@Test
	public void e2() {
		assertMacroArguments(new String[] { "1", "2", "3" }, "\\def\\foo#1#2#3{}", "123");
		assertMacroArguments(new String[] { "1", "2", "3" }, "\\def\\foo#1#2#3{}", " 1 2 3");
		assertMacroArguments(new String[] { "1", "2", "3" }, "\\def\\foo#1#2#3{}", " 1 {2} 3");
	}

	@Test
	public void undelimitedArguments() {
		assertMacroArguments(new String[] { "x", "T" }, "\\def\\foo#1#2{}", "x This sentence is the argument.");
	}

	@Test
	public void delimitedArgs1() {
		assertMacroArguments(new String[] { "x", "y" }, "\\def\\foo#1#2.{}", "xy. But this not");
	}

	/**
	 * cf. 11.5.3 Examples with delimited arguments
	 */
	@Test
	public void delimitedArguments() {
		assertMacroArguments(new String[] { "x", "The arg" }, "\\def\\foo#1#2.{}", "xThe arg. But this not");
	}

	private void assertMacroArguments(String[] expectedArgs, String macroDefinition, String input) {

		// create macro
		ProcessorState env = new ProcessorState();
		InputProcessor ip = new InputProcessor(env);
		ip.addInputSource(new InputSource("macroDefinition", macroDefinition));
		ExpansionProcessor ep = new ExpansionProcessor(env, ip);
		ExecutionProcessor ex = new ExecutionProcessor(env, ep);

		// read and process whole macro definition
		while (ex.hasNext())
			ex.next();

		// env now contains macro definition:
		Macro foo = (Macro) env.getCommand("foo");

		// read arguments
		ip = new InputProcessor(env);
		ip.addInputSource(new InputSource("arguments", input));
		
		ExpandableTokenIterator tokens = new ExpandableTokenIterator();
		tokens.add(ip);
		List<? extends Iterable<Token>> args = foo.getArguments(tokens);

		// test
		assertEquals(expectedArgs.length, args.size());
		for (int i = 0; i < args.size(); i++) {
			Iterable<Token> arg = args.get(i);
			StringBuilder strb = new StringBuilder();
			arg.forEach(token -> strb.append(token.rawValue()));
			assertEquals("#" + (i + 1), expectedArgs[i], strb.toString());
		}
	}

}
