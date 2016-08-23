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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.jevopi.jetex.latex.LatexToHTML;
import de.jevopi.jetex.tex.lexer.InputSource;

public class Jetex {

	public static void main(String[] args) throws IOException {
		try {
			Jetex app = new Jetex();
			int res = app.parseArgs(args);
			if (res != 0) {
				System.exit(res);
			}
			app.run();
			System.out.println("Done.");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(3);
			
		}
	}

	private String autoDefFile;

	private List<String> inputFile = new ArrayList<>();

	private String outFile;

	private boolean enforce = false;

	private int parseArgs(String[] args) {
		int i = 0;
		try {
			while (i < args.length) {
				String s = args[i++];
				if ("-a".equals(s)) {
					autoDefFile = nextArg(i++, args, "Filen for automatic generated macros expected");
				} else if ("-o".equals(s)) {
					outFile = nextArg(i++, args, "Output file expected");
				} else if ("-f".equals(s)) {
					enforce = true;
				} else {
					inputFile.add(s);
				}
			}
		} catch (IllegalArgumentException ex) {
			printHelp();
			return 1;
		}
		if (!check()) {
			printHelp();
			return 1;
		}
		return 0;
	}

	private boolean check() {
		for (String s : inputFile) {
			if (!new File(s).exists()) {
				System.err.println("input file " + s + " not found");
				return false;
			}

		}
		if (inputFile.isEmpty()) {
			System.err.println("at least one input file required");
			return false;
		}
		if (autoDefFile != null) {
			if (!enforce && new File(autoDefFile).exists()) {
				System.err.println("initDefFile already exist");
				return false;
			}
		}
		if (outFile != null) {
			if (!enforce && new File(outFile).exists()) {
				System.err.println("output file already exist");
				return false;
			}
		}
		return true;
	}

	private void printHelp() {
		System.err.flush();
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
		}
		System.out.println("jetex [-a autoDefFile] [-f] -o outfile inputfile_1 [... inputfile_n]\n" + "with:\n"
				+ "-a autoDefFile: Creates an file with all (unknown) macros used in given texfile\n"
				+ "-f force overwriting of autoDefFile and output file/n" + "-o outfile: Output file");
	}

	String nextArg(int i, String[] args, String errorMessage) {
		if (i >= args.length) {
			System.err.println(errorMessage);
			throw new IllegalArgumentException();
		}
		return args[i];
	}

	void run() throws IOException {
		LatexToHTML latexToHTML = new LatexToHTML();
		File[] files = inputFile.stream().map(s -> new File(s)).collect(Collectors.toList()).toArray(new File[inputFile.size()]);
		String out = latexToHTML.transform(files);
		FileOutputStream fos = new FileOutputStream(new File(outFile));
		fos.write(out.getBytes(InputSource.UTF8));
		fos.close();
		if (autoDefFile!=null) {
			String s = latexToHTML.getUndefinedElementsTemplate();
			if (! s.isEmpty()) {
				fos = new FileOutputStream(new File(autoDefFile));
				fos.write(s.getBytes(InputSource.UTF8));
				fos.close();
			}
		}
	}

}
