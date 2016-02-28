/**
 * Copyright (c) 2016 Jens von Pilgrim
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *   Jens von Pilgrim - Initial API and implementation
 */
package de.jevopi.jetex;

import de.jevopi.jetex.tex.TexError;
import de.jevopi.jetex.tex.TexLocation;

public class MissingImplementationError extends TexError {

	private static final long serialVersionUID = 3509283829434688639L;

	public MissingImplementationError(TexLocation location) {
		super(location, "Required feature not implemented yet", "implementation");
	}

}
