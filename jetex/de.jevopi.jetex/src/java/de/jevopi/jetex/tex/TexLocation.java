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
package de.jevopi.jetex.tex;

/**
 * Location in input, used for error messages.
 */
public class TexLocation {
	
	int lineNumber, absOffset, lineOffset;
	String fileName;

	public TexLocation(int absOffset, int lineNumber, int lineOffset) {
		this(absOffset, lineNumber,lineOffset, null);
	}
	
	
	public TexLocation(int absOffset, int lineNumber, int lineOffset, String fileName) {
		this.absOffset = absOffset;
		this.lineNumber = lineNumber;
		this.lineOffset = lineOffset;
		this.fileName = fileName;
	}
	
	@Override
	public String toString() {
		StringBuilder strb = new StringBuilder();
		if (fileName!=null && ! fileName.isEmpty()) {
			strb.append(fileName).append(':');
		}
		strb.append(lineNumber).append(',').append(lineOffset);
		return strb.toString();
	}
	
	
}
