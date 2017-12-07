package com.model2.mvc.web.product;

import java.io.File;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;

@RestController
@RequestMapping("/product/*")
public class ProductRestController {
	
	@Autowired
	@Qualifier("productServiceImpl")
	ProductService productService;
	
	 
	public ProductRestController() {
		System.out.println("RestController Activate");
	}
	
	@RequestMapping(value="json/getProduct/{prodNo}", method=RequestMethod.GET)
	public Product getProduct(@PathVariable int prodNo) throws Exception{
		System.out.println("/json/getProduct");
		return productService.getProduct(prodNo);
	}
	
	@RequestMapping(value="json/addProduct", method=RequestMethod.POST)
	public Product addProduct(@RequestBody Product product ) throws Exception{
		System.out.println("/json/addProduct");
		
//		if(uploadFile != null) {
//			String fileName = uploadFile.getOriginalFilename();
//			product.setFileName(fileName);
//			File file = new File("C:\\workspace\\2.08.Model2MVCShop(Rest Server)\\WebContent\\images\\uploadFiles\\"+ fileName);
//			uploadFile.transferTo(file);
//		}
		
		System.out.println("전달받은 내역 : "+product);
		productService.addProduct(product);
		
		return product;
	}
	
		
	@RequestMapping(value="json/updateProduct",method=RequestMethod.POST)
	public String updateProduct(@RequestBody Product product) throws Exception{
		System.out.println("/updateProduct.do");
		productService.updateProduct(product);
		
		return "forward:/product/updateProduct.jsp";
	}

	
	
	@RequestMapping(value="/json/listProduct")
	public Map listProduct(@RequestBody Search search, Map productMap) throws Exception{
		System.out.println("/listProduct.do");
		
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		
		search.setPageSize(5);
		
		Map<String, Object> map = productService.getProductList(search);
		
		productMap.put("productMapList", map.get("productList"));
		productMap.put("productSearch", search);
		
		
		
		return productMap;
	}

}
