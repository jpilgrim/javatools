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
package de.jevopi.jetex.tex;

/**
 * Character categories
 */
public enum Category {
	/**
	 * 0. Escape character \
	 */
	ESC('\\', "ESC"),
	/**
	 * 1. Beginning of group {
	 */
	BEGINGROUP('{', "BG"),

	/**
	 * 2. End of group }
	 */
	ENDGROUP('}', "EG"),
	/**
	 * 3. Math shift $
	 */
	MATHSHIFT('$', "MS"),
	/**
	 * 4. Alignment tab &
	 */
	TAB('&', "TAB"),
	/**
	 * 5. End of line \n
	 */
	EOL('\n', "EOL"),
	/**
	 * 6. Parameter character #
	 */
	PAR('#', "PAR"),
	/**
	 * 7. Superscript ^
	 */
	SUPER('^', "SUP"),
	/**
	 * 8. Subscript _
	 */
	SUB('_', "SUB"),
	/**
	 * 9. Ignored (char 0)
	 */
	IGNORE((char) 0, "IGN"),
	/**
	 * 10. Space ' '
	 */
	SPACE(' ', "S"),
	/**
	 * 11. Letter a..z, A..Z
	 */
	LETTER('a', "L"),
	/**
	 * 12. Other; e.g. digits and punctuation.
	 */
	OTHER('.', "O"),
	/**
	 * 13. Active; ~
	 */
	ACTIVE('~', "ACT"),
	/**
	 * 14. Comment character %
	 */
	COMMENT('%', "CMT"),
	/**
	 * 15. Invalid character (char 127)
	 */
	INVALID((char) 127, "INV");

	char defaultCharacter;

	public final String shortID;

	private Category(char defaultCharacter, String shortID) {
		this.defaultCharacter = defaultCharacter;
		this.shortID = shortID;
	}

	public boolean in(Category... categories) {
		for (Category cat : categories) {
			if (cat == this) {
				return true;
			}
		}
		return false;
	}
}
