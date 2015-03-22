package edu.indiana.d2i.htrc.solr.perfeval.workloads;

import java.io.File;
import java.util.List;

public enum WorkLoad {
	single_sparse(0, 1, 1, null), // 1 user random access
	repeated_sparse(5000, 5, 1, null),
	one_pass_clustered(100, 1, 100, QueryDistribution.getInstance()), // multiple users continuous access with randomly picked queries
	multiple_pass_clustered(100, 3, 100, QueryDistribution.getInstance()); // specifies volume Id to query. Good for stress a single shard
	
	private int timeInterval;// interval between 2 successive query requests
	private int repetition; // the time of loop of this whole workload
	private int numOfQuery;//how many query to to send as one pass
	private QueryDistribution queryDistrib;
	
	
	WorkLoad(int timeInterval, int repetition, int numOfQuery, QueryDistribution queryDistrib) {
		this.timeInterval = timeInterval;
		this.repetition = repetition;
		this.numOfQuery = numOfQuery;
		this.queryDistrib = queryDistrib;
	}
	
	
	public int getTimeInverval() {
		return timeInterval;
	}

	public int getRepetition() {
		return repetition;
	}
	
	public int getNumOfQuery() {
		return numOfQuery;
	}
	
	public QueryDistribution getQueryDistribution() {
		return queryDistrib;
	}
	
	/*public List<String> getQueryList() {
		
	}*/
}
