package bg.premiummobile.productimporter.configuration;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpClient {
	
	
	@Bean
	public CloseableHttpClient getClient(){
		
		PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
		manager.setMaxTotal(30);
		manager.setDefaultMaxPerRoute(20);
		CloseableHttpClient client = HttpClientBuilder.create().setConnectionManager(manager).build();
		
		return client;
	}
	
}
