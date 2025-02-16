package kr.co.pikpak.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pikpak.mapper.ProductMgmtMapper;

@Service
public class ProductMgmtServiceImpl implements ProductMgmtService {

	@Autowired
	private ProductMgmtMapper productMgmtMapper;

	@Override
	public List<HashMap<String, Object>> searchcompanyname(HashMap<String, Object> param) {

		return productMgmtMapper.searchcompanyname(param);
	}

	@Override
	public HashMap<String, Object> insertProduct(HashMap<String, Object> param) {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			int insertcount = productMgmtMapper.insertProduct(param);
			if (insertcount > 0) {
				resultMap.put("success_yn", "success");
				resultMap.put("message", "상품 등록완료");
			} else {
				resultMap.put("success_yn", "fail");
				resultMap.put("message", "상품에 실패하였습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	@Override
	public List<HashMap<String, Object>> selectAllList() {

		return productMgmtMapper.selectAllList();
	}

	@Override
	public HashMap<String, Object> selectProductInfo(HashMap<String, Object> param) {

		return productMgmtMapper.selectProductInfo(param);
	}

	@Override
	public HashMap<String, Object> updateProduct(HashMap<String, Object> param) {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		try {
			int updateCount = productMgmtMapper.updateProduct(param);
			if (updateCount > 0) {
				resultMap.put("success_yn", "success");
				resultMap.put("message", "수정 완료");
			} else {
				resultMap.put("success_yn", "fail");
				resultMap.put("message", "수정한 항목이 없습니다.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	@Override
	public List<HashMap<String, Object>> selectSearchProduct(HashMap<String, Object> param) {

		return productMgmtMapper.selectSearchProduct(param);
	}

	@Override
	public String generateNewCode(HashMap<String, Object> param) {
	
		String supplierInitial = getSupplierInitial(param);

		String lastCode = getLastCode(param);

		String newCode;
		if (lastCode == null || lastCode.isEmpty()) {
		
			newCode = supplierInitial + "00001";
		} else {
			
			newCode = getNewCode(lastCode, supplierInitial);
		}
		return newCode;
	}

	public String getSupplierInitial(HashMap<String, Object> param) {
		String supplier_cd = (String) param.get("supplier_cd");
		return productMgmtMapper.getSupplierInitial(supplier_cd);
	}

	public String getLastCode(HashMap<String, Object> param) {
		String supplier_cd = (String) param.get("supplier_cd");
		return productMgmtMapper.getLastCodeBySupplier(supplier_cd);
	}

	public String getNewCode(String lastCode, String supplierInitial) {
	
		String codeNumber = lastCode.replaceAll("[^0-9]", ""); 

		int newNumber = Integer.parseInt(codeNumber) + 1;

		return supplierInitial + String.format("%05d", newNumber);
	}



	@Override
	public String getOperatorNameByUserId(String userId) {
		
		return productMgmtMapper.getOperatorNameByUserId(userId);
	}

}
