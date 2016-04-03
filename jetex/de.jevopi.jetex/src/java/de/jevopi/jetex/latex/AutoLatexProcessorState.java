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
package de.jevopi.jetex.latex;

import java.util.HashSet;
import java.util.Set;

import de.jevopi.jetex.Command;
import de.jevopi.jetex.latex.undefsupport.UndefinedCommand;
import de.jevopi.jetex.latex.undefsupport.UndefinedEnvironment;

/**
 * Latex processor state that accepts undefined commands and environments. If a command or
 * environment is not defined, an undefined proxy is created.
 */
public class AutoLatexProcessorState extends LatexProcessorState {
	
	final ElementsStatistics<Command> commandStatistics = new ElementsStatistics<>();
	final ElementsStatistics<LatexEnvironment> environmentStatistics = new ElementsStatistics<>();
	
	final Set<UndefinedCommand> undefinedCommands = new HashSet<>();
	final Set<UndefinedEnvironment> undefinedEnvironments = new HashSet<>();
	
	public Command getCommand(String sequence) {
		Command cmd = super.getCommand(sequence);
		if (cmd==null) {
			cmd = new UndefinedCommand(sequence);
			undefinedCommands.add((UndefinedCommand) cmd);
			addMacro(cmd);
		}
		commandStatistics.inc(cmd);
		return cmd;
	}

	public LatexEnvironment getEnvironment(String name) {
		LatexEnvironment env = super.getEnvironment(name);
		if (env==null) {
			env = new UndefinedEnvironment(name);
			undefinedEnvironments.add((UndefinedEnvironment) env);
			addEnvironment(env);
		}
		environmentStatistics.inc(env);
		return env;
	}
	
}
