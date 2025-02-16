package kr.co.pikpak.repo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.pikpak.dto.order_dto;
import kr.co.pikpak.dto.order_list_dto;
import kr.co.pikpak.dto.product_cd_dto;

@Mapper
public interface order_repo {
	//로그인 정보
	public List<order_list_dto> search_login(String activeUserID);
	
	//주문 등록
	int order_enroll(kr.co.pikpak.dto.order_dto order_dto);
	
	//주문 목록
	public List<order_list_dto> order_list(String user_company);
	
	//주문 목록(전체)
	public List<order_list_dto> order_list_all();
	
	//주문 목록(타입)
	public List<order_list_dto> order_list_type(String process_st, String start_dt, String end_dt, String product_cd, int type, int notall, String user_company);
	
	//상품 코드 조회
	public List<product_cd_dto> product_cd_search(String product_cd);
	
	//주문 수정
	public int order_modify(order_dto order_dto, String activeUserID);
	
	//주문 승인
	public int order_approval(order_dto order_dto);
	
	//주문 삭제
	public int order_delete(int order_idx);
	
	//상품 검색
	public List<order_list_dto> product_search();
}
