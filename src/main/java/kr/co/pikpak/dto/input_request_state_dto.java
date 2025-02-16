package kr.co.pikpak.dto;

import lombok.Data;

@Data
public class input_request_state_dto {
	String request_idx, request_cd, supplier_cd, product_cd, product_nm, total_requested_qty;
	String total_delivered_qty, remaining_qty, add_req, hope_dt, request_st, request_dt, operator_id, operator_nm;
}
