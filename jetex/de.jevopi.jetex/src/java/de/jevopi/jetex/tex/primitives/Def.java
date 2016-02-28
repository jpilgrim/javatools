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
package de.jevopi.jetex.tex.primitives;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.jevopi.jetex.ProcessorState;
import de.jevopi.jetex.tex.Category;
import de.jevopi.jetex.tex.Macro;
import de.jevopi.jetex.tex.expansion.ExpansionError;
import de.jevopi.jetex.tex.tokens.ControlSequence;
import de.jevopi.jetex.tex.tokens.ExpandableTokenIterator;
import de.jevopi.jetex.tex.tokens.IExpandableTokenIterator;
import de.jevopi.jetex.tex.tokens.ITokenIterator;
import de.jevopi.jetex.tex.tokens.Token;

/**
 * <pre>
 * \def#1...{«replacement»}
 * </pre>
 */
public class Def extends PrimitiveCommand {

	
	@Override
	public void execute(ProcessorState state, IExpandableTokenIterator tokens) {
		state.inhibitExpansion();
		String csname = getCSName(tokens);
		List<List<Token>> parDelimiters = getDelimiters(tokens);
		Collection<Token> replacement = getReplacement(tokens);
		state.allowExpension();
		Macro macro = new Macro(csname, parDelimiters, replacement);
		state.addMacro(macro);
	}

	protected Collection<Token> getReplacement(ITokenIterator tokens) {
		Token bg = tokens.next();
		if (bg.getCategory() == Category.BEGINGROUP) {
			return ExpandableTokenIterator.fetchGroup(tokens);
		}
		throw new ExpansionError(tokens.getLocation(), "Didn't found macro replacement");
	}

	protected List<List<Token>> getDelimiters(ITokenIterator tokens) {
		List<List<Token>> parDelimiters = new ArrayList<>(3);
		while (tokens.peek().getCategory() == Category.PAR) {
			tokens.next();
			List<Token> delimiters = new ArrayList<>(3);
			while (!tokens.peek().getCategory().in(Category.PAR, Category.BEGINGROUP)) {
				delimiters.add(tokens.next());
			}
			parDelimiters.add(delimiters);
		}
		return parDelimiters;
	}

	protected String getCSName(ITokenIterator tokens) {
		Token t = tokens.next();
		if (t instanceof ControlSequence) {
			return ((ControlSequence) t).getSequence();
		}
		throw new ExpansionError(tokens.getLocation(), "Expected Control Sequence, was " + t);
	}

}
