package kr.co.pikpak.dto;

import lombok.Data;

@Data
public class accepted_order_enroll_dto {
	int acceptedorder_idx, order_qty;
	String order_cd, order_id, vender_nm, product_cd, product_nm, start_dt, due_dt, acceptedorder_st, update_dt, operator_id;
}
