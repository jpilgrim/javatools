/*
 * Copyright (c) 2016 Jens von Pilgrim
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *   Jens von Pilgrim - Initial API and implementation
 */
package de.jevopi.jetex.latex;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import de.jevopi.jetex.ProcessorState;
import de.jevopi.jetex.tex.expansion.MacroExpansion;
import de.jevopi.jetex.tex.tokens.ITokenIterator;
import de.jevopi.jetex.tex.tokens.Token;

/**
 * <pre>
 * \newenvironment{name}[noParameters][default]{replacementBegin}{replacementEnd}
 * </pre>
 */
public class LatexEnvironment {

	public static class EnvironmentStatus {
		public final LatexEnvironment environment;

		public final List<? extends Iterable<Token>> arguments;

		public EnvironmentStatus(LatexEnvironment environment, List<? extends Iterable<Token>> arguments) {
			this.environment = environment;
			this.arguments = arguments;
		}

	}
	
	private final String name;

	private final int noParameters;

	private final List<? extends Collection<Token>> defaults;

	private final Collection<Token> replacementBegin;

	private final Collection<Token> replacementEnd;

	public LatexEnvironment(String name, int noParameters, List<? extends Collection<Token>> defaults,
			Collection<Token> replacementBegin, Collection<Token> replacementEnd) {
		this.name = name;
		this.noParameters = noParameters;
		this.defaults = defaults;
		this.replacementBegin = replacementBegin;
		this.replacementEnd = replacementEnd;
	}

	@Override
	public String toString() {
		StringBuilder strb = new StringBuilder("env ").append(name);
		if (noParameters != 0) {
			strb.append("[").append(noParameters).append("]");
		}
		for (Collection<Token> def : defaults) {
			strb.append("[");
			Iterator<Token> iter = def.iterator();
			if (iter.hasNext()) {
				strb.append(iter.next().rawValue());
				if (iter.hasNext()) {
					strb.append("…");
				}
			}
			strb.append("]");
		}
		return strb.toString();
	}

	public String getName() {
		return name;
	}

	public Iterator<Token> expandBegin(ProcessorState state, ITokenIterator tokens) {
		List<? extends Iterable<Token>> arguments = LatexTokenIterators.getArguments(tokens, noParameters, defaults);
		LatexProcessorState.cast(state).beginEnvironment(new EnvironmentStatus(this, arguments));
		return new MacroExpansion(replacementBegin.iterator(), arguments);
	}

	public Iterator<Token> expandEnd(ProcessorState state, ITokenIterator tokens) {
		LatexProcessorState.cast(state).endEnvironment();
		return new MacroExpansion(replacementEnd.iterator(), Collections.emptyList());
	}
	
}
