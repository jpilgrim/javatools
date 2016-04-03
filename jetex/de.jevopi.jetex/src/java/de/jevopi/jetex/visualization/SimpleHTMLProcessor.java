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
package de.jevopi.jetex.visualization;

import java.util.Iterator;

import de.jevopi.jetex.tex.tokens.Token;

public class SimpleHTMLProcessor extends TextProcessor {

	protected boolean sub = false;
	protected boolean sup = false;
	private boolean math = false;

	public SimpleHTMLProcessor(Iterator<Token> tokens) {
		super(tokens);
	}
	
	@Override
	public String next() {
		String n = super.next();
		if (sub) {
			sub = false;
			return "<sub>"+n+"</sub>";
		}
		if (sup) {
			sup = false;
			return "<sup>"+n+"</sup>";
		}
		return n;
	}

	@Override
	protected String space(Token token) {
		return " ";
	}

	@Override
	protected String other(Token token) {
		return token.rawValue();
	}

	@Override
	protected String letter(Token token) {
		return token.rawValue();
	}

	@Override
	protected String comment(Token token) {
		String comment = token.rawValue().substring(1).trim();
		if (comment.isEmpty()) {
			return null;
		}
		return "<!-- " + comment + " -->\n";
	}

	@Override
	protected String eol(Token token) {
		return "\n";
	}

	@Override
	protected String nbsp(Token token) {
		return "&nbsp;";
	}
	
	@Override
	protected String sub(Token token) {
		sub = true;
		return null;
	}
	
	@Override
	protected String sup(Token token) {
		sup = true;
		return null;
	}
	
	@Override
	protected String mathshift(Token token) {
		math = !math;
		if (math) {
			return mathStart();
		} else {
			return mathEnd();
		}
	}

	protected String mathStart() {
		return "<i>";
	}

	protected String mathEnd() {
		return "</i>";
	}

	

}
