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

import java.util.Iterator;

import de.jevopi.jetex.ProcessorState;
import de.jevopi.jetex.latex.LatexEnvironment;
import de.jevopi.jetex.latex.LatexProcessorState;
import de.jevopi.jetex.tex.tokens.ITokenIterator;
import de.jevopi.jetex.tex.tokens.Token;
import de.jevopi.jetex.tex.tokens.TokenIterators;

/**
 * <pre>
 * \end{«name»}
 * </pre>
 * 
 */
public class End extends LatexCommand {

	@Override
	public Iterator<Token> expand(ProcessorState state, ITokenIterator tokens) {
		String envName = TokenIterators.getGroupedString(tokens);
		LatexEnvironment env = LatexProcessorState.cast(state).getEnvironment(envName);
		return env.expandEnd(state, tokens);
	}
	

}
