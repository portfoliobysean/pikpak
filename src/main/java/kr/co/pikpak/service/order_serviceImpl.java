package kr.co.pikpak.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pikpak.dto.order_dto;
import kr.co.pikpak.dto.order_list_dto;
import kr.co.pikpak.dto.product_cd_dto;
import kr.co.pikpak.repo.order_repo;

@Service
public class order_serviceImpl implements order_service{
	
	@Autowired
	private order_repo order_repo;
	
	@Override
	public List<order_list_dto> search_login(String activeUserID) {
		List<order_list_dto> list = order_repo.search_login(activeUserID);
		return list;
	}
	
	@Override
	public int order_enroll(order_dto order_dto) {
		int result = order_repo.order_enroll(order_dto);
		
		return result;
	}
	
	@Override
	public List<order_list_dto> product_search() {
		List<order_list_dto> list = order_repo.product_search();
		return list;
	}
	
	@Override
	public List<order_list_dto> order_list(String user_company) {
		List<order_list_dto> list = order_repo.order_list(user_company);
		
		return list;
	}
	
	@Override
	public List<order_list_dto> order_list_all() {
		List<order_list_dto> alllist = order_repo.order_list_all();
		return alllist;
	}
	
	@Override
	public List<order_list_dto> order_list_type(String process_st, String start_dt, String end_dt, String product_cd,
			int type, int notall, String user_company) {
		List<order_list_dto> typelist = order_repo.order_list_type(process_st, start_dt, end_dt, product_cd, type, notall, user_company);
		return typelist;
	}
	
	@Override
	public List<product_cd_dto> product_cd_search(String product_cd) {
		List<product_cd_dto> product_list = order_repo.product_cd_search(product_cd);
		
		return product_list;
	}
	
	@Override
	public int order_delete(int order_idx) {
		int result = order_repo.order_delete(order_idx);
		return result;
	}
	
	@Override
	public int order_modify(order_dto order_dto, String activeUserID) {
		int result = order_repo.order_modify(order_dto, activeUserID);
		
		return result;
	}
	
	@Override
	public int order_approval(order_dto order_dto) {
		int result = order_repo.order_approval(order_dto);
		
		return result;
	}

}
