package kr.co.pikpak.dto;

import lombok.Data;

@Data
public class order_dto {
	int order_idx, order_qty, order_price;
	String order_cd, order_id, vendor_nm, process_st, start_dt, due_dt, approval_log;
	String order_dt, update_by, update_dt, approval_dt, product_cd, product_nm;
}
