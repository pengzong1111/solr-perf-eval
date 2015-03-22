package edu.indiana.d2i.htrc.solr.perfeval.httpClient.async;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.apache.log4j.Logger;

import com.ning.http.client.AsyncHandler;
import com.ning.http.client.HttpResponseBodyPart;
import com.ning.http.client.HttpResponseHeaders;
import com.ning.http.client.HttpResponseStatus;
import com.ning.http.client.Response;

/**
 * handler the asyn response. Get the response time of each request. Also extract the Qtime from Solr response
 * log format
 * success/failure	response_code	response_time	Qtime
 * @author Zong
 *
 */
public class HtrcSolrQueryHandler implements AsyncHandler<String>{
	Logger statsLogger = Logger.getLogger("statsLog");
	Logger logger = Logger.getLogger("logger");
	//private InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
	private JsonReader jsonReader;
	private long startTime;
	private long statusReceivedTime;
	private long bodyReceivedTime;
	private long qtime;
	private int responseCode;
//	private int count = 0;
	private ByteArrayOutputStream bytes = new ByteArrayOutputStream(2048);
	//3 params are just for logging
//	private String userName;
	private String endPoint;
	private String path;
	public HtrcSolrQueryHandler(/*String userName, */String endPoint, String path) {
		 this.startTime = System.currentTimeMillis();
		 this.endPoint = endPoint;
		 this.path = path;
	}
	
	private HtrcSolrQueryHandler() {
		
	}
	
	public void onThrowable(Throwable t) {
	//	System.out.println("!!!!!!!!!!!!");
		logger.error("exception for: " + endPoint + path, t);
		return;
	}

	public com.ning.http.client.AsyncHandler.STATE onBodyPartReceived(HttpResponseBodyPart bodyPart) throws Exception {
	//	System.out.println("body received!!");
		//System.out.println(count ++);
		bytes.write(bodyPart.getBodyPartBytes());
		return com.ning.http.client.AsyncHandler.STATE.CONTINUE;
	}

	public com.ning.http.client.AsyncHandler.STATE onStatusReceived(HttpResponseStatus responseStatus) throws Exception {
	//	System.out.println("responseStatus received!!");
		statusReceivedTime = System.currentTimeMillis();
		responseCode = responseStatus.getStatusCode();
		if(responseCode != 200) {
			logger.warn("response code: " + responseCode);
		//	statsLogger.info("failure	" + responseCode + "	" + (statusReceivedTime-startTime));
			return com.ning.http.client.AsyncHandler.STATE.ABORT;
		} /*else {
			statsLogger.info("success	" + responseCode + "	" + (statusReceivedTime-startTime));
		}*/
		return com.ning.http.client.AsyncHandler.STATE.CONTINUE;
	}

	public com.ning.http.client.AsyncHandler.STATE onHeadersReceived(HttpResponseHeaders headers) throws Exception {
		//System.out.println(headers.getHeaders().toString());
		return com.ning.http.client.AsyncHandler.STATE.CONTINUE;
	}

	public String onCompleted() throws Exception {
		//here all body content are received, so get timestamp
		//System.out.println("completed!!");
		bodyReceivedTime = System.currentTimeMillis();
		if(responseCode == 200) {
			//statsLogger.info("completed	200	"+ (bodyReceivedTime-startTime));
			JsonReader reader = Json.createReader(new ByteArrayInputStream(bytes.toByteArray()));
			JsonObject json = reader.readObject();
			qtime = json.getJsonObject("responseHeader").getInt("QTime");
			StringBuilder sb = new StringBuilder();
			sb.append(endPoint).append('\t').append(path).append('\t').append(responseCode).append('\t').append(statusReceivedTime-startTime).append('\t').append(bodyReceivedTime-startTime).append('\t').append(qtime);
			return sb.toString();
		} else {
			StringBuilder sb = new StringBuilder();
			//follow the same format but the response code will be an error code and Qtime will be 0
			sb.append(endPoint).append('\t').append(path).append('\t').append(responseCode).append('\t').append(statusReceivedTime-startTime).append('\t').append(bodyReceivedTime-startTime).append('\t').append(qtime);
			return sb.toString();
		}
		
	//  String result = bytes.toString("UTF-8");
	//	System.out.println(result);
	}
}
