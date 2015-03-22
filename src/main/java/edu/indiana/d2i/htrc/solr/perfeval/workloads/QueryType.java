package edu.indiana.d2i.htrc.solr.perfeval.workloads;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public enum QueryType {
	term_query(new File("queries/term-query-high-freq.txt")), // words with high frequency in English literature
	short_phrase_query(new File("queries/short-phrase-query.txt")), // a phrase with only 2 terms
	long_phrase_query(new File("queries/long-phrase-query.txt")), // a phrase with 5 terms
	boolean_and_2_query(new File("queries/boolean-and-2-query.txt")), //boolean query that is conjunctive with 2 term queries
	boolean_and_5_query(new File("queries/boolean-and-5-query.txt")),//boolean query that is conjunctive with 5 term queries
	boolean_or_2_query(new File("queries/boolean-or-2-query.txt")),//boolean query that is disjunctive with 2 term queries
	boolean_or_5_query(new File("queries/boolean-or-5-query.txt")), //boolean query that is disjunctive with 5 term queries
	numeric_range_query(new File("queries/numeric-range-query.txt")), //rang query on publishDateTrie field
//	prefix_query, // prefix can be merged into wildcard
	proximity_query(new File("queries/proximity-query.txt")), // queries that does not require exact match with a slop-factor. ocr:"under grant"~2
	wild_card(new File("queries/wildcard-query.txt"));
	
	private static Logger logger = Logger.getLogger("logger");
	private File queryFile;
	QueryType(File queryFile) {
		this.queryFile = queryFile;
	}
	
	public List<String> load(int size) {
		try {
			List<String> queries = new ArrayList<String>(size);
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(queryFile)));
			String query = null;
			int count = 0;
			while((query=br.readLine()) != null && count < size) {
				queries.add(query);
				count++;
			}
			br.close();
			return queries;
		} catch (FileNotFoundException e) {
			logger.error("no such file for query input:", e );
			return null;
		} catch (IOException e) {
			logger.error("IOException happened while reading query input file: ", e);
			return null;
		}
	}
}
