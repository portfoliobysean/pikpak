package kr.co.pikpak.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ClientMgmtMapper {


	int insertSupplier(HashMap<String, Object> param);

	int insertVendor(HashMap<String, Object> param);

	List<HashMap<String, Object>> selectList();

	List<HashMap<String, Object>> selectSupplierList();

	List<HashMap<String, Object>> selectVendorList();

	HashMap<String, Object> selectSupplierById(String supplierId);

	HashMap<String, Object> selectVendorById(String vendorId);

	int updateSupplier(HashMap<String, Object> param);

	int updateVendor(HashMap<String, Object> param);

	List<HashMap<String, Object>> searchSupplierByName(HashMap<String, Object> param);

	List<HashMap<String, Object>> searchVendorByName(HashMap<String, Object> param);

	List<HashMap<String, Object>> selectSearchClient(HashMap<String, Object> param);

	List<HashMap<String, Object>> getAllList();

	String getLastSupplierCode();

	String getLastVendorCode();

	int countDupChk(HashMap<String, Object> param);

}
