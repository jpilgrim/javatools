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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import de.jevopi.jetex.ProcessorState;
import de.jevopi.jetex.latex.LatexEnvironment.EnvironmentStatus;
import de.jevopi.jetex.latex.latexcmds.LatexCommand;
import de.jevopi.jetex.tex.tokens.Token;

public class LatexProcessorState extends ProcessorState {

	private final Stack<EnvironmentStatus> environmentStack = new Stack<>();
	private final Map<String, LatexEnvironment> environments = new HashMap<>();

	public LatexProcessorState() {
		try {
			initLatexCommands();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	/**
	 * Cast helper.
	 */
	public static LatexProcessorState cast(ProcessorState state) {
		return (LatexProcessorState) state;
	}

	private void initLatexCommands() throws IOException, InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		Class<LatexCommand> classLC = LatexCommand.class;
		loadAndInitBuiltinCommands(classLC);
	}
	
	public void beginEnvironment(EnvironmentStatus envStatus) {
		environmentStack.push(envStatus);
	}
	
	public EnvironmentStatus endEnvironment() {
		return environmentStack.pop();
	}
	
	public void addEnvironment(LatexEnvironment env) {
		environments.put(env.getName(), env);
	}
	
	public LatexEnvironment getEnvironment(String name) {
		return environments.get(name);
	}

}
