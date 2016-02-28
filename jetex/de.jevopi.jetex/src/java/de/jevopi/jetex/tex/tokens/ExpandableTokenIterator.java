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
package de.jevopi.jetex.tex.tokens;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

import de.jevopi.jetex.tex.Category;
import de.jevopi.jetex.tex.TexLocation;
import de.jevopi.jetex.tex.lexer.InputProcessor;

public class ExpandableTokenIterator implements IExpandableTokenIterator {

	Stack<Iterator<Token>> tokens = new Stack<>();

	public ExpandableTokenIterator() {
	}

	public void add(Iterator<Token> expansion) {
		tokens.push(expansion);
	}

	@Override
	public Token next() {
		while (!tokens.isEmpty()) {
			Iterator<Token> iter = tokens.peek();
			if (iter.hasNext()) {
				return iter.next();
			} else {
				tokens.pop();
			}
		}
		throw new NoSuchElementException();
	}

	@Override
	public boolean hasNext() {
		while (!tokens.isEmpty()) {
			Iterator<Token> iter = tokens.get(tokens.size() - 1);
			if (iter.hasNext()) {
				return true;
			} else {
				tokens.remove(tokens.size() - 1);
			}
		}
		return false;
	}

	/**
	 * Similar to peek(0), peeks the next token in the input stream
	 */
	@Override
	public Token peek() {
		Token next = next();
		tokens.add(next.iterator());
		return next;
	}

	/**
	 * Returns the n-th token ahead without removing it, 0 is the next token.
	 */
	@Override
	public Token peek(int lookAhead) {
		if (lookAhead == 0) {
			return peek();
		}
		List<Token> peeked = new ArrayList<>(lookAhead + 1);
		Token next = null;
		while (lookAhead >= 0) {
			next = next();
			peeked.add(next);
			lookAhead--;
		}
		tokens.add(peeked.iterator());
		return next;
	}

	/**
	 * Returns collection of all elements until end of group without expanding
	 * (nested groups are added as well). The begin group token is expected to
	 * be consumed before. The end group token is consumed but not added to the
	 * returned collection.
	 */
	public static Collection<Token> fetchGroup(Iterator<Token> tokens) {
		int depth = 0;
		List<Token> elements = new ArrayList<>();
		do {
			Token s = tokens.next();
			if (s.getCategory() == Category.BEGINGROUP) {
				depth++;
			}
			if (s.getCategory() == Category.ENDGROUP) {
				depth--;
			}
			if (depth >= 0) {
				elements.add(s);
			}
		} while (depth >= 0);
		return elements;
	}

	@Override
	public TexLocation getLocation() {
		if (tokens.isEmpty()) {
			return null;
		}
		Object first = tokens.get(0);
		if (first instanceof InputProcessor) {
			return ((InputProcessor) first).getLocation();
		}
		if (first instanceof ExpandableTokenIterator) {
			return ((ITokenIterator) first).getLocation();
		}
		return null;
	}

}
