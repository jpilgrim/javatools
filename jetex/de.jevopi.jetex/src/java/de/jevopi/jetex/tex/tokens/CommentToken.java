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

import de.jevopi.jetex.tex.Category;

public class CommentToken extends Token {

	String comment;

	public CommentToken(String comment) {
		this.comment = comment;
	}

	@Override
	public Category getCategory() {
		return Category.COMMENT;
	}

	@Override
	public String rawValue() {
		return "%" + comment;
	}

	@Override
	public String toString() {
		return "CMT(" + comment + ")";
	}

}
