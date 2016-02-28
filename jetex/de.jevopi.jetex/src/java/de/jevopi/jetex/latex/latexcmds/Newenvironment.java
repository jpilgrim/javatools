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
import de.jevopi.jetex.latex.LatexEnvironment;
import de.jevopi.jetex.latex.LatexProcessorState;
import de.jevopi.jetex.latex.LatexTokenIterators;
import de.jevopi.jetex.tex.tokens.IExpandableTokenIterator;
import de.jevopi.jetex.tex.tokens.Token;
import de.jevopi.jetex.tex.tokens.TokenIterators;

/**
 * <pre>
 * \newenvironment{\«name»}[«no_parameters»][«default»]«replacementBegin»«replacementEnd»
 * </pre>
 */
public class Newenvironment extends LatexCommand {

	@Override
	public void execute(ProcessorState state, IExpandableTokenIterator tokens) {
		state.inhibitExpansion();
		String envname = TokenIterators.getGroupedString(tokens);
		int noParameters = LatexTokenIterators.getNoParameters(tokens);
		List<List<Token>> defaults = new ArrayList<>(noParameters);
		while (defaults.size() < noParameters) {
			List<Token> def = LatexTokenIterators.getOptionalArg(tokens);
			if (def == null) {
				break;
			}
			defaults.add(def);
		}
		Collection<Token> replacementBegin = LatexTokenIterators.getReplacement(tokens);
		Collection<Token> replacementEnd = LatexTokenIterators.getReplacement(tokens);
		state.allowExpension();
		LatexEnvironment env = new LatexEnvironment(envname, noParameters, defaults, replacementBegin, replacementEnd);
		LatexProcessorState.cast(state).addEnvironment(env);
	}

}
