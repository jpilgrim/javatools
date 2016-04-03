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

import static de.jevopi.jetex.tex.Category.ACTIVE;
import static de.jevopi.jetex.tex.Category.BEGINGROUP;
import static de.jevopi.jetex.tex.Category.COMMENT;
import static de.jevopi.jetex.tex.Category.ENDGROUP;
import static de.jevopi.jetex.tex.Category.EOL;
import static de.jevopi.jetex.tex.Category.ESC;
import static de.jevopi.jetex.tex.Category.IGNORE;
import static de.jevopi.jetex.tex.Category.INVALID;
import static de.jevopi.jetex.tex.Category.LETTER;
import static de.jevopi.jetex.tex.Category.MATHSHIFT;
import static de.jevopi.jetex.tex.Category.OTHER;
import static de.jevopi.jetex.tex.Category.PAR;
import static de.jevopi.jetex.tex.Category.SPACE;
import static de.jevopi.jetex.tex.Category.SUB;
import static de.jevopi.jetex.tex.Category.SUPER;
import static de.jevopi.jetex.tex.Category.TAB;

/**
 * Switch class, subclasses only need to implement the handled cases. The switch
 * is called via {@link #switchCategory(Category, Object)}. All non explicitly
 * handled cases delegate to the {@link #defaultCase(Category, Object)}. This
 * class is used for instance for state machines.
 */
public class CategorySwitch<INPUT, RETURN> {

	public RETURN switchCategory(Category cat, INPUT input) {
		switch (cat) {
		case ACTIVE:
			return caseActive(input);
		case BEGINGROUP:
			return caseBeginGroup(input);
		case COMMENT:
			return caseComment(input);
		case ENDGROUP:
			return caseEndGroup(input);
		case EOL:
			return caseEOL(input);
		case ESC:
			return caseESC(input);
		case IGNORE:
			return caseIgnore(input);
		case INVALID:
			return caseInvalid(input);
		case LETTER:
			return caseLetter(input);
		case MATHSHIFT:
			return caseMathShift(input);
		case OTHER:
			return caseOther(input);
		case PAR:
			return casePar(input);
		case SPACE:
			return caseSpace(input);
		case SUB:
			return caseSub(input);
		case SUPER:
			return caseSuper(input);
		case TAB:
			return caseTab(input);
		default:
			return defaultCase(cat, input);

		}
	}

	protected RETURN caseActive(INPUT input) {
		return defaultCase(ACTIVE, input);
	}

	protected RETURN caseBeginGroup(INPUT input) {
		return defaultCase(BEGINGROUP, input);
	}

	protected RETURN caseComment(INPUT input) {
		return defaultCase(COMMENT, input);
	}

	protected RETURN caseEndGroup(INPUT input) {
		return defaultCase(ENDGROUP, input);
	}

	protected RETURN caseEOL(INPUT input) {
		return defaultCase(EOL, input);
	}

	protected RETURN caseESC(INPUT input) {
		return defaultCase(ESC, input);
	}

	protected RETURN caseIgnore(INPUT input) {
		return defaultCase(IGNORE, input);
	}

	protected RETURN caseInvalid(INPUT input) {
		return defaultCase(INVALID, input);
	}

	protected RETURN caseLetter(INPUT input) {
		return defaultCase(LETTER, input);
	}

	protected RETURN caseOther(INPUT input) {
		return defaultCase(OTHER, input);
	}

	protected RETURN caseMathShift(INPUT input) {
		return defaultCase(MATHSHIFT, input);
	}

	protected RETURN caseSpace(INPUT input) {
		return defaultCase(SPACE, input);
	}

	protected RETURN casePar(INPUT input) {
		return defaultCase(PAR, input);
	}

	protected RETURN caseSub(INPUT input) {
		return defaultCase(SUB, input);
	}

	protected RETURN caseSuper(INPUT input) {
		return defaultCase(SUPER, input);
	}

	protected RETURN caseTab(INPUT input) {
		return defaultCase(TAB, input);
	}

	protected RETURN defaultCase(Category category, INPUT input) {
		return null;
	}
}
