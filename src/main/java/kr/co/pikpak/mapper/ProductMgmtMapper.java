package kr.co.pikpak.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMgmtMapper {

	List<HashMap<String, Object>> searchcompanyname(HashMap<String, Object> param);

	int insertProduct(HashMap<String, Object> param);

	List<HashMap<String, Object>> selectAllList();

	HashMap<String, Object> selectProductInfo(HashMap<String, Object> param);

	int updateProduct(HashMap<String, Object> param);

	List<HashMap<String, Object>> selectSearchProduct(HashMap<String, Object> param);

	String getSupplierInitial(String supplier_cd);

	String getLastCodeBySupplier(String supplier_cd);

	String getOperatorNameByUserId(String userId);

	

}
