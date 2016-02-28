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

import de.jevopi.jetex.tex.tokens.Token;
import de.jevopi.jetex.visualization.AbstractVisualProcessor;
import de.jevopi.jetex.visualization.TextProcessor;

public class LatexToText extends AbstractLatexTransformer {

	@Override
	protected AbstractVisualProcessor getVisualProcessor(Iterator<Token> tokens) {
		return new TextProcessor(tokens);
	}

}
