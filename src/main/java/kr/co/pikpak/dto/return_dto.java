package kr.co.pikpak.dto;

import lombok.Data;

@Data
public class return_dto {
	int return_idx, return_qty, return_price;
	String return_cd, return_id, vendor_nm, return_st, reprocess_wk, outgoing_cd, product_cd, product_nm;
	String return_type, return_type2, reprocess_log, requested_dt, processing_dt;
	String exreceiving_cd, exreceiving_size, operator_id, supplier_nm, supplier_cd, exreceiving_qty;
}
