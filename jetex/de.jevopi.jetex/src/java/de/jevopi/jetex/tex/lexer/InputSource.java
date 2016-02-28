package de.jevopi.jetex.tex.lexer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class InputSource {

	final static Charset UTF8 = Charset.forName("UTF-8");

	private CharSequence sequence;

	private String name;

	private int index = 0;

	private int lineNumber = 0;

	private int lineOffset = 0;

	public InputSource(String name, CharSequence sequence) {
		this.name = name;
		this.sequence = sequence;
	}

	public InputSource(File file) throws IOException {
		this.name = file.getName();
		this.sequence = new String(Files.readAllBytes(file.toPath()), UTF8);
	}

	public InputSource(String name, InputStream is) throws IOException {
		StringBuilder strb = new StringBuilder();
		byte[] buffer = new byte[2096];
		int r;
		while ((r = is.read(buffer)) > 0) {
			strb.append(new String(buffer, 0, r, UTF8));
		}
		this.sequence = strb.toString();
	}

	boolean hasNext() {
		return index < sequence.length();
	}

	boolean hasNext(int lookahead) {
		return index + lookahead < sequence.length();
	}

	public char peek() {
		return peek(0);
	}

	public char peek(int lookahead) {
		return sequence.charAt(index + lookahead);
	}

	public char nextChar() {
		char c = sequence.charAt(index++);
		if (c == '\n') {
			lineNumber++;
			lineOffset = 0;
		} else {
			lineOffset++;
		}
		return c;

	}

	public int index() {
		return index;
	}

	public int lineNumber() {
		return lineNumber;
	}

	public int lineOffset() {
		return lineOffset;
	}

	public String getName() {
		return name;
	}
	

}
