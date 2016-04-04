package de.jevopi.jetex.latex;

import java.util.HashMap;
import java.util.Map;

public class ElementsStatistics {

	private static class Statistics {
		int usageCount = 0;
	}
	
	final Map<String, Statistics> statistics = new HashMap<>();
	
	public void inc(String element) {
		Statistics s = statistics.get(element);
		if (s==null) {
			s = new Statistics();
			statistics.put(element, s);
		}
		s.usageCount++;
	}
	
	public int getUsageCount(String name) {
		Statistics stats = statistics.get(name);
		if (stats==null) {
			return 0;
		}
		return stats.usageCount;
	}
	
}
