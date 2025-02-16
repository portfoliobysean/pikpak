package kr.co.pikpak.dto;

import lombok.Data;

@Data
public class return_list_dto {
	int return_idx, return_qty, return_price, total_qty, purchase_pr;
	String return_id, return_st, return_cd, return_type, return_type2, outgoing_cd;
	String user_nm, user_company, user_tel, product_cd, outgoing_dt, reprocess_log;
	String product_nm, supplier_nm, reprocess_wk, requested_dt, processing_dt, approval_log, approval_dt;
}
