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

public class ConditionResult {

	private final boolean isCaseCondition;
	private final boolean test;
	private final int number;
	
	public ConditionResult(boolean testResult) {
		this.test = testResult;
		this.number = 0;
		this.isCaseCondition = false;
	}

	public ConditionResult(int number) {
		this.number = number;
		this.test = false;
		this.isCaseCondition = true;
	}
	
	public boolean isTest() {
		return ! isCaseCondition;
	}

	public boolean isCase() {
		return isCaseCondition;
	}
	
	public boolean selectElse() {
		return isTest() && ! test;
	}
	
	public int getCase() {
		if (!isCase()) {
			throw new IllegalStateException();
		}
		return number;
	}
	
	
	
}
