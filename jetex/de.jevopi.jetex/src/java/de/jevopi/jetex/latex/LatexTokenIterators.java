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
package de.jevopi.jetex.latex;

import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.jevopi.jetex.tex.Category;
import de.jevopi.jetex.tex.execution.ExecutionError;
import de.jevopi.jetex.tex.expansion.ExpansionError;
import de.jevopi.jetex.tex.tokens.ControlSequence;
import de.jevopi.jetex.tex.tokens.ITokenIterator;
import de.jevopi.jetex.tex.tokens.Token;
import de.jevopi.jetex.tex.tokens.TokenIterators;

public class LatexTokenIterators {

	/**
	 * Returns list of arguments, optional arguments may be replaced with their default value.
	 */
	public static List<? extends Iterable<Token>> getArguments(ITokenIterator tokens, int noParameters,
			List<? extends Collection<Token>> defaults) {
		if (noParameters == 0) {
			return emptyList();
		}
		List<Collection<Token>> arguments = new ArrayList<>(noParameters);

		while (arguments.size() < defaults.size()) {
			List<Token> arg = LatexTokenIterators.getOptionalArg(tokens);
			if (arg == null) {
				break;
			}
			arguments.add(arg);
		}
		for (int i = arguments.size(); i < defaults.size(); i++) {
			arguments.add(defaults.get(i));
		}
		while (arguments.size() < noParameters) {
			List<Token> arg = TokenIterators.getUndelimitedArg(tokens);
			arguments.add(arg);
		}
		return arguments;
	}

	/**
	 * Returns collection of all elements until end of optional group without
	 * expanding (nested optional groups are added as well). The begin optional
	 * group token is expected to be consumed before. The end optional group
	 * token is consumed but not added to the returned collection. Returns null
	 * if no optional group is found.
	 */
	public static List<Token> getOptionalArg(ITokenIterator tokens) {
		Token t = tokens.peek();
		if (t==null) {
			return null;
		}
		if (isBeginOptionalGroup(t)) {
			tokens.next(); // consume
			int depth = 0;
			List<Token> elements = new ArrayList<>();
			do {
				Token s = tokens.next();
				if (isBeginOptionalGroup(s)) {
					depth++;
				}
				if (isEndOptionalGroup(s)) {
					depth--;
				}
				if (depth >= 0) {
					elements.add(s);
				}
			} while (depth >= 0);
			return elements;
		}
		return null;
	}

	public static boolean isBeginOptionalGroup(Token token) {
		return (token.getCategory() == Category.OTHER && "[".equals(token.rawValue()));
	}

	public static boolean isEndOptionalGroup(Token token) {
		return (token.getCategory() == Category.OTHER && "]".equals(token.rawValue()));
	}
	
	/**
	 * Returns the number of parameters specified in newcommand or newenvironment. 
	 */
	public static int getNoParameters(ITokenIterator tokens) {
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

	/**
	 * Returns the control sequence name, either in a group (latex) or plain (tex).
	 */
	public static String getCSName(ITokenIterator tokens) {
		Token t = tokens.next();
		if (t instanceof ControlSequence) {
			return t.getSequence();
		}
		if (t.getCategory() == Category.BEGINGROUP) {
			Collection<Token> group = TokenIterators.fetchGroup(tokens);
			if (group.size() != 1 || !(group.iterator().next() instanceof ControlSequence)) {
				throw new ExpansionError(tokens.getLocation(), "Unsupported, expectd a single control sequence");
			}
			return group.iterator().next().getSequence();
		}
		throw new ExpansionError(tokens.getLocation(), "Expected Control Sequence, was " + t);
	}

	/**
	 * Returns the replacement group (tex) or single element (latex)
	 */
	public static Collection<Token> getReplacement(ITokenIterator tokens) {
		Token bg = tokens.next();
		if (bg.getCategory() == Category.BEGINGROUP) {
			return TokenIterators.fetchGroup(tokens);
		}
		return Collections.singleton(bg);
	}


}
