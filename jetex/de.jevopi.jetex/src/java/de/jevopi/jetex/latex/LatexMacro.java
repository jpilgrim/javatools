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

import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import de.jevopi.jetex.Command;
import de.jevopi.jetex.ProcessorState;
import de.jevopi.jetex.tex.MacroPrefix;
import de.jevopi.jetex.tex.expansion.MacroExpansion;
import de.jevopi.jetex.tex.tokens.ITokenIterator;
import de.jevopi.jetex.tex.tokens.Token;

/**
 * A latex macro definition consists of, in sequence,
 * <ol>
 * <li>TODO any number of \global, \long, and \outer prefixes,
 * <li>a control sequence or active character to be defined,
 * <li>the number of parameters
 * <li>default tokens for parameters
 * <li>a replacement text
 * </ol>
 */
public class LatexMacro extends Command {

	EnumSet<MacroPrefix> prefixes;

	private final String name;

	private final int noParameters;

	private final List<? extends Collection<Token>> defaults;

	private final Collection<Token> replacement;

	public LatexMacro(String name, int noParameters, List<? extends Collection<Token>> defaults,
			Collection<Token> replacement) {
		this.name = name;
		this.noParameters = noParameters;
		this.defaults = defaults;
		this.replacement = replacement;
	}

	@Override
	public String toString() {
		StringBuilder strb = new StringBuilder("\\").append(name);
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

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Iterator<Token> expand(ProcessorState state, ITokenIterator tokens) {
		List<? extends Iterable<Token>> arguments = LatexTokenIterators.getArguments(tokens, noParameters, defaults);
		return new MacroExpansion(replacement.iterator(), arguments);
	}

}
