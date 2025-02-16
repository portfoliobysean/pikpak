package kr.co.pikpak.dto;

import lombok.Data;

@Data
public class order_enroll_dto_lhwtemp {
	int order_idx, order_qty, order_price;
	String order_cd, order_id, vendor_nm, process_st, start_dt, due_dt, order_dt, update_by, update_dt;
	String product_cd, product_nm;
}
