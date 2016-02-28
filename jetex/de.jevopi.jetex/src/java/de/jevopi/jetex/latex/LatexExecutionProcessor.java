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

import de.jevopi.jetex.tex.execution.ExecutionProcessor;
import de.jevopi.jetex.tex.expansion.ExpansionProcessor;

public class LatexExecutionProcessor extends ExecutionProcessor {

	public LatexExecutionProcessor(LatexProcessorState state, ExpansionProcessor expansionProcessor) {
		super(state, expansionProcessor);
	}

}
