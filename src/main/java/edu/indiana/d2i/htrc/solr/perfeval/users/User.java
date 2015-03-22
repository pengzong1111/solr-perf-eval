package edu.indiana.d2i.htrc.solr.perfeval.users;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import edu.indiana.d2i.htrc.solr.perfeval.conf.Conf;
import edu.indiana.d2i.htrc.solr.perfeval.httpClient.SolrAsyncHttpClient;
import edu.indiana.d2i.htrc.solr.perfeval.util.Util;
import edu.indiana.d2i.htrc.solr.perfeval.workloads.WorkLoad;

public class User implements Runnable{
	private static Logger logger = Logger.getLogger("logger");
	Logger statsLogger = Logger.getLogger("statsLog");
	private long timeInterval;
	private SolrAsyncHttpClient solrAsyncHttpClient;
	private WorkLoad workload;
	private static final String PATH_PREFIX = "/solr/ocr/select?wt=json&rows=15&q=";
	private static final String[] END_POINTS;
	private String name;
	private List<Future<String>> asyncResults;
	static {
		END_POINTS = Conf.getProperty("END_POINTS").split(",");
	}
	//a file that contains query str, one query per line
//	private File queryStrFile;
	List<String> queryStrList;
	
	public User(long timeInterval, List<String> queryStrList, int order) {
		this.timeInterval = timeInterval;
		this.queryStrList = queryStrList;
		// leave endpoint and path null values because they will be assigned different values for different request.<a shard will be selected as endpoint. a query will be selected as path along with PATH_Prefix>
		solrAsyncHttpClient = new SolrAsyncHttpClient(null, null, "GET", "edu.indiana.d2i.htrc.solr.perfeval.httpClient.async.HtrcSolrQueryHandler");
		name = "user" + order;
		asyncResults = new LinkedList<Future<String>>();
	}
	
	public void run() {
		try {
			System.out.println(name + " read " + queryStrList.size() + " queries");
			int i=0;
			for(String queryStr : queryStrList) {
				String path = PATH_PREFIX + queryStr;
				String endpoint = END_POINTS[i%END_POINTS.length];
				i++;
				solrAsyncHttpClient.setServiceEndPoint(endpoint);
				solrAsyncHttpClient.setPath(path);
				Future<String> f = solrAsyncHttpClient.request();
				asyncResults.add(f);
				Thread.sleep(timeInterval);
			}
		} catch (InterruptedException e) {
			logger.error("user thread interrupted!! ", e);
		}
		
		for(Future<String> f : asyncResults) {
			try {
				statsLogger.info(f.get());
			} catch (InterruptedException e) {
				logger.error(name + " got interrupted when retrieving result: ", e);
			} catch (ExecutionException e) {
				logger.error(name +" execution exception thrown: ", e);
			}
		}
		solrAsyncHttpClient.close();
	}

}
