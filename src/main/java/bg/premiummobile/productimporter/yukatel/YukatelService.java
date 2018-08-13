package bg.premiummobile.productimporter.yukatel;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import bg.premiummobile.productimporter.yukatel.model.YukatelStockInfo;

@Service
public class YukatelService {
	
	public List<YukatelStockInfo> getYukatelStockInfo(String sessionId, boolean onlyAvailable) throws IOException{
		
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("xajax", "artikel_suche");
		if(onlyAvailable) {
			requestBody.put("xajaxargs[]", "<xjxquery><q>artnr=&adrnr=&bezeichnung=&hauptwarengruppe=3&nurlager=on</q></xjxquery>");
		}
		else {
			requestBody.put("xajaxargs[]", "<xjxquery><q>artnr=&adrnr=&bezeichnung=&hauptwarengruppe=3&warengruppe=</q></xjxquery>");
		}
						         
		Document doc = Jsoup.connect("https://wawi.yukatel.de:85/nexterp/jonas2/faktura/auftrag_neu/auftrag_request.php")
				  .data(requestBody)
				  .header("Cookie", "PHPSESSID=" + sessionId)
				  .timeout(30000)
				  .post();
		Element xjx = doc.selectFirst("xjx");
		Element cmd = xjx.selectFirst("cmd");
		Element body = cmd.selectFirst("tbody");
		Elements rows = body.getElementsByTag("tr");
		System.out.println(rows.size());
//		Elements rows = body.getElementsByTag("tr");
//		for(Element row : rows) {
//			System.out.println(row.getAllElements().size());
//		}
//		String title = doc.title();
//		System.out.println(title);
		return null;
	}
}
