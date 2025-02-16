package kr.co.pikpak.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public interface ClientMgmtService {

	HashMap<String, Object> insertSupplier(HashMap<String, Object> param);

	HashMap<String, Object> insertVendor(HashMap<String, Object> param);

	List<HashMap<String, Object>> selectList();

	List<HashMap<String, Object>> getSupplierList();

	List<HashMap<String, Object>> getVendorList();

	HashMap<String, Object> getSupplierById(String string);

	HashMap<String, Object> getVendorById(String string);

	HashMap<String, Object> updateSupplier(HashMap<String, Object> param);

	HashMap<String, Object> updateVendor(HashMap<String, Object> param);

	List<HashMap<String, Object>> searchSupplierByName(HashMap<String, Object> param);

	List<HashMap<String, Object>> searchVendorByName(HashMap<String, Object> param);

	List<HashMap<String, Object>> selectSearchClient(HashMap<String, Object> param);

	List<HashMap<String, Object>> getAllList();

	String getLastCode(String clientType);

	String getNewCode(String lastCode);

	HashMap<String, Object> countDupChk(HashMap<String, Object> param);


}
