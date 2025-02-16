package kr.co.pikpak.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public interface ProductMgmtService {

	List<HashMap<String, Object>> searchcompanyname(HashMap<String, Object> param);

	HashMap<String, Object> insertProduct(HashMap<String, Object> param);

	List<HashMap<String, Object>> selectAllList();

	HashMap<String, Object> selectProductInfo(HashMap<String, Object> param);

	HashMap<String, Object> updateProduct(HashMap<String, Object> param);

	List<HashMap<String, Object>> selectSearchProduct(HashMap<String, Object> param);


	String generateNewCode(HashMap<String, Object> param);

	String getOperatorNameByUserId(String userId);

	
}
