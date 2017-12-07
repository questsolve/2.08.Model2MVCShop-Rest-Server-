package com.model2.mvc.service.purchase.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.purchase.PurchaseDao;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.user.UserDao;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductDao;
import com.model2.mvc.service.product.impl.ProductDaoImpl;

@Repository("purchaseServiceImpl")
public class PurchaseServiceImpl implements PurchaseService {

	@Autowired
	@Qualifier("userDaoImpl")
	UserDao userDAO;
	
	@Autowired
	@Qualifier("purchaseDaoImpl")
	PurchaseDao purchaseDAO;
	
	@Autowired
	@Qualifier("productDaoImpl")
	ProductDao productDAO;
	
	public PurchaseServiceImpl() {
		purchaseDAO = new PurchaseDaoImpl();
		productDAO = new ProductDaoImpl();
	}

	@Override
	public void addPurchase(Purchase purchaseVO) throws Exception {
		purchaseDAO.insertPurchase(purchaseVO);
		
	}

	@Override
	public Purchase getPurchase(int tranNo) throws Exception {
		System.out.println("PurchaseService : " +tranNo);
		return purchaseDAO.findPurchase(tranNo);
	}

	
	@Override
	public Map<String,Object> getPurchaseList(Search searchVO,String buyerId) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		User user = userDAO.getUser(buyerId);
		map.put("purchaseSearch", searchVO);
		map.put("buyer",buyerId);
		List<Purchase> list = purchaseDAO.getPurchaseList(map);
		
		for (Purchase purchase : list) {
			purchase.setBuyer(user);
		}
		
		int totalCount = purchaseDAO.getTotalCount(map);
		
		map.put("purchaseList", list);
		map.put("totalCount", new Integer(totalCount));
		
		return map;
	}

	@Override
	public HashMap<String, Object> getSaleList(Search searchVO) throws Exception {
		return purchaseDAO.getSaleList(searchVO);
	}

	@Override
	public void updatePurchase(Purchase purchaseVO) throws Exception {
		purchaseDAO.updatePurchase(purchaseVO);

	}

	@Override
	public void updateTranCode(Purchase purchaseVO) throws Exception {
		purchaseDAO.updateTranCode(purchaseVO);
	}
	
	public void updateProductCount(int purchaseCount, int prodNo) throws Exception {
		System.out.println("업데이트프로덕트카운트");
		System.out.println("purchaseCount"+purchaseCount + " : prodNo" +prodNo);
		productDAO.updateProductCount(null);
	}

	@Override
	public Purchase getPurchase2(int ProdNo) throws Exception {
		return this.getPurchase(ProdNo);
		
	}


}