package edu.indiana.d2i.htrc.solr.perfeval.daterange;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;

public class DataRangeGenerator {
	private static final int MIN_DATE = 1000;
	private static final int MAX_DATE = 2016;
	private static Random random = new Random();
	public static void main(String[] args) throws FileNotFoundException {
		PrintWriter pw = new PrintWriter("queries/numeric-range-query.txt");
		for(int i=0; i<3000; i++) {
			int startDate = MIN_DATE + random.nextInt(MAX_DATE - MIN_DATE);
			int endDate = startDate + random.nextInt(MAX_DATE - startDate);
			StringBuilder sb = new StringBuilder("publishDateTrie:[").append(startDate).append(" TO ").append(endDate).append("]");
			pw.println(sb.toString());
		}
		pw.flush();
		pw.close();
	}

}
