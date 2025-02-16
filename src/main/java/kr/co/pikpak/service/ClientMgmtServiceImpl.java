package kr.co.pikpak.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pikpak.mapper.ClientMgmtMapper;

@Service
public class ClientMgmtServiceImpl implements ClientMgmtService {

	@Autowired
	private ClientMgmtMapper clientMgmtMapper;

	@Override
	public HashMap<String, Object> insertSupplier(HashMap<String, Object> param) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			int insertcount = clientMgmtMapper.insertSupplier(param);
			if (insertcount > 0) {
				resultMap.put("success_yn", "success");
				resultMap.put("message", "등록완료");
			} else {
				resultMap.put("success_yn", "fail");
				resultMap.put("message", "등록실패");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	@Override
	public HashMap<String, Object> insertVendor(HashMap<String, Object> param) {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			int insertcount = clientMgmtMapper.insertVendor(param);
			if (insertcount > 0) {
				resultMap.put("success_yn", "success");
				resultMap.put("message", "등록완료");
			} else {
				resultMap = new HashMap<>();
				resultMap.put("success_yn", "fail");
				resultMap.put("message", "등록실패");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	@Override
	public List<HashMap<String, Object>> selectList() {

		return clientMgmtMapper.selectList();
	}

	@Override
	public List<HashMap<String, Object>> getSupplierList() {

		return clientMgmtMapper.selectSupplierList();
	}

	@Override
	public List<HashMap<String, Object>> getVendorList() {

		return clientMgmtMapper.selectVendorList();
	}

	@Override
	public HashMap<String, Object> getSupplierById(String supplierId) {
		
		return clientMgmtMapper.selectSupplierById(supplierId);
	}

	@Override
	public HashMap<String, Object> getVendorById(String vendorId) {
		
		return clientMgmtMapper.selectVendorById(vendorId);
	}

	@Override
	public HashMap<String, Object> updateSupplier(HashMap<String, Object> param) {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			int updateCount = clientMgmtMapper.updateSupplier(param);
			if (updateCount > 0) {
				resultMap.put("success_yn", "success");
				resultMap.put("message", "수정 완료");

			} else {
				resultMap.put("success_yn", "fail");
				resultMap.put("message", "수정한 항목이 없습니다");

			}
		} catch (Exception e) {
			resultMap.put("success_yn", "fail");
			resultMap.put("message", "수정 실패: " + e.getMessage());
		}

		return resultMap;
	}

	@Override
	public HashMap<String, Object> updateVendor(HashMap<String, Object> param) {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			int updateCount = clientMgmtMapper.updateVendor(param); // 업데이트된 행의 개수를 반환
			if (updateCount > 0) {
				resultMap.put("success_yn", "success");
				resultMap.put("message", "수정 완료");

			} else {
				resultMap.put("success_yn", "fail");
				resultMap.put("message", "수정한 항목이 없습니다");

			}
		} catch (Exception e) {
			resultMap.put("success_yn", "fail");
			resultMap.put("message", "수정 실패: " + e.getMessage());
		}

		return resultMap;
	}

	@Override
	public List<HashMap<String, Object>> selectSearchClient(HashMap<String, Object> param) {

		return clientMgmtMapper.selectSearchClient(param);
	}

	@Override
	public List<HashMap<String, Object>> searchSupplierByName(HashMap<String, Object> param) {

		return clientMgmtMapper.searchSupplierByName(param);
	}

	@Override
	public List<HashMap<String, Object>> searchVendorByName(HashMap<String, Object> param) {

		return clientMgmtMapper.searchVendorByName(param);
	}

	@Override
	public List<HashMap<String, Object>> getAllList() {

		return clientMgmtMapper.getAllList();
	}

	@Override
	public String getLastCode(String clientType) {

		if ("supplier".equals(clientType)) {
			return clientMgmtMapper.getLastSupplierCode();
		} else {
			return clientMgmtMapper.getLastVendorCode();
		}
	}

	@Override
	public String getNewCode(String lastCode) {
		
		String prefix = lastCode.replaceAll("[0-9]", ""); 
		String numberPart = lastCode.replaceAll("[^0-9]", ""); 

		int newNumber = Integer.parseInt(numberPart) + 1;

		return prefix + String.format("%05d", newNumber);
	}

	@Override
	public HashMap<String, Object> countDupChk(HashMap<String, Object> param) {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			int count = clientMgmtMapper.countDupChk(param);
			if (count > 0) {
				resultMap.put("success_yn", "fail");
				resultMap.put("message", "이미 존재하는 이니셜입니다.");
			} else {
				resultMap.put("success_yn", "success");
				resultMap.put("message", "사용 가능한 이니셜입니다.");
			}
		} catch (Exception e) {
		}

		return resultMap;
	}
}
