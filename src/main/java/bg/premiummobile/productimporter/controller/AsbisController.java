package bg.premiummobile.productimporter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import bg.premiummobile.productimporter.asbis.AsbisService;
import bg.premiummobile.productimporter.domain.Result;

@RestController
@RequestMapping("/asbis")
public class AsbisController {

	@Autowired
	private AsbisService service;
	
	@GetMapping("/downloadCategory/{category}")
	@ResponseBody
	public List<Result> downloadAndImportCategory(@PathVariable("category") String category) throws Exception{
		return service.downloadAndImportCategory(category);
	}
}
