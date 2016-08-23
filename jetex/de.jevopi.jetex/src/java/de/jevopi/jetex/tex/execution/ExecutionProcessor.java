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

package de.jevopi.jetex.tex.execution;

import java.util.Iterator;

import de.jevopi.jetex.AbstractProcessor;
import de.jevopi.jetex.Command;
import de.jevopi.jetex.ProcessorState;
import de.jevopi.jetex.TokenAssignment;
import de.jevopi.jetex.tex.TexLocation;
import de.jevopi.jetex.tex.expansion.ExpansionProcessor;
import de.jevopi.jetex.tex.tokens.IExpandableTokenIterator;
import de.jevopi.jetex.tex.tokens.Token;

public class ExecutionProcessor extends AbstractProcessor implements IExpandableTokenIterator {
	
	private final ExpansionProcessor expansionProcessor;

	public ExecutionProcessor(ProcessorState state, ExpansionProcessor expansionProcessor) {
		super(state);
		this.expansionProcessor = expansionProcessor;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public TexLocation getLocation() {
		return expansionProcessor.getLocation();
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		return expansionProcessor.hasNext();
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public Token next() {
		Token token = expansionProcessor.next();
		String sequence = token.getSequence();
		if (sequence!=null) {
			Command cmd = state.getCommand(sequence);
			if (cmd==null) {
				throw new ExecutionError(getLocation(), "Command " + sequence + " not defined.");
			}
			cmd.execute(state, expansionProcessor);
		}
		return token;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public Token peek() {
		return expansionProcessor.peek();
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public Token peek(int lookAhead) {
		return expansionProcessor.peek(lookAhead);
	}
	
	@Override
	public void add(Iterator<Token> expansion) {
		expansionProcessor.add(expansion);
		
	}

	/**
	 * @param macro
	 */
	public void addMacro(Command macro) {
		state.addMacro(macro);
	}

	/**
	 * @param tokenAssignment
	 */
	public void addTokenAssignment(TokenAssignment tokenAssignment) {
		state.addTokenAssignment(tokenAssignment);
	}

	
}
