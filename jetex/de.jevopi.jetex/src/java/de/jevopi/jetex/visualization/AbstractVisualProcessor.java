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
package de.jevopi.jetex.visualization;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import de.jevopi.jetex.IProcessorStateListener;
import de.jevopi.jetex.latex.LatexProcessorState;
import de.jevopi.jetex.tex.Category;
import de.jevopi.jetex.tex.tokens.IExpandableTokenIterator;
import de.jevopi.jetex.tex.tokens.Token;

public abstract class AbstractVisualProcessor implements Iterator<String>, IProcessorStateListener {

	final IExpandableTokenIterator tokens;
	LatexProcessorState state;

	String next;
	String nextBuffer;
	

	public AbstractVisualProcessor(LatexProcessorState state, IExpandableTokenIterator tokens) {
		this.tokens = tokens;
		this.state = state;
		state.registerProcessorStateListener(this);
		fetchNext();
	}

	private void fetchNext() {
		next = null;
		
		if (nextBuffer!=null) {
			next = nextBuffer;
			nextBuffer = null;
			return;
		}
		
		while (tokens.hasNext()) {
			Token token = tokens.next();
			state.handleModeSwitching(token);
			switch (token.getCategory()) {// @formatter:off
			case ACTIVE: next = nbsp(token); break;
			case EOL: next = eol(token); break;
			case COMMENT: next = comment(token); break;
			case LETTER: next = letter(token) ; break;
			case OTHER: next = other(token); break;
			case SPACE: next = space(token); 
				if (tokens.peek()!=null && tokens.peek().getCategory()==Category.EOL) {
					next = null;
				}
			break;
			case SUB: next = sub(token); break;
			case SUPER: next = sup(token); break;
			case MATHSHIFT: next = mathshift(token); break;
			case TAB: next = tab(token); break;
			default: // do nothing;
			} // @formatter:on
			if (next != null) {
				if (nextBuffer != null) {
					// this probably should be handled internally...
					String s = (token.getCategory()==Category.SPACE) ? null: next; 
					next = nextBuffer;
					nextBuffer = s;
				}
				break;
			}
		}
		if (next==null && !tokens.hasNext()) { // this is maybe a hack here...
			state.enforceVerticalMode();
			if (nextBuffer!=null) {
				next = nextBuffer;
				nextBuffer = null;
				return;
			}
		}
	}

	

	protected String tab(Token token) {
		return null;
	}

	protected String mathshift(Token token) {
		return null;
	}

	protected String sup(Token token) {
		return null;
	}

	protected String sub(Token token) {
		return null;
	}

	protected abstract String space(Token token);

	protected abstract String other(Token token);

	protected abstract String letter(Token token);

	protected abstract String comment(Token token);

	protected abstract String eol(Token token);

	protected abstract String nbsp(Token token);
	
	@Override
	public void handleParagraphStart() {
		nextBuffer = startParagraph(); 
		
	}
	
	protected String startParagraph() {
		return "";
	}

	@Override
	public void handleParagraphEnd() {
		nextBuffer = endParagraph();
	}
	
	protected String endParagraph() {
		return "";
	}

	@Override
	public boolean hasNext() {
		return next != null;
	}

	@Override
	public String next() {
		String s = next;
		fetchNext();
		return s;
	}

	
	}
