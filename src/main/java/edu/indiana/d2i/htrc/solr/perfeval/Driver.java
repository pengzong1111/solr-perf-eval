package edu.indiana.d2i.htrc.solr.perfeval;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import edu.indiana.d2i.htrc.solr.perfeval.conf.Conf;
import edu.indiana.d2i.htrc.solr.perfeval.users.User;
import edu.indiana.d2i.htrc.solr.perfeval.util.Util;

public class Driver {
	private static Logger logger = Logger.getLogger("logger"); 
	public static void main(String[] args) throws FileNotFoundException {
		logger.info("start");
		int numberOfUsers = Integer.parseInt(Conf.getProperty("NO_OF_USER"));
		ExecutorService executorService = Executors.newFixedThreadPool(numberOfUsers);
		User[] users = new User[numberOfUsers];
		File inputQueryFile = new File("queries/singleQuery");
		List<String> queryStrList = Util.readLines(inputQueryFile);
		//initialize each user thread first because it takes some time.  
		//then start them in another loop so that they start approximately at the same time
		for(int i=0; i<users.length; i++) {
			users[i] = new User(100, queryStrList, i);
		}
		
		//start the specified number of User thread. 
		for(User user: users) {
			executorService.execute(user);
		}
	
		System.out.println("all users running...");
		executorService.shutdown();
		try {
			executorService.awaitTermination(5000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Driver exits!");
	}

}
