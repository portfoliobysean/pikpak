package kr.co.pikpak.dto;

import lombok.Data;

@Data
public class receiving_dto {
	Integer receiving_idx, receiving_qty;
	String deliver_cd, exreceiving_cd, receiving_cd, lot_no, supplier_cd, product_cd, product_nm, receiving_size, location_cd;
	String operator_id, inventory_dt, receiving_dt, receiving_log;
	
	//테이블에는 없음, 로트번호 만들기 위함
	String make_dt;
	String exreceiving_type; //납품인지 반품인지에 따라 로트번호 생성 다름
	
	//테이블에 없음 그냥 집어넣으려고 받아옴
	String supplier_nm;
	
	
}
