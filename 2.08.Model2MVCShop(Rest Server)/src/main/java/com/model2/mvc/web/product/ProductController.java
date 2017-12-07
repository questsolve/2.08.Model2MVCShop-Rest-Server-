package com.model2.mvc.web.product;

import java.io.File;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;

@Controller
@RequestMapping("/product/*")
public class ProductController {
	
	@Autowired
	@Qualifier("productServiceImpl")
	ProductService productService;
	
	@Value("#{commonProperties['pageUnit']}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	int pageSize;
	
	 
	public ProductController() {
		System.out.println(getClass());
	}
	
	@RequestMapping(value="addProduct", method=RequestMethod.POST)
	public String addProduct(@ModelAttribute("product") Product product ,@RequestParam("filelName")MultipartFile uploadFile) throws Exception{
		System.out.println("/addProduct.do");
		
		if(uploadFile != null) {
			String fileName = uploadFile.getOriginalFilename();
			product.setFileName(fileName);
			File file = new File("C:\\workspace\\2.07.Model2MVCShop(URI,pattern)\\WebContent\\images\\uploadFiles\\"+ fileName);
			uploadFile.transferTo(file);
		}
		
		System.out.println(product);
		productService.addProduct(product);
		
		return "forward:/product/addProduct.jsp";
	}
	
	@RequestMapping(value="addProduct", method=RequestMethod.GET)
	public String addProductView()  throws Exception {
		System.out.println("/addProductView.do");
		
		return "forward:/product/addProductView.jsp";
	}
	
	@RequestMapping(value="getProduct", method=RequestMethod.GET)
	public String getProduct(@RequestParam("prodNo") int prodNo,@RequestParam("menu") String menu, Map map, HttpServletRequest request, HttpServletResponse response) throws Exception{
		System.out.println("/getProduct.do");
		Product product = productService.getProduct(prodNo);
		map.put("product", product);
		
		//연희 도움 받기... 쿠키 뭐라고 했던 거 같은데..
		
		String history = null;
		Cookie[] cookies = request.getCookies();
		if (cookies!=null && cookies.length > 0) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if (cookie.getName().equals("history")) {
					history = cookie.getValue();
				}
			}
		}
		
		history += ","+prodNo;
		Cookie cookie = new Cookie("history",history);
		response.addCookie(cookie);
		
		if(menu.equals("search")) {
			return "forward:/product/getProduct.jsp";
		}else {
			return "forward:/product/updateProductView.jsp";
		}
	}
	
	@RequestMapping(value="updateProduct",method=RequestMethod.POST)
	public String updateProduct(@ModelAttribute("product")Product product) throws Exception{
		System.out.println("/updateProduct.do");
		productService.updateProduct(product);
		
		return "forward:/product/updateProduct.jsp";
	}
	@RequestMapping(value="/updateProduct",method=RequestMethod.GET)
	public String updateProductView(@RequestParam("prodNo") int prodNo) throws Exception{
		productService.getProduct(prodNo);
		return "forward:/product/updateProductView.jsp";
	}
	
	
	@RequestMapping(value="listProduct")
	public String listProduct(@ModelAttribute("search")Search search,@RequestParam("menu")String menu , Map productMap) throws Exception{
		System.out.println("/listProduct.do");
		
		String workFlow ="";
		if(menu.equals("search")) {
			workFlow = "상품목록조회";
		}else{
			workFlow = "상품관리";
		}
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		Map<String, Object> map = productService.getProductList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		productMap.put("productMapList", map.get("productList"));
		productMap.put("resultPage", resultPage);
		productMap.put("productSearch", search);
		productMap.put("menu", menu);
		productMap.put("workFlow", workFlow);
		
		return "forward:/product/listProduct.jsp";
	}

}
