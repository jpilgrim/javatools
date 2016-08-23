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

import de.jevopi.jetex.tex.tokens.IExpandableTokenIterator;
import de.jevopi.jetex.visualization.AbstractVisualProcessor;
import de.jevopi.jetex.visualization.SimpleHTMLProcessor;

public class LatexToHTML extends AbstractLatexTransformer {

	@Override
	protected AbstractVisualProcessor getVisualProcessor(LatexProcessorState state, IExpandableTokenIterator tokens) {
		return new SimpleHTMLProcessor(state, tokens);
	}
	
	protected LatexProcessorState createLatexProcessorState() {
		return new AutoLatexProcessorState();
	}

}
