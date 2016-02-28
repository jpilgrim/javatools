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
	ESC(0, '\\', "ESC"),
	/**
	 * 1. Beginning of group {
	 */
	BEGINGROUP(1, '{', "BG"),

	/**
	 * 2. End of group }
	 */
	ENDGROUP(2, '}', "EG"),
	/**
	 * 3. Math shift $
	 */
	MATHSHIFT(3, '$', "MS"),
	/**
	 * 4. Alignment tab &
	 */
	TAB(4, '&', "TAB"),
	/**
	 * 5. End of line \n
	 */
	EOL(5, '\n', "EOL"),
	/**
	 * 6. Parameter character #
	 */
	PAR(6, '#', "PAR"),
	/**
	 * 7. Superscript ^
	 */
	SUPER(7, '^', "SUP"),
	/**
	 * 8. Subscript _
	 */
	SUB(8, '_', "SUB"),
	/**
	 * 9. Ignored (char 0)
	 */
	IGNORE(9, (char) 0, "IGN"),
	/**
	 * 10. Space ' '
	 */
	SPACE(10, ' ', "S"),
	/**
	 * 11. Letter a..z, A..Z
	 */
	LETTER(11, 'a', "L"),
	/**
	 * 12. Other; e.g. digits and punctuation.
	 */
	OTHER(12, '.', "O"),
	/**
	 * 13. Active; ~
	 */
	ACTIVE(13, '~', "ACT"),
	/**
	 * 14. Comment character %
	 */
	COMMENT(14, '%', "CMT"),
	/**
	 * 15. Invalid character (char 127)
	 */
	INVALID(15, (char) 127, "INV");

	/**
	 * Only for debugging.
	 */
	char defaultCharacter;

	public final int code;

	public final String shortID;

	public static Category forCode(int code) {
		if (code < 0 || code >= Category.values().length) {
			throw new IndexOutOfBoundsException("Catcode must between 0 and " + (Category.values().length - 1)
					+ ", was " + code);
		}
		return Category.values()[code];
	}

	private Category(int code, char defaultCharacter, String shortID) {
		this.code = code;
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
