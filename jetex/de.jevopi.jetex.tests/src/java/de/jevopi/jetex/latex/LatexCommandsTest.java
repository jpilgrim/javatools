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

import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.jevopi.jetex.AbstractCommandsTest;

@RunWith(Parameterized.class)
public class LatexCommandsTest extends AbstractCommandsTest {
	@Parameters(name = "{0}")
    public static Collection<Object[]> testFiles() {
    	return testFiles("latexBuiltin");
    }
    
}
