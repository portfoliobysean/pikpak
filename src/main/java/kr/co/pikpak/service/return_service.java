package kr.co.pikpak.service;

import java.util.List;

import kr.co.pikpak.dto.outgoing_cd_dto;
import kr.co.pikpak.dto.return_dto;
import kr.co.pikpak.dto.return_list_dto;

public interface return_service {
	
	//반품 등록
	int return_enroll(return_dto return_dto);
	
	//출고 코드 조회
	public List<outgoing_cd_dto> outgoing_cd_search(String outgoing_cd);
	
	//출고 코드 중복 조회
	public Integer outgoing_cd_check(String outgoing_cd);
	
	//반품 목록
	public List<return_list_dto> return_list(String user_company);
	
	//반품 목록(전체)
	public List<return_list_dto> return_list_all();
	
	//반품 목록(타입)
	public List<return_list_dto> return_list_type(String return_st, String start_dt, String end_dt, String product_cd, int type, int notall, String user_company);
	
	//반품 승인
	public int return_approval(return_dto return_dto, int type);
	
	//반품 삭제
	public int return_delete(int return_idx);
	
	//재입고
	public int return_restock(return_dto return_dto);
	
	//회사 코드 조회
	public String supplier_cd_search(String supplier_nm);
}
