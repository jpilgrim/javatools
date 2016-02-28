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

import java.util.Iterator;

import de.jevopi.jetex.tex.execution.ExecutionProcessor;
import de.jevopi.jetex.tex.expansion.ExpansionProcessor;
import de.jevopi.jetex.tex.lexer.InputProcessor;
import de.jevopi.jetex.tex.lexer.InputSource;
import de.jevopi.jetex.tex.tokens.Token;
import de.jevopi.jetex.visualization.AbstractVisualProcessor;

public abstract class AbstractLatexTransformer {

	
	public String transform(String latexInput) {
		return transform(latexInput, null);
	}
	
	public String transform(String latexInput, String filename) {
		LatexProcessorState state = new LatexProcessorState();
		InputProcessor ip = new InputProcessor(state);
		ip.addInputSource(new InputSource(filename, latexInput));
		ExpansionProcessor ep = new ExpansionProcessor(state, ip);
		ExecutionProcessor ex = new LatexExecutionProcessor(state, ep);
		AbstractVisualProcessor vp = getVisualProcessor(ex);
		StringBuilder strb = new StringBuilder();
		while (vp.hasNext()) {
			strb.append(vp.next());
		}
		return strb.toString();
	}
	
	abstract protected AbstractVisualProcessor getVisualProcessor(Iterator<Token> tokens);
}
