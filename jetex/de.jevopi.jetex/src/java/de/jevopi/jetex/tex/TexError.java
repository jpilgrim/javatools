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
 * Base class for all errors, includes location of error.
 */
public abstract class TexError extends RuntimeException {

	private static final long serialVersionUID = 5187219244044151606L;

	TexLocation location;

	String errorMsg;

	String errorCode;

	public TexError(TexLocation location, String errorMsg, String errorCode) {
		this.location = location;
		this.errorMsg = errorMsg;
		this.errorCode = errorCode;
	}

	@Override
	public String getMessage() {
		return errorCode + " error: " + errorMsg + " (" + location + ")";
	}

}
