package com.model2.mvc.service.product;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;


public interface ProductDao {
	
	public void insertProduct(Product productVO) throws SQLException;
	
	public Product findProduct(int prodNo) throws Exception;
	
	public List<Product> getProductList(Search search) throws Exception;
	
	public void updateProduct(Product productVO) throws Exception;
	
	public void updateProductCount(Map map) throws Exception;
	
	public int getTotalCount(Search search) throws Exception;
}
