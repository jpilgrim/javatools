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
package de.jevopi.jetex.tex;

import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import de.jevopi.jetex.Command;
import de.jevopi.jetex.ProcessorState;
import de.jevopi.jetex.tex.execution.ExecutionError;
import de.jevopi.jetex.tex.expansion.MacroExpansion;
import de.jevopi.jetex.tex.tokens.ITokenIterator;
import de.jevopi.jetex.tex.tokens.Token;
import de.jevopi.jetex.tex.tokens.TokenIterators;

/**
 * A (user defined) macro definition (via \def).
 */
public class Macro extends Command {

	EnumSet<MacroPrefix> prefixes;

	private final String name;

	private final List<List<Token>> parDelimiters;

	private final Collection<Token> replacement;

	public Macro(String name, List<List<Token>> parDelimiters, Collection<Token> replacement) {
		this.name = name;
		this.parDelimiters = parDelimiters;
		this.replacement = replacement;
	}

	@Override
	public String toString() {
		StringBuilder strb = new StringBuilder();
		if (parDelimiters != null) {
			for (int i = 0; i < parDelimiters.size(); i++) {
				strb.append('#').append(i + 1);
				parDelimiters.get(i).forEach(t -> strb.append(t.rawValue()));
			}
		}
		return "\\" + name + strb.toString();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Iterator<Token> expand(ProcessorState state, ITokenIterator tokens) {
		List<? extends Iterable<Token>> arguments = getArguments(tokens);
		return new MacroExpansion(replacement.iterator(), arguments);
	}

	/**
	 * Visible for testing only.
	 */
	public List<? extends Iterable<Token>> getArguments(ITokenIterator tokens) {
		if (parDelimiters.isEmpty()) {
			return emptyList();
		}
		List<List<Token>> arguments = new ArrayList<List<Token>>(parDelimiters.size() - 1);
		boolean first = true;
		for (List<Token> delimiters : parDelimiters) {
			if (first) {
				for (Token dToken : delimiters) {
					if (!tokens.hasNext()) {
						throw new ExecutionError(tokens.getLocation(), "Macro " + getName()
								+ " needs to start with delimiter");
					}
					if (!dToken.equals(tokens.next())) {
						throw new ExecutionError(tokens.getLocation(), "Wrong usage of macro " + getName()
								+ ", wrong preceding delimiter");
					}
				}
				first = false;
			} else {
				ArrayList<Token> arg;
				if (delimiters.isEmpty()) {
					arg = TokenIterators.getUndelimitedArg(tokens);
				} else {
					arg = TokenIterators.getDelimitedArg(tokens, delimiters);
				}
				arguments.add(arg);
			}
		}
		return arguments;
	}

	@Override
	public boolean similar(Command c) {
		if (c == this) {
			return true;
		}
		if (c instanceof Macro) {
			Macro m = (Macro) c;

			if (parDelimiters.size() != m.parDelimiters.size()) {
				return false;
			}
			if (!TokenIterators.similar(replacement, m.replacement)) {
				return false;
			}
			for (int i = 0; i < parDelimiters.size(); i++) {
				if (!TokenIterators.similar(parDelimiters.get(i), m.parDelimiters.get(i))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

}
