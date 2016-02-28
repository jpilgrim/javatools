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
package de.jevopi.jetex.latex.latexcmds;

import de.jevopi.jetex.Command;

public abstract class LatexCommand extends Command {

	final String name;

	public LatexCommand() {
		name = Character.toLowerCase(getClass().getSimpleName().charAt(0)) + getClass().getSimpleName().substring(1);
	}

	@Override
	public String getName() {
		return name;
	}

}
