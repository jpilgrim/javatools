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
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import de.jevopi.jetex.SyntaxError;
import de.jevopi.jetex.tex.Category;
import de.jevopi.jetex.tex.expansion.ExpansionError;

public class TokenIterators {

	/**
	 * Returns next control sequence
	 */
	public static String getCSName(ITokenIterator tokens) {
		Token t = tokens.next();
		if (t instanceof ControlSequence) {
			return ((ControlSequence) t).getSequence();
		}
		throw new ExpansionError(tokens.getLocation(), "Expected Control Sequence, was " + t);
	}

	/**
	 * Returns next string, i.e. all letters and other encapsulated in a group;
	 * or single next letter. If other characters occur, an error is thrown.
	 */
	public static String getGroupedString(ITokenIterator tokens) {
		Token t = tokens.next();
		switch (t.getCategory()) {
		case BEGINGROUP:
			List<Token> group = TokenIterators.fetchGroup(tokens);
			StringBuilder strb = new StringBuilder();
			for (Token l : group) {
				if (l.getCategory() == Category.LETTER || l.getCategory() == Category.OTHER) {
					strb.append(l.rawValue());
				} else {
					throw new ExpansionError(tokens.getLocation(), "Expected letter or other, found " + l);
				}
			}
			return strb.toString();
		case LETTER:
			return t.rawValue();
		default:
			throw new ExpansionError(tokens.getLocation(), "Expected string in group or single letter " + t);
		}
	}

	/**
	 * Returns optional with number (i.e. digits which are consumed), or empty
	 * optional if no number has been found. single characters can be used as 
	 * numbers, in that case "'" (and optional "\") has to preceed the character.
	 */
	public static Optional<Integer> getNumber(ITokenIterator tokens) {
		StringBuilder strb = new StringBuilder();
		while (tokens.hasNext()) {
			Token t = tokens.peek();
			if (t.getCategory() == Category.OTHER && Character.isDigit(t.rawValue().charAt(0))) {
				tokens.next();
				strb.append(t.rawValue());
			} else {
				break;
			}
		}
		if (strb.length() != 0) {
			return Optional.of(Integer.parseInt(strb.toString()));
		}
		if (next(tokens, '\'')) {
			Token t = tokens.next();
			String s = t.rawValue();
			char c;
			if (s.length()==2 && s.charAt(0)=='\\') {
				tokens.next(); // consume "sequence"
				c = s.charAt(1);
			} else if (s.length()==1) {
				c = s.charAt(0);
			} else {
				throw new SyntaxError(tokens.getLocation(), "Expected number or 'character");
			}
			return Optional.of((int)c); 		
		}
		return Optional.empty();
	}

	/**
	 * Returns list of undelimited arguments, used by macros to retrieve
	 * arguments.
	 */
	public static ArrayList<Token> getUndelimitedArg(ITokenIterator tokens) {
		ArrayList<Token> arg;
		arg = new ArrayList<>();
		while (tokens.hasNext() && tokens.peek().getCategory() == Category.SPACE) {
			tokens.next();
		}
		if (tokens.hasNext()) {
			Token t = tokens.next();
			if (t.getCategory() == Category.BEGINGROUP) {
				arg.addAll(fetchGroup(tokens));
			} else {
				arg.add(t);
			}
		}
		return arg;
	}

	/**
	 * Returns delimited args, used by macros to retrieve arguments.
	 */
	public static ArrayList<Token> getDelimitedArg(ITokenIterator tokens, List<Token> delimiters) {
		ArrayList<Token> arg;
		arg = new ArrayList<>();
		int delimiterIndex = 0;
		while (tokens.hasNext()) {
			Token t = tokens.next();
			if (t.getCategory() == Category.BEGINGROUP) {
				arg.addAll(fetchGroup(tokens));
				delimiterIndex = 0;
			} else {
				arg.add(t);
			}
			if (t.equals(delimiters.get(delimiterIndex))) {
				delimiterIndex++;
			} else {
				delimiterIndex = 0;
			}

			if (delimiterIndex == delimiters.size()) {
				for (int i = 0; i < delimiters.size(); i++) {
					arg.remove(arg.size() - 1);
				}
				break;
			}
		}
		return arg;
	}

	/**
	 * Returns collection of all elements until end of group without expanding
	 * (nested groups are added as well). The begin group token is expected to
	 * be consumed before. The end group token is consumed but not added to the
	 * returned collection.
	 */
	public static List<Token> fetchGroup(Iterator<Token> tokens) {
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

	/**
	 * Consumes «equals». Equal sign is optional in assignments, thus, optional
	 * spaces and an optional equal sign are consumed. Returns true if at least
	 * one space or the equals character have been consumed.
	 */
	public static boolean consumeEquals(ITokenIterator tokens) {
		boolean found = consumeOptionalSpaces(tokens);
		if (tokens.hasNext() && "=".equals(tokens.peek().rawValue())) {
			tokens.next();
			found = true;
		}
		return consumeOptionalSpaces(tokens) | found;
	}

	/**
	 * Consumes following 0 to n spaces. Returns true if at least a single space
	 * has been consumed.
	 */
	public static boolean consumeOptionalSpaces(ITokenIterator tokens) {
		boolean found = false;
		if (tokens.hasNext() && tokens.peek().getCategory() == Category.SPACE) {
			tokens.next();
			found = true;
		}
		return found;
	}
	
	/**
	 * Consumes following category character. Returns true such a character was found.
	 */
	public static boolean next(IExpandableTokenIterator tokens, Category category) {
		if (tokens.hasNext() && tokens.peek().getCategory() == category) {
			tokens.next();
			return true;
		}
		return false;
		
	}

	/**
	 * Consumes next required character. If not found, and an exception is
	 * thrown.
	 */
	public static void consumeCharacter(ITokenIterator tokens, char character) {
		Token t = tokens.next();
		if (t == null) {
			throw new ExpansionError(tokens.getLocation(), "Expected '" + character + "', EOF found");
		}
		if (t.rawValue().length() != 1 || t.rawValue().charAt(0) != character) {
			throw new ExpansionError(tokens.getLocation(), "Expected '" + character + "', found '" + t.rawValue() + "'");
		}
	}

	/**
	 * If next token equals given character, true is returned and character is
	 * consumed. Otherwise, false is returned.
	 */
	public static boolean next(ITokenIterator tokens, char character) {
		Token t = tokens.peek();
		if (t != null && t.rawValue().length() != 1 || t.rawValue().charAt(0) != character) {
			tokens.next();
			return true;
		}
		return false;
	}

	/**
	 * Return tokens inside a group, if no group is found, an error is issued.
	 */
	public static List<Token> getReplacement(ITokenIterator tokens) {
		Token bg = tokens.next();
		if (bg.getCategory() == Category.BEGINGROUP) {
			return TokenIterators.fetchGroup(tokens);
		}
		throw new ExpansionError(tokens.getLocation(), "Didn't found group");

	}

	


}
