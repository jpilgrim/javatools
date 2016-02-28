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

import java.util.Collections;
import java.util.Iterator;

import de.jevopi.jetex.tex.tokens.IExpandableTokenIterator;
import de.jevopi.jetex.tex.tokens.ITokenIterator;
import de.jevopi.jetex.tex.tokens.Token;

/**
 * Base class for primitive commands, user defined macros etc.
 */
public abstract class Command {

	public final static Iterator<Token> SKIP = Collections.<Token> emptyList().iterator();

	
	public abstract String getName();

	/**
	 * Expands the command. If command is not expandable, null is returned.
	 * Note that returning null is different from SKIP, since in the latter case the 
	 * command sequence is skipped as well.
	 */
	public Iterator<Token> expand(ProcessorState state, ITokenIterator tokens) {
		return null;
	}
	
	/**
	 * Executes the command. 
	 */
	public void execute(ProcessorState state, IExpandableTokenIterator tokenIterator) {
	}

}
