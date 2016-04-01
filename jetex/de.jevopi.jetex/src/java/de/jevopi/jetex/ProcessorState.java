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
package de.jevopi.jetex;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;

import de.jevopi.jetex.tex.CatcodeMap;
import de.jevopi.jetex.tex.primitives.PrimitiveCommand;

/**
 * Internal state of whole processor.
 */
public class ProcessorState {
	final CatcodeMap catcodeMap;

	final Map<String, Command> commands = new HashMap<>();

	boolean inhibitExpansion = false;

	/**
	 * Stack of condition results, used by else and fi to determine whether to
	 * skip preceding tokens (or throw an error).
	 */
	final Stack<ConditionResult> conditionResults = new Stack<>();

	public ProcessorState() {
		catcodeMap = new CatcodeMap();
		try {
			initPrimitiveCommands();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	private void initPrimitiveCommands() throws IOException, InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		Class<PrimitiveCommand> classPC = PrimitiveCommand.class;
		loadAndInitBuiltinCommands(classPC);
	}

	protected void loadAndInitBuiltinCommands(Class<? extends Command> commandBaseClass) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		ClassLoader cl = commandBaseClass.getClassLoader();
		String pckgName = commandBaseClass.getPackage().getName();
		String path = pckgName.replace('.', '/');
		URL url = cl.getResource(path);
		File primitivesClassDir = new File(url.getFile());
		List<String> classNames = new ArrayList<>();
		for (File f : primitivesClassDir.listFiles()) {
			if (f.isFile()) {
				String fileName = f.getName();
				if (fileName.endsWith(".class")) {
					classNames.add(fileName.substring(0, fileName.length() - 6));
				}
			}
		}
		for (String className : classNames) {
			Class<?> c = Class.forName(pckgName + "." + className);
			if ((c.getModifiers() & (Modifier.ABSTRACT | Modifier.INTERFACE)) == 0 && Command.class.isAssignableFrom(c)) {
				Command pc = (Command) c.newInstance();
				commands.put(pc.getName(), pc);
			}
		}
	}

	public CatcodeMap getCatcodeMap() {
		return catcodeMap;
	}

	public Command getCommand(String sequence) {
		Command cmd = commands.get(sequence);
		return cmd;
	}

	public void addMacro(Command macro) {
		commands.put(macro.getName(), macro);
	}

	/**
	 * For testing only.
	 */
	public Collection<Command> getCommands() {
		return commands.values();
	}

	public void addTokenAssignment(TokenAssignment tokenAssignment) {
		String seq = tokenAssignment.token.getSequence();
		if (seq != null) {
			Command cmd = getCommand(seq);
			if (cmd == null) {
				throw new IllegalArgumentException("Did not found command " + seq);
			}
			commands.put(tokenAssignment.name, cmd);
			return;
		}

		commands.put(tokenAssignment.name, tokenAssignment);

	}

	public void inhibitExpansion() {
		inhibitExpansion = true;
	}

	public void allowExpension() {
		inhibitExpansion = false;
	}

	public boolean isExpansionInhibited() {
		return inhibitExpansion;
	}

	public void pushConditionResult(ConditionResult conditionResult) {
		conditionResults.push(conditionResult);
	}

	public Optional<ConditionResult> popConditionResult() {
		if (conditionResults.empty()) {
			return Optional.empty();
		}
		return Optional.of(conditionResults.pop());
	}

	public Optional<ConditionResult> peekConditionResult() {
		if (conditionResults.empty()) {
			return Optional.empty();
		}
		return Optional.of(conditionResults.peek());
	}

}
