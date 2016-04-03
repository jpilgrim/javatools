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

import java.util.Iterator;

import de.jevopi.jetex.Command;
import de.jevopi.jetex.ProcessorState;
import de.jevopi.jetex.tex.tokens.ITokenIterator;
import de.jevopi.jetex.tex.tokens.Token;


/**
 * Undefined commands, used as a proxy once it has been requested.
 */
public class UndefinedCommand extends Command {

	final String name;
	
	int estimatedMandatoryParameters = 0;
	int estimatedOptionalParameters = 0;

	public UndefinedCommand(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
		
	@Override
	public Iterator<Token> expand(ProcessorState state, ITokenIterator tokens) {
		EstimatedArguments args = EstimatedArguments.getEstimatedArguments(tokens);
		estimatedOptionalParameters = Math.max(estimatedOptionalParameters, args.estimatedOptionalArgs());
		estimatedMandatoryParameters = Math.max(estimatedMandatoryParameters, args.estimatedMandatoryArgs());
		return args;
	}

	@Override
	public boolean similar(Command c) {
		return c==this;
	}

	public String definitionTemplate() {
		StringBuilder strb = new StringBuilder();
		strb.append("\\newcommand\\").append(name);
		if (estimatedOptionalParameters+estimatedMandatoryParameters>0) {
			strb.append("[").append(estimatedOptionalParameters+estimatedMandatoryParameters).append("]");
		}
		for (int i=1; i<=estimatedOptionalParameters; i++) {
			strb.append("[]");
		}
		strb.append("{");
		for (int i=1; i<=estimatedOptionalParameters+estimatedMandatoryParameters; i++) {
			strb.append("#").append(i);
		}
		strb.append("}%");
		return strb.toString();
	}
	
}
