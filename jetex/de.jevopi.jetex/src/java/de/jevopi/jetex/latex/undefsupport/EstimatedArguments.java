/*
 * Copyright (c) 2016 Jens von Pilgrim
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *   Jens von Pilgrim - Initial API and implementation
 */
package de.jevopi.jetex.latex.undefsupport;

import java.util.ArrayList;
import java.util.List;

import de.jevopi.jetex.latex.LatexTokenIterators;
import de.jevopi.jetex.tex.Category;
import de.jevopi.jetex.tex.tokens.ExpandableTokenIterator;
import de.jevopi.jetex.tex.tokens.ITokenIterator;
import de.jevopi.jetex.tex.tokens.Token;
import de.jevopi.jetex.tex.tokens.TokenIterators;

public class EstimatedArguments extends ExpandableTokenIterator {
	
	/**
	 * Returns list of estimated optional and non-optional arguments;
	 */
	public static EstimatedArguments getEstimatedArguments(ITokenIterator tokens) {
		EstimatedArguments args = new EstimatedArguments();

		while (true) {
			List<Token> arg = LatexTokenIterators.getOptionalArg(tokens);
			if (arg == null) {
				break;
			}
			args.optionalArgs.add(arg);
		}
		while (tokens.hasNext() && tokens.peek().getCategory() == Category.BEGINGROUP) {
			List<Token> arg = TokenIterators.getUndelimitedArg(tokens);
			args.mandatoryArgs.add(arg);
		}
		args.seal();
		return args;
	}
	
	private List<Iterable<Token>> optionalArgs = new ArrayList<>(5);
	private List<Iterable<Token>> mandatoryArgs = new ArrayList<>(5);
	
	public int estimatedOptionalArgs() {
		return optionalArgs.size();
	}
	public int estimatedMandatoryArgs() {
		return mandatoryArgs.size();
	}
	
	
	private void seal() {
		for (int i=mandatoryArgs.size()-1; i>=0; i--) {
			add(mandatoryArgs.get(i).iterator());
		}
		for (int i=optionalArgs.size()-1; i>=0; i--) {
			add(optionalArgs.get(i).iterator());
		}
	}
	
	
	
	
}
