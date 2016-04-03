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
package de.jevopi.jetex;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.jevopi.jetex.latex.LatexToHTML;

@RunWith(Parameterized.class)
public class SimpleHTMLTest extends AbstractCommandsTest {
	@Parameters(name = "{0}")
    public static Collection<Object[]> testFiles() {
    	return testFiles("simpleHTML");
    }
    
    @Override
    protected LatexToHTML createLatexTransformer() {
		 return new LatexToHTML();
	}
    
    public void testExpansion() throws IOException {
    	String texAndExpectation = new String(Files.readAllBytes(testFile.toPath()), Charset.forName("UTF-8"));
    	String[] fixture = split(texAndExpectation);
    	LatexToHTML latexToHTML = createLatexTransformer();
    	String actual = latexToHTML.transform(fixture[0], testFile.getName());
    	assertEquals(fixture[1], actual);
    	if (fixture.length>2) {
    		String autoTemplate = latexToHTML.getUndefinedElementsTemplate();
    		assertEquals(fixture[2], autoTemplate);
    	}
    }

    
}
