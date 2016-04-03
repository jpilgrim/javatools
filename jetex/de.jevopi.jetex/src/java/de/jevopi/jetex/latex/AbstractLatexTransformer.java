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
package de.jevopi.jetex.latex;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import de.jevopi.jetex.latex.undefsupport.UndefinedCommand;
import de.jevopi.jetex.latex.undefsupport.UndefinedEnvironment;
import de.jevopi.jetex.tex.execution.ExecutionProcessor;
import de.jevopi.jetex.tex.expansion.ExpansionProcessor;
import de.jevopi.jetex.tex.lexer.InputProcessor;
import de.jevopi.jetex.tex.lexer.InputSource;
import de.jevopi.jetex.tex.tokens.Token;
import de.jevopi.jetex.visualization.AbstractVisualProcessor;

public abstract class AbstractLatexTransformer {

	
	private Set<UndefinedCommand> undefinedCommands;
	private Set<UndefinedEnvironment> undefinedEnvironments;

	public String transform(String latexInput) {
		return transform(latexInput, null);
	}
	
	/**
	 * @param latexInput the input as string
	 * @param filename filename, only used for error messages
	 */
	public String transform(String latexInput, String filename) {
		return doTransform(new InputSource(filename, latexInput));
	}
	
	public String transform(File... files) throws IOException {
		InputSource[] inputSources = new InputSource[files.length];
		int i=0;
		for (File file: files) {
			inputSources[i++] = new InputSource(file);
		}
		return doTransform(inputSources);
	}
	
	private String doTransform(InputSource... inputSources) {
		LatexProcessorState state = createLatexProcessorState();
		undefinedCommands = null;
		undefinedEnvironments = null;
		InputProcessor ip = new InputProcessor(state);
		for (InputSource inputSource: inputSources) {
			ip.addInputSource(inputSource);
		}
		ExpansionProcessor ep = new ExpansionProcessor(state, ip);
		ExecutionProcessor ex = new LatexExecutionProcessor(state, ep);
		AbstractVisualProcessor vp = getVisualProcessor(ex);
		StringBuilder strb = new StringBuilder();
		while (vp.hasNext()) {
			strb.append(vp.next());
		}
		
		if (state instanceof AutoLatexProcessorState) {
			undefinedCommands = ((AutoLatexProcessorState) state).undefinedCommands;
			undefinedEnvironments = ((AutoLatexProcessorState) state).undefinedEnvironments;
		}
		
		return strb.toString();
	}

	protected LatexProcessorState createLatexProcessorState() {
		return new LatexProcessorState();
	}
	
	public String getUndefinedElementsTemplate() {
		StringBuilder strb = new StringBuilder();
		if (undefinedCommands!=null) {
			for (UndefinedCommand cmd: undefinedCommands) {
				if (strb.length()>0) {
					strb.append("\n");
				}
				strb.append(cmd.definitionTemplate());
			}
		}
		if (undefinedEnvironments!=null) {
			for (UndefinedEnvironment env: undefinedEnvironments) {
				if (strb.length()>0) {
					strb.append("\n");
				}
				strb.append(env.definitionTemplate());
			}
		}
		return strb.toString();
	}

	abstract protected AbstractVisualProcessor getVisualProcessor(Iterator<Token> tokens);
}
