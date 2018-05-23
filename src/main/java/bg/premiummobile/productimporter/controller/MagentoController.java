package bg.premiummobile.productimporter.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import bg.premiummobile.productimporter.configuration.HttpClient;
import bg.premiummobile.productimporter.httpclients.Magento2Client;
import bg.premiummobile.productimporter.magento.domain.MagentoAttribute;
import bg.premiummobile.productimporter.magento.domain.MagentoSiteMapUrlXML;
import bg.premiummobile.productimporter.magento.domain.MagentoSiteMapXML;

@Controller
@RequestMapping("/magento")
public class MagentoController {
	
//	@Autowired
//	private MagentoPriceUpdater updater;
	
	@Autowired
	private Magento2Client magentoClient;
	
	@Autowired
	private HttpClient client;
	
//	@PostMapping("/magentoprices")
//	@ResponseBody
//	public List<String> uploadPrices(@RequestParam("file") MultipartFile file) throws Exception{
//		return updater.mapFile(file);
//	}
	
	
	@GetMapping("/cachewarmer")
	@ResponseBody
	public List<HashMap<String, String>> warmPremiumMobileCache() throws Exception{
		List<HashMap<String, String>> responses = new ArrayList<HashMap<String, String>>();
		MagentoSiteMapXML siteMap = magentoClient.getSitemap();
		HashMap<String, String> error503 = new HashMap<String, String>();
		HashMap<String, String> error404 = new HashMap<String, String>();
		HashMap<String, String> error502 = new HashMap<String, String>();
		HashMap<String, String> status200 = new HashMap<String, String>();
		long totalTime = 0;
		int counter = 0;
		for(MagentoSiteMapUrlXML url : siteMap.getList()){
			long startTime = System.currentTimeMillis();
			CloseableHttpResponse response = client.getClient().execute(new HttpGet(url.getUrl()));
			long endTime = System.currentTimeMillis() - startTime;
			HttpEntity httpEntity = response.getEntity();
//			String responseString = EntityUtils.toString(httpEntity);
			EntityUtils.consume(httpEntity);
			String responseString = response.getStatusLine().toString();
			switch(response.getStatusLine().getStatusCode()){
				case 503:
					error503.put(url.getUrl(), responseString);
					break;
				case 404:
					error404.put(url.getUrl(), responseString);
					break;
				case 502:
					error502.put(url.getUrl(), responseString);
					break;
				case 200:
				default:
					status200.put(url.getUrl(), responseString);
					break;
			}
			
			System.out.println(url.getUrl() + ": " + responseString + ", " + endTime);	
			counter++;
			totalTime += endTime;
		}
		System.out.println("Total Time: " + totalTime);
		System.out.println("Average Time: " + totalTime/counter);
		responses.add(error503);
		responses.add(error404);
		responses.add(error502);
		responses.add(status200);
		return responses;
	}
	
	@GetMapping("/readMagentoAttributes")
	@ResponseBody
	public List<MagentoAttribute> readAttributes() throws Exception{
		List<MagentoAttribute> response = magentoClient.downloadMagentoAttributes();
		
		return response;
	}
	
	@GetMapping("/readMagentoCategories")
	@ResponseBody
	public List<String> readCategories() throws Exception{
		List<String> response = magentoClient.downloadMagentoCategories();
		
		return response;
	}
}
