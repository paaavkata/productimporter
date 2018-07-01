package bg.premiummobile.productimporter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import bg.premiummobile.productimporter.domain.Result;
import bg.premiummobile.productimporter.stantek.StantekService;

@RestController("/stantek")
public class StantekController {
	
	@Autowired
	private StantekService service;
	
	@GetMapping("/printCategoryInfo/{category}")
	@ResponseBody
	public List<Result> readNew(@PathVariable("category") String category){
		return service.downloadAndImportCategory(category);
	}
}
