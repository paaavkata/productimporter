package bg.premiummobile.productimporter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import bg.premiummobile.productimporter.domain.Result;
import bg.premiummobile.productimporter.solytron.SolytronService;

@RestController
@RequestMapping("/solytron")
public class SolytronController {

	@Autowired
	private SolytronService solytronService;
	
	@GetMapping("/importCategoryToMagento/{categoryName}/")
	@ResponseBody
	public List<Result> downloadAndImportCategory(@PathVariable("categoryName") String category){
		List<Result> results = solytronService.downloadAndImportCategory(category);
		return results;
	}
}
