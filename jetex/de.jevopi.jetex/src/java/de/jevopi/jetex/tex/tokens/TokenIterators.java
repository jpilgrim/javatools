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
import java.util.List;

import de.jevopi.jetex.tex.Category;

public class TokenIterators {

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
				arg.addAll(ExpandableTokenIterator.fetchGroup(tokens));
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
				arg.addAll(ExpandableTokenIterator.fetchGroup(tokens));
				delimiterIndex = 0;
			} else {
				arg.add(t);
			}
			if (t.equals(delimiters.get(delimiterIndex))) {
				delimiterIndex++;
			} else {
				delimiterIndex = 0;
			}
			
			if (delimiterIndex==delimiters.size()) {
				for (int i=0; i<delimiters.size(); i++) {
					arg.remove(arg.size()-1);
				}
				break;
			} 
		}
		return arg;
	}


}
