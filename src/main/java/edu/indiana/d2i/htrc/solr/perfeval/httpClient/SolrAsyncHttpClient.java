package edu.indiana.d2i.htrc.solr.perfeval.httpClient;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.ning.http.client.AsyncHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;

public class SolrAsyncHttpClient {
	private static Logger logger = Logger.getLogger("logger");

	private String serviceEndPoint;
	private String path;
	private String method;
	private AsyncHttpClient asyncHttpClient;
	private String asyncHandlerClass;
	
	public String getServiceEndPoint() {
		return serviceEndPoint;
	}

	public void setServiceEndPoint(String serviceEndPoint) {
		this.serviceEndPoint = serviceEndPoint;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	public SolrAsyncHttpClient(String serviceEndPoint, String path, String method, String asyncHandlerName) {
		this.serviceEndPoint = serviceEndPoint;
		this.path = path;
		this.method = method;
		
		AsyncHttpClientConfig config = new AsyncHttpClientConfig.Builder().setRequestTimeout(5*60*1000).setReadTimeout(5*60*1000).build();
		this.asyncHttpClient = new AsyncHttpClient(config);
		//by default
		asyncHandlerClass = asyncHandlerName;
	}
	
	public Future<String> request() {
		String requestStr = serviceEndPoint + path;
		if(method.equals("GET")) {
			try {
				Future<String> f = asyncHttpClient.prepareGet(requestStr).execute((AsyncHandler<String>)(Class.forName(asyncHandlerClass).getConstructor(String.class, String.class).newInstance(serviceEndPoint, path)));
				return f;
			} catch (InstantiationException e) {
				logger.error("instiation exception: ", e);
				return null;
			} catch (IllegalAccessException e) {
				logger.error("IllegalAccessException: ", e);
				return null;
			} catch (ClassNotFoundException e) {
				logger.error("ClassNotFoundException: ", e);
				return null;
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		} else {
			logger.warn("only GET is supported now!");
			return null;
		}
	}
	
	public void close() {
		asyncHttpClient.closeAsynchronously();;
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		String endpoint = "http://chinkapin.pti.indiana.edu:9994";
		String path = "/solr/ocr/select?q=*:*&rows=0&wt=json";
		SolrAsyncHttpClient client = new SolrAsyncHttpClient(endpoint, path, "GET", "edu.indiana.d2i.htrc.solr.perfeval.httpClient.async.HtrcSolrQueryHandler");
		
		Future<String> f1 = client.request();
		
		client.setPath("/solr/ocr/select?q=id:wu.89087460754&rows=0&wt=x");
		Future<String> f2 = client.request();
		System.out.println("===============================================");
	logger.info("===============================================");
		String result1 = f1.get();
		System.out.println("f1:\n" + result1);
		
		String result2 = f2.get();
		System.out.println("f2:\n" + result2);
		System.out.println("----------------------------------------------");
		client.close();
	}
}
