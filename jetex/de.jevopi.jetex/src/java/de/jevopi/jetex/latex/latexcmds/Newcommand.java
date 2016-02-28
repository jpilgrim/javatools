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
package de.jevopi.jetex.latex.latexcmds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.jevopi.jetex.ProcessorState;
import de.jevopi.jetex.latex.LatexMacro;
import de.jevopi.jetex.latex.LatexTokenIterators;
import de.jevopi.jetex.tex.Category;
import de.jevopi.jetex.tex.execution.ExecutionError;
import de.jevopi.jetex.tex.expansion.ExpansionError;
import de.jevopi.jetex.tex.tokens.ControlSequence;
import de.jevopi.jetex.tex.tokens.ExpandableTokenIterator;
import de.jevopi.jetex.tex.tokens.IExpandableTokenIterator;
import de.jevopi.jetex.tex.tokens.ITokenIterator;
import de.jevopi.jetex.tex.tokens.Token;

/**
 * <pre>
 * \newcommand{\«name»}[«no_parameters»][«default»]...«replacement»
 * </pre>
 * 
 * vs.
 * 
 * <pre>
 * \def#1...{«replacement»}
 * </pre>
 */
public class Newcommand extends LatexCommand {

	@Override
	public void execute(ProcessorState state, IExpandableTokenIterator tokens) {
		state.inhibitExpansion();
		String csname = getCSName(tokens);
		int noParameters = getNoParameters(tokens);
		List<List<Token>> defaults = new ArrayList<>(noParameters);
		while (defaults.size()<noParameters) {
			List<Token> def = LatexTokenIterators.getOptionalArg(tokens);
			if (def==null) {
				break;
			}
			defaults.add(def);
		}	
		Collection<Token> replacement = getReplacement(tokens);
		state.allowExpension();
		LatexMacro macro = new LatexMacro(csname, noParameters, defaults, replacement);
		state.addMacro(macro);
	}

	private int getNoParameters(ITokenIterator tokens) {
		List<Token> noParametersGroup = LatexTokenIterators.getOptionalArg(tokens);
		if (noParametersGroup!=null) {
			if (noParametersGroup.size()!=1) {
				throw new ExecutionError(tokens.getLocation(), "Expect single digit for number of parameters");
			}
			Token t = noParametersGroup.get(0);
			if (t.getCategory()!=Category.OTHER) {
				throw new ExecutionError(tokens.getLocation(), "Expect digit for number of parameters");
			}
			try {
				return Integer.parseInt(t.rawValue());
			} catch (NumberFormatException ex) {
				throw new ExecutionError(tokens.getLocation(), "Expect digit for number of parameters, got " + t.rawValue());
			}
		}
		return 0;
	}

	protected String getCSName(ITokenIterator tokens) {
		Token t = tokens.next();
		if (t instanceof ControlSequence) {
			return t.getSequence();
		}
		if (t.getCategory() == Category.BEGINGROUP) {
			Collection<Token> group = ExpandableTokenIterator.fetchGroup(tokens);
			if (group.size() != 1 || !(group.iterator().next() instanceof ControlSequence)) {
				throw new ExpansionError(tokens.getLocation(), "Unsupported, expectd a single control sequence");
			}
			return group.iterator().next().getSequence();
		}
		throw new ExpansionError(tokens.getLocation(), "Expected Control Sequence, was " + t);
	}

	protected Collection<Token> getReplacement(ITokenIterator tokens) {
		Token bg = tokens.next();
		if (bg.getCategory() == Category.BEGINGROUP) {
			return ExpandableTokenIterator.fetchGroup(tokens);
		}
		return Collections.singleton(bg);
	}

	

}
