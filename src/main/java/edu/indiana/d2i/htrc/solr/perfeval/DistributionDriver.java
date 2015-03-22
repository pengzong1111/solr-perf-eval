package edu.indiana.d2i.htrc.solr.perfeval;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import edu.indiana.d2i.htrc.solr.perfeval.conf.Conf;
import edu.indiana.d2i.htrc.solr.perfeval.users.User;
import edu.indiana.d2i.htrc.solr.perfeval.util.Util;
import edu.indiana.d2i.htrc.solr.perfeval.workloads.QueryDistribution;
import edu.indiana.d2i.htrc.solr.perfeval.workloads.WorkLoad;

public class DistributionDriver {
	private static Logger logger = Logger.getLogger("logger"); 
	public static void main(String[] args) throws FileNotFoundException {
		logger.info("start::");
		int numberOfUsers = Integer.parseInt(Conf.getProperty("NO_OF_USER"));
		WorkLoad workload = Util.getWorkLoad(Conf.getProperty("WORK_LOAD"));
		int repetition = workload.getRepetition();
		int numOfQuery = workload.getNumOfQuery();
		int timeInterval = workload.getTimeInverval();
		QueryDistribution queryDistrib = workload.getQueryDistribution();
		
		if(queryDistrib == null) {
			System.out.println("workload is: " + workload.name());
			System.out.println("No distribution is required.please use RepeatableDriver instead. ");
			return;
		}
		//File queryDir = new File(Conf.getProperty("QUERY_DIR"));
		// a query string list for each user
		List<String>[] queryStrLists = Util.retrieveQueries(numOfQuery, queryDistrib, numberOfUsers);
	//	List<String> queries = Util.randomlyPick(queryStrList, numOfQuery);
		/*if(queries == null) {
			logger.error("no input queries!!");
			return;
		}*/
		
		User[] users = new User[numberOfUsers];

		for (int loop = 0; loop < repetition; loop++) {
			// initialize each user thread first because it takes some time.
			// then start them in another loop so that they start approximately
			// at the same time
			for (int i = 0; i < users.length; i++) {
				users[i] = new User(timeInterval, queryStrLists[i], i);
			}

			ExecutorService executorService = Executors.newFixedThreadPool(numberOfUsers);
			// start the specified number of User thread.
			for (User user : users) {
				executorService.execute(user);
			}

			System.out.println("all users running...");
			executorService.shutdown();
			try {
				boolean terminated = executorService.awaitTermination(600000, TimeUnit.MILLISECONDS);
				System.out.println("terminated? " + terminated);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("loop" + loop + " exits!");
		}
		System.out.println("DistributionDriver exit!");
	}
}
