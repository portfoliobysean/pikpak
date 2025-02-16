package kr.co.pikpak.dto;

import org.springframework.stereotype.Repository;

import lombok.Data;

@Data
@Repository("ir_dto")
public class input_request_dto {
	//실제 테이블에 있는 컬럼
	int request_idx, product_qty;
	String request_cd, supplier_cd, supplier_nm, product_cd, product_nm;
	String operator_id, request_st, add_req, hope_dt, request_dt, update_dt, update_id;
	
	//테이블에는 없지만 출력시켜야하는 컬럼
	String operator_nm, update_nm;
	
}
