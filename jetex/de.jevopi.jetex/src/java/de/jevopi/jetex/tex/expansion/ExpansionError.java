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
package de.jevopi.jetex.tex.expansion;

import de.jevopi.jetex.tex.TexError;
import de.jevopi.jetex.tex.TexLocation;

public class ExpansionError extends TexError {

	private static final long serialVersionUID = 7095284090453646386L;

	public ExpansionError(TexLocation location, String errorMsg) {
		super(location, errorMsg, "expansion");
	}

}
