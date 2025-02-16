package kr.co.pikpak.dto;

import lombok.Data;

@Data
public class ex_receiving_dto {
	int exreceiving_idx, exreceiving_qty, return_qty;
	String request_cd, deliver_cd, return_cd, supplier_cd, exreceiving_cd, product_cd, exreceiving_st, exreceiving_area;
	String exreceiving_size, make_dt, departure_dt, processing_dt, exreceiving_type, operator_id, update_dt, update_id;
}
