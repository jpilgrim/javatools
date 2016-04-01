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
package de.jevopi.jetex.tex.expansion;

import java.util.Iterator;
import java.util.NoSuchElementException;

import de.jevopi.jetex.AbstractProcessor;
import de.jevopi.jetex.Command;
import de.jevopi.jetex.ProcessorState;
import de.jevopi.jetex.tex.TexLocation;
import de.jevopi.jetex.tex.lexer.InputProcessor;
import de.jevopi.jetex.tex.tokens.ExpandableTokenIterator;
import de.jevopi.jetex.tex.tokens.IExpandableTokenIterator;
import de.jevopi.jetex.tex.tokens.Token;

public class ExpansionProcessor extends AbstractProcessor implements IExpandableTokenIterator {

	final ExpandableTokenIterator inputTokens;

	final InputProcessor inputProcessor;

	/**
	 * @param inputTokens usually the input processor
	 */
	public ExpansionProcessor(ProcessorState state, InputProcessor inputProcessor) {
		super(state);
		this.inputProcessor = inputProcessor;
		this.inputTokens = new ExpandableTokenIterator();
		this.inputTokens.add(inputProcessor);
	}

	@Override
	public TexLocation getLocation() {
		return inputProcessor.getLocation();
	}

	@Override
	public boolean hasNext() {
		return inputTokens.hasNext();
	}

	@Override
	public Token next() {
		while (inputTokens.hasNext()) {
			Token token = inputTokens.next();
			if (canExpand(token)) {
				Iterator<Token> expansion = expandToken(token);
				if (expansion != null) {
					inputTokens.add(expansion);
					return next();
				}
			}
			return token;
		}
		throw new NoSuchElementException();
	}

	@Override
	public Token peek() {
		return inputTokens.peek();
	}

	@Override
	public Token peek(int lookAhead) {
		return inputTokens.peek(lookAhead);
	}

	private boolean canExpand(Token token) {
		return !state.isExpansionInhibited() && token.isExpandable();
	}

	private Iterator<Token> expandToken(Token token) {
		String seq = token.getSequence();
		if (seq != null) {
			Command cmd = state.getCommand(seq);
			if (cmd == null) {
				throw new ExpansionError(getLocation(), "Undefined control sequence '" + seq + "'.");
			}
			Iterator<Token> res = cmd.expand(state, this);
			return res;
		}
		return null;
	}

	@Override
	public void add(Iterator<Token> expansion) {
		inputTokens.add(expansion);
	}

}
