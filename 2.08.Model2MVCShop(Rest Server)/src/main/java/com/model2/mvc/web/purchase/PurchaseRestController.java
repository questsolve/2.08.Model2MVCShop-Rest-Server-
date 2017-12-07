package com.model2.mvc.web.purchase;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.user.UserService;


public class PurchaseRestController {

	@Autowired
	@Qualifier("userServiceImpl")
	UserService userService;
	
	@Autowired
	@Qualifier("productServiceImpl")
	ProductService productService;
	
	@Autowired
	@Qualifier("purchaseServiceImpl")
	PurchaseService purchaseService;
		
	@Value("#{commonProperties['pageUnit']}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	int pageSize;
	
	
	public PurchaseRestController() {
		System.out.println("PurchaseController Default Constructor");
	}
	
//	@RequestMapping(value="addPurchase", method=RequestMethod.POST)
//	public String addPurchase(@ModelAttribute("purchase")Purchase purchase, 
//									   @RequestParam("buyerId")String buyerId, @RequestParam("prodNo") int prodNo, HttpServletRequest request) throws Exception {
//		
//		System.out.println("/addPurchase.do");
//		Product product = productService.getProduct(prodNo);
//		User user = userService.getUser(buyerId);
//		
//		purchase.setBuyer(user);
//		purchase.setPurchaseProd(product);
//		purchase.setDivyAddr(request.getParameter("receiverAddr"));
//		purchase.setDivyDate(request.getParameter("receiverDate"));
//		purchase.setDivyRequest(request.getParameter("receiverRequest"));
//		purchase.setPaymentOption(request.getParameter("paymentOption"));
//		purchase.setReceiverName(request.getParameter("receiverName"));
//		purchase.setReceiverPhone(request.getParameter("receiverPhone"));
//		purchase.setPurchaseCount(Integer.parseInt(request.getParameter("purchaseCount")));
//		
//		if( (purchase.getPurchaseProd().getProdInven()<purchase.getPurchaseCount() ) ){
//			request.setAttribute("purchase", purchase);
//			return "forward:/purchase/refusePurchase.jsp";
//		}
//		
//		purchaseService.addPurchase(purchase);
//		productService.updateProductCount(purchase.getPurchaseCount(), purchase.getPurchaseProd().getProdNo());
//		
//		return "forward:/purchase/addPurchase.jsp";
//	}
	
	@RequestMapping(value="/addPurchase", method=RequestMethod.POST)
	public ModelAndView addPurchase(@ModelAttribute("purchase")Purchase purchase, 
									   @RequestParam("buyerId")String buyerId, @RequestParam("prodNo") int prodNo, HttpServletRequest request) throws Exception {
		
		System.out.println("/addPurchase");
		Product product = productService.getProduct(prodNo);
		User user = userService.getUser(buyerId);
		
		purchase.setBuyer(user);
		purchase.setPurchaseProd(product);
		purchase.setDivyAddr(request.getParameter("receiverAddr"));
		purchase.setDivyDate(request.getParameter("receiverDate"));
		purchase.setDivyRequest(request.getParameter("receiverRequest"));
		purchase.setPaymentOption(request.getParameter("paymentOption"));
		purchase.setReceiverName(request.getParameter("receiverName"));
		purchase.setReceiverPhone(request.getParameter("receiverPhone"));
		purchase.setPurchaseCount(Integer.parseInt(request.getParameter("purchaseCount")));
		
		purchaseService.addPurchase(purchase);
		productService.updateProductCount(purchase.getPurchaseCount(), purchase.getPurchaseProd().getProdNo());
		
		ModelAndView mV = new ModelAndView(); 
		mV.addObject("purchase", purchase);	
		mV.setViewName("forward:/purchase/addPurchase.jsp");
		return mV;
	}
	
	@RequestMapping(value="addPurchase", method=RequestMethod.GET)
	public String addPurchaseView(@RequestParam("prodNo")int prodNo, HttpSession session, Map<String,Object> map) throws Exception {
		Product product = productService.getProduct(prodNo);
		User user = (User)session.getAttribute("user");
		Purchase purchase = new Purchase();
		purchase.setBuyer(user);
		purchase.setPurchaseProd(product);
			
		map.put("purchase", purchase);
		
		return "forward:/purchase/addPurchaseView.jsp";
	}
	
//	@RequestMapping(value="getPurchase")
//	public String getPurchase(@RequestParam("tranNo") int tranNo, Map<String,Object> map) throws Exception{
//		System.out.println("/getPurchase.jsp");
//		System.out.println(tranNo+" 번 거래목록");
//		Purchase purchase = purchaseService.getPurchase(tranNo);
//		System.out.println(purchase);
//		map.put("purchase", purchase);
//		return "forward:/purchase/getPurchase.jsp";
//	}
	
	@RequestMapping(value="getPurchase")
	public ModelAndView getPurchase(@RequestParam("tranNo") int tranNo, Map<String,Object> map) throws Exception{
		System.out.println("/getPurchase");
		System.out.println(tranNo+" 번 거래목록");
		Purchase purchase = purchaseService.getPurchase(tranNo);
		System.out.println(purchase);
		map.put("purchase", purchase);
		ModelAndView mV = new ModelAndView("forward:/purchase/getPurchase.jsp",map);
		
		return mV;
	}
	
	@RequestMapping(value="listPurchase")
	public String listPurchase(@ModelAttribute("search") Search search, HttpSession session, Map<String, Object> purchaseMap) throws Exception{
		System.out.println("/listPurchase");		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		Map<String, Object> map =purchaseService.getPurchaseList(search, ((User)session.getAttribute("user")).getUserId());
		Page resultPage = 
				new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		System.out.println(((User)session.getAttribute("user")));
		
		purchaseMap.put("purchaseMap", map);
		purchaseMap.put("purchaseMapList", map.get("purchaseList"));
		purchaseMap.put("resultPage",resultPage);
		purchaseMap.put("purchaseSearch", search);
		System.out.println(map.size());
		System.out.println(map.get("purchaseList"));
		System.out.println(resultPage);
		System.out.println(search);
		System.out.println(((Purchase)((List)purchaseMap.get("purchaseMapList")).get(0)).getBuyer());
		
		return "forward:/purchase/listPurchase.jsp";
	}

	@RequestMapping(value="updatePurchase", method=RequestMethod.POST)
	public String updatePurchase(@RequestParam("tranNo")int tranNo, Map<String, Object> map, HttpServletRequest request) throws Exception{
		System.out.println("/updatePurchase.do");
		Purchase purchase = purchaseService.getPurchase(tranNo);
		purchase.setDivyAddr(request.getParameter("receiverAddr"));
		purchase.setDivyDate(request.getParameter("divyDate"));
		purchase.setDivyRequest(request.getParameter("receiverRequest"));
		purchase.setPaymentOption(request.getParameter("paymentOption"));
		purchase.setReceiverName(request.getParameter("receiverName"));
		purchase.setReceiverPhone(request.getParameter("receiverPhone"));
		
		System.out.println("::::" +request.getParameter("receiverDate"));
		System.out.println("::::" +purchase.getDivyDate());
		
		purchaseService.updatePurchase(purchase);
		map.put("purchase", purchase);		
		return "forward:/purchase/updatePurchase.jsp";
	}
	
	@RequestMapping(value="updatePurchase", method=RequestMethod.GET)
	public String updatePurchaseView(@RequestParam("tranNo")int tranNo, Map<String, Object> map) throws Exception{
		System.out.println("/updatePurchaseView.do");
		Purchase purchase =purchaseService.getPurchase(tranNo);
		User user = userService.getUser(purchase.getBuyer().getUserId());
		Product product = productService.getProduct(purchase.getPurchaseProd().getProdNo());
		purchase.setBuyer(user);
		purchase.setPurchaseProd(product);


		System.out.println("####"+purchase.getBuyer().getUserId() +" :  " + purchase.getBuyer().getUserName());
		System.out.println("####"+purchase.getPurchaseProd().getProdNo() + " :  " +purchase.getPurchaseProd().getProdName());
		
		
		map.put("purchase", purchase);
		return "forward:/purchase/updatePurchaseView.jsp";
	}
	
	@RequestMapping(value="updateTranCode")
	public String updateTranCode(@RequestParam("tranNo") int tranNo, Map map) throws Exception {
		Purchase purchase = purchaseService.getPurchase(tranNo);
		int tranCode = Integer.parseInt(purchase.getTranCode().trim());
		System.out.println("tranCode = " + tranCode);
		
		if(tranCode<2) {
			tranCode= tranCode+1;
		}
		
		purchase.setTranCode(""+tranCode);
		
		String transtatus ="";
		if(purchase.getTranCode().contains("0")){
			transtatus = "배송 준비 중";
		}else if(purchase.getTranCode().contains("1")){
			transtatus = "배송 중";
		}else{
			transtatus = "배송 완료";
		}
		
		
		purchaseService.updateTranCode(purchase);
		
		return "forward:/purchase/listPurchase";
	}
}
