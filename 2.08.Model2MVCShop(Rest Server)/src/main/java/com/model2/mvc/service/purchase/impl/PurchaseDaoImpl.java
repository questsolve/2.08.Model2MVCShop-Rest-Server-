package com.model2.mvc.service.purchase.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.purchase.PurchaseDao;
import com.model2.mvc.service.domain.Purchase;

@Repository("purchaseDaoImpl")
public class PurchaseDaoImpl implements PurchaseDao{

	@Autowired
	@Qualifier("sqlSessionTemplate")
	private SqlSession sqlSession;
		
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	public PurchaseDaoImpl() {
	}
	
	public void insertPurchase(Purchase purchaseVO) throws Exception{
		sqlSession.insert("PurchaseMapper.addPurchase",purchaseVO);
	}
	
	public Purchase findPurchase(int tranNo) throws Exception {
		return sqlSession.selectOne("PurchaseMapper.getPurchase", tranNo);
	}
	
	public List<Purchase> getPurchaseList(Map map) throws Exception{
		System.out.println(map.get("buyer"));
		return sqlSession.selectList("PurchaseMapper.getPurchaseList", map);		
	}

	public HashMap<String,Object> getSaleList(Search searchVO) throws Exception{
		//판매목록을 검색하는 메소드 구현
		return null;
	}
	
	public int getTotalCount(Map map) throws Exception{
		return sqlSession.selectOne("PurchaseMapper.getTotalPurchaseCount", map);
	}
	
	public void updatePurchase(Purchase purchaseVO) throws Exception{
		sqlSession.update("PurchaseMapper.updatePurchase",purchaseVO);
	}
	
	public void updateTranCode(Purchase purchaseVO) throws Exception{
		sqlSession.update("PurchaseMapper.updateTranCode", purchaseVO);
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
