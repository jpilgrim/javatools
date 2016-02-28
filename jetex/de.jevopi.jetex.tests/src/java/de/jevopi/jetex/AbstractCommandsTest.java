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

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameter;

import de.jevopi.jetex.latex.LatexToText;

public abstract class AbstractCommandsTest {
	
	protected static Collection<Object[]> testFiles(String testfoldername) {
		File testfolder = new File(testfoldername);
		List<Object[]> args = new ArrayList<>();
		for (File f : testfolder.listFiles((f) -> {
			return f.isFile() && f.getName().endsWith(".tex.test");
		})) {
			args.add(new Object[] { f });
		}
		;
		return args;
	}
    
    @Parameter // first data value (0) is default
    public File testFile;
    
    @Test
    public void testExpansion() throws IOException {
    	String texAndExpectation = new String(Files.readAllBytes(testFile.toPath()), Charset.forName("UTF-8"));
    	String[] fixture = split(texAndExpectation);
    	String actual = process(fixture[0], testFile.getName());
    	assertEquals(fixture[1], actual);
    	
    }

    
    public String process(String input, String filename) {
    	LatexToText latexToText = new LatexToText();
    	return latexToText.transform(input, filename);
	}


	private String[] split(String texAndExpectation) {
		String[] split = texAndExpectation.split("%\\s+-+\n");
		assertEquals("Wrong test file format", 2, split.length);
		return new String[] { split[0].trim(), split[1].trim() };
	}

}
