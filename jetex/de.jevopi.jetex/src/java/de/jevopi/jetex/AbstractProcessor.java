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
package de.jevopi.jetex;

import java.util.Iterator;

import de.jevopi.jetex.tex.CatcodeMap;
import de.jevopi.jetex.tex.TexLocation;
import de.jevopi.jetex.tex.tokens.Token;

public abstract class AbstractProcessor implements Iterator<Token> {
	public final ProcessorState state;

	public AbstractProcessor(ProcessorState state) {
		this.state = state;
	}

	/**
	 * Delegate method
	 * 
	 * @see de.jevopi.jetex.ProcessorState#getCatcodeMap()
	 */
	public CatcodeMap getCatcodeMap() {
		return state.getCatcodeMap();
	}

	public abstract TexLocation getLocation();

}
