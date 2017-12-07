package com.model2.mvc.service.product.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductDao;
import com.model2.mvc.service.product.ProductService;


@Service("productServiceImpl")
public class ProductServiceImpl implements ProductService {

	@Autowired
	@Qualifier("productDaoImpl")
	ProductDao productDAO;
	
	public ProductServiceImpl() {
		productDAO = new ProductDaoImpl();
	}

	@Override
	public void addProduct(Product productVO) throws Exception {
		productDAO.insertProduct(productVO);
	}

	@Override
	public Product getProduct(int prodNo) throws Exception {
		return productDAO.findProduct(prodNo);
		 
	}

	@Override
	public Map<String, Object> getProductList(Search search) throws Exception {
		
		List<Product> list = productDAO.getProductList(search);
		
		int totalCount = productDAO.getTotalCount(search);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("productList", list);
		map.put("totalCount", new Integer(totalCount));
		
		return map;
	}

	@Override
	public void updateProduct(Product productVO) throws Exception {
		productDAO.updateProduct(productVO);
	}
	
	@Override
	public void updateProductCount(int purchasCount, int prodNo) throws Exception{
		Map<String, Integer> map  = new HashMap<String, Integer>();
		map.put("purchaseCount", new Integer(purchasCount));
		map.put("prodNo", new Integer(prodNo));
		
		productDAO.updateProductCount(map);
		
	}
	
}
