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

public class ControlWord extends ControlSequence {
	String word;

	public ControlWord(StringBuilder word) {
		this.word = word.toString();
	}

	@Override
	public String toString() {
		return "\\" + word;
	}

	@Override
	public String rawValue() {
		return toString();
	}

	@Override
	public String getSequence() {
		return word;
	}

}
