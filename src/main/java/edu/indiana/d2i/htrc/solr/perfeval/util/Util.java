package edu.indiana.d2i.htrc.solr.perfeval.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

import edu.indiana.d2i.htrc.solr.perfeval.workloads.QueryDistribution;
import edu.indiana.d2i.htrc.solr.perfeval.workloads.QueryType;
import edu.indiana.d2i.htrc.solr.perfeval.workloads.WorkLoad;

public class Util {
	private static Logger logger = Logger.getLogger(Util.class);
	/**
	 * read each line of the input file and put lines into a list
	 * @param inputFile
	 * @return
	 * @throws FileNotFoundException
	 */
	public static List<String> readLines(File inputFile) throws FileNotFoundException {
		System.out.println("reading queries ...");
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
		List<String> idList = new LinkedList<String>();
		String id = null;
		try {
			while((id=br.readLine()) != null) {
				idList.add(id);
			}
			br.close();
		} catch (IOException e) {
			logger.error("IOException when reading id list:", e);
		}
		return idList;
	}
	
	/**
	 * get the WorkLoad Enum value based on specified workload name
	 * @param workloadName
	 * @return
	 */
	public static WorkLoad getWorkLoad(String workloadName){
		for(WorkLoad workload : WorkLoad.values()) {
			if(workload.name().equals(workloadName)) {
				return workload;
			}
		}
		
		//if not found, return null;
		return null;
	}

	private static Random random = new Random();
	/**
	 * pick numOfQuery of queries from specified queryStrList
	 * @param queryStrList
	 * @param numOfQuery
	 * @return
	 */
	public static List<String> randomlyPick(List<String> queryStrList, int numOfQuery) {
		List<String> randomList = new LinkedList<String>();
		for(int i=0; i<numOfQuery; i++) {
			int index = random.nextInt(queryStrList.size());
			randomList.add(queryStrList.get(index));
		}
		return randomList;
	}


	/**
	 * get query string list array, in which each query string list is for a user
	 * @param numOfQuery
	 * @param queryDistrib
	 * @param numberOfUsers
	 * @return
	 */
	public static List<String>[] retrieveQueries(int numOfQuery, QueryDistribution queryDistrib, int numberOfUsers) {
		int total = numOfQuery * numberOfUsers;
		Map<QueryType, Integer> distribMap = queryDistrib.getDistribMap();
		Map<QueryType, List<String>> typeToQueriesMap = new HashMap<QueryType, List<String>>();
		List<String>[] queriesForEachUserArr = new List[numberOfUsers];
		for(int i=0; i<queriesForEachUserArr.length; i++) {
			queriesForEachUserArr[i] = new LinkedList<String>();
		}
		for(QueryType type : distribMap.keySet()) {
			int percentile = distribMap.get(type);
			int size = total*percentile/100;
			List<String> queries = type.load(size);
			int section = size/numberOfUsers;
			int start = 0;
			int end = start + section;
			for(List<String> list : queriesForEachUserArr) {
				list.addAll(queries.subList(start, end));
				start = end;
				end = start+section;
			}
		}
		return queriesForEachUserArr;
	}
}
