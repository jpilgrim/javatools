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

import java.util.Collections;
import java.util.Iterator;

import de.jevopi.jetex.ProcessorState;
import de.jevopi.jetex.latex.LatexEnvironment;
import de.jevopi.jetex.latex.LatexProcessorState;
import de.jevopi.jetex.tex.tokens.ITokenIterator;
import de.jevopi.jetex.tex.tokens.Token;

public class UndefinedEnvironment extends LatexEnvironment {

	int estimatedMandatoryParameters = 0;
	int estimatedOptionalParameters = 0;
	
	public UndefinedEnvironment(String name) {
		super(name,  0, Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
	}
	
	public Iterator<Token> expandBegin(ProcessorState state, ITokenIterator tokens) {
		EstimatedArguments args = EstimatedArguments.getEstimatedArguments(tokens);
		estimatedOptionalParameters = Math.max(estimatedOptionalParameters, args.estimatedOptionalArgs());
		estimatedMandatoryParameters = Math.max(estimatedMandatoryParameters, args.estimatedMandatoryArgs());
		
		LatexProcessorState.cast(state).beginEnvironment(new EnvironmentStatus(this, Collections.emptyList()));
		return args;
	}

	public Iterator<Token> expandEnd(ProcessorState state, ITokenIterator tokens) {
		LatexProcessorState.cast(state).endEnvironment();
		return null;
	}

	
	public String definitionTemplate() {
		StringBuilder strb = new StringBuilder();
		strb.append("\\newenvironment{").append(getName()).append("}");
		if (estimatedOptionalParameters+estimatedMandatoryParameters>0) {
			strb.append("[").append(estimatedOptionalParameters+estimatedMandatoryParameters).append("]");
		}
		for (int i=1; i<=estimatedOptionalParameters; i++) {
			strb.append("[]");
		}
		strb.append("%\n\t{");
		for (int i=1; i<=estimatedOptionalParameters+estimatedMandatoryParameters; i++) {
			strb.append("#").append(i);
		}
		strb.append("}%\n\t{}%");
		return strb.toString();
		
		
	}

}
