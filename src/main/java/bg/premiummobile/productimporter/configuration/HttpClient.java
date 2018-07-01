package bg.premiummobile.productimporter.configuration;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpClient {
	
	private static final int TIMEOUT_SECONDS = 10;
	private static final int SOCKET_TIMEOUT = 15;
	
	@Bean
	public CloseableHttpClient getClient(){
		
		PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
		manager.setMaxTotal(30);
		manager.setDefaultMaxPerRoute(20);
		
		RequestConfig config = RequestConfig.custom()
		  .setConnectTimeout(TIMEOUT_SECONDS * 1000)
		  .setConnectionRequestTimeout(TIMEOUT_SECONDS * 1000).build();
//		  .setSocketTimeout(TIMEOUT_SECONDS * 1000).build();
		
		CloseableHttpClient client = HttpClientBuilder.create().setConnectionManager(manager).setDefaultRequestConfig(config).build();
		
		return client;
	}
	
}
