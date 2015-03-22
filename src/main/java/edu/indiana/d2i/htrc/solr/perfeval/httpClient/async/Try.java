package edu.indiana.d2i.htrc.solr.perfeval.httpClient.async;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;

public class Try {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		Future<Integer> f = asyncHttpClient.prepareGet("http://www.ning.com/").execute(
		   new AsyncCompletionHandler<Integer>(){

		    @Override
		    public Integer onCompleted(Response response) throws Exception{
		        // Do something with the Response
		        return response.getStatusCode();
		    }

		    @Override
		    public void onThrowable(Throwable t){
		        // Something wrong happened.
		    }
		});

		int statusCode = f.get();
		System.out.println(statusCode);
		System.exit(0);
	}

}
