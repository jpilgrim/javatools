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
import java.util.List;

import de.jevopi.jetex.ProcessorState;
import de.jevopi.jetex.latex.LatexMacro;
import de.jevopi.jetex.latex.LatexTokenIterators;
import de.jevopi.jetex.tex.tokens.IExpandableTokenIterator;
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
		String csname = LatexTokenIterators.getCSName(tokens);
		int noParameters = LatexTokenIterators.getNoParameters(tokens);
		List<List<Token>> defaults = new ArrayList<>(noParameters);
		while (defaults.size()<noParameters) {
			List<Token> def = LatexTokenIterators.getOptionalArg(tokens);
			if (def==null) {
				break;
			}
			defaults.add(def);
		}	
		Collection<Token> replacement = LatexTokenIterators.getReplacement(tokens);
		state.allowExpension();
		LatexMacro macro = new LatexMacro(csname, noParameters, defaults, replacement);
		state.addMacro(macro);
	}

	
	

}
