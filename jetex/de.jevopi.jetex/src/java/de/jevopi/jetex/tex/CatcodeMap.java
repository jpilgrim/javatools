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

import static de.jevopi.jetex.tex.Category.LETTER;
import static de.jevopi.jetex.tex.Category.OTHER;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class CatcodeMap {

	final Map<Category, Set<Character>> charToCatcode;

	public CatcodeMap() {
		charToCatcode = new HashMap<>();
		Stream.of(Category.values()).forEach(cat -> {
			Set<Character> set = new HashSet<>();
			if (cat != LETTER && cat != OTHER) {
				set.add(cat.defaultCharacter);
			}
			charToCatcode.put(cat, set);
		});
	}
	
	public boolean isCategory(final char c, final Category cat) {
		if (charToCatcode.get(cat).contains(c)) {
			return true;
		}
		if (cat == LETTER && Character.isLetter(c)) {
			return true;
		}
		return cat==OTHER;
	}

	public Category category(final char c) {
		Optional<Category> mapped = Stream.of(Category.values()).filter(cat -> charToCatcode.get(cat).contains(c))
				.findAny();
		if (mapped.isPresent()) {
			return mapped.get();
		}
		if (Character.isLetter(c)) {
			return LETTER;
		}
		return Category.OTHER;
	}

	/**
	 * Sets the category of a character, similar to tex command {@code \catcode}
	 */
	void catcode(final char c, Category newCategory) {
		Category oldCat = category(c);
		if (oldCat != newCategory) {
			charToCatcode.get(newCategory).add(c);
			charToCatcode.get(oldCat).remove(c);
		}
	}

}
