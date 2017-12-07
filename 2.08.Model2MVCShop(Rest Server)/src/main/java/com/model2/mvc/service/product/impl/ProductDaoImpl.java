package com.model2.mvc.service.product.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductDao;

@Repository("productDaoImpl")
public class ProductDaoImpl implements ProductDao {

	public ProductDaoImpl() {
	}
	
	@Autowired
	@Qualifier("sqlSessionTemplate")
	private SqlSession sqlSession;
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	
	public void insertProduct(Product productVO) throws SQLException {
		sqlSession.insert("ProductMapper.addProduct",productVO);
	}
	
	public Product findProduct(int prodNo) throws Exception {
		return sqlSession.selectOne("ProductMapper.getProduct", prodNo);
	}
	
	public List<Product> getProductList(Search search) throws Exception{
		return sqlSession.selectList("ProductMapper.getProductList",search);
	}
	
	public void updateProduct(Product productVO) throws Exception {
		sqlSession.update("ProductMapper.updateProduct",productVO);
	}
	
	public void updateProductCount(Map map) throws Exception {
		sqlSession.update("ProductMapper.updateProductCount",map);	
	}
	
	public int getTotalCount(Search search) throws Exception{
		return sqlSession.selectOne("ProductMapper.getTotalCount", search);
	}
	
	/*private String makeCurrentPage(String sql, Search search) throws Exception{
		//받은 sql 중에서 필요한 개수만큼 추출하는 Inline View 생성하는 query 만들기
		sql = "SELECT * "
				+ "FROM ( SELECT inner_table. * ,  ROWNUM AS row_seq " +
				" 	FROM (	"+sql+" ) inner_table "+
				"	WHERE ROWNUM <="+search.getCurrentPage()*search.getPageSize()+" ) " +
				"WHERE row_seq BETWEEN "+((search.getCurrentPage()-1)*search.getPageSize()+1) +
				" AND "+search.getCurrentPage()*search.getPageSize();
		System.out.println("CurrentPage SQL :: " + sql);
		return sql;
	}*/


	



}
