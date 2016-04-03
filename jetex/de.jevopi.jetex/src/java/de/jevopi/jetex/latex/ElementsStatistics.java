package de.jevopi.jetex.latex;

import java.util.HashMap;
import java.util.Map;

public class ElementsStatistics<T_Element> {

	private static class Statistics {
		int usageCount = 0;
	}
	
	final Map<T_Element, Statistics> statistics = new HashMap<>();
	
	public void inc(T_Element element) {
		Statistics s = statistics.get(element);
		if (s==null) {
			s = new Statistics();
			statistics.put(element, s);
		}
		s.usageCount++;
	}
	
}
