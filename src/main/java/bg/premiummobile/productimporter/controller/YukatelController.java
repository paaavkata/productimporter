package bg.premiummobile.productimporter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import bg.premiummobile.productimporter.yukatel.YukatelService;

@Controller
@RequestMapping("/yukatel")
public class YukatelController {

	@Autowired
	private YukatelService service;
	
	@GetMapping("/getStock")
	@ResponseBody
	public void downloadYukatelStock(@RequestParam("sessionId") String sessionId, @RequestParam("onlyAvailable") boolean onlyAvailable) throws Exception{

		service.getYukatelStockInfo(sessionId, onlyAvailable);
	}
}
