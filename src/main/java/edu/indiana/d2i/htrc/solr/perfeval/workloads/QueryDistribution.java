package edu.indiana.d2i.htrc.solr.perfeval.workloads;

import java.util.HashMap;
import java.util.Map;

import edu.indiana.d2i.htrc.solr.perfeval.conf.Conf;

public class QueryDistribution {
	private Map<QueryType, Integer> distribMap = new HashMap<QueryType, Integer>();; // a map from query type to its percentage
	private static final QueryDistribution queryDistribution = new QueryDistribution();
	private QueryDistribution() {
		distribMap.put(QueryType.term_query, Integer.parseInt(Conf.getProperty("TERM_QUERY")));
		distribMap.put(QueryType.short_phrase_query, Integer.parseInt(Conf.getProperty("SHORT_PHRASE_QUERY")));
		distribMap.put(QueryType.long_phrase_query, Integer.parseInt(Conf.getProperty("LONG_PHRASE_QUERY")));
		distribMap.put(QueryType.boolean_and_2_query, Integer.parseInt(Conf.getProperty("BOOLEAN_AND_2_QUERY")));
		distribMap.put(QueryType.boolean_and_5_query, Integer.parseInt(Conf.getProperty("BOOLEAN_AND_5_QUERY")));
		distribMap.put(QueryType.boolean_or_2_query, Integer.parseInt(Conf.getProperty("BOOLEAN_OR_2_QUERY")));
		distribMap.put(QueryType.boolean_or_5_query, Integer.parseInt(Conf.getProperty("BOOLEAN_OR_5_QUERY")));
		distribMap.put(QueryType.numeric_range_query, Integer.parseInt(Conf.getProperty("NUMERIC_RANGE_QUERY")));
		distribMap.put(QueryType.proximity_query, Integer.parseInt(Conf.getProperty("PROXIMITY_QUERY")));
		distribMap.put(QueryType.wild_card, Integer.parseInt(Conf.getProperty("WILD_CARD")));
	}
	
	public static QueryDistribution getInstance() {
		return queryDistribution;
	}
	
	public Map<QueryType, Integer> getDistribMap() {
		return distribMap;
	}
}
