package kr.co.pikpak.dto;

import lombok.Data;

@Data
public class order_list_dto {
	int order_idx, order_qty, order_price, purchase_pr;
	String order_cd, process_st, start_dt, due_dt, order_dt, update_by, update_dt, product_cd, order_id;
	String user_nm, user_tel, user_company, product_nm, product_sz, product_wt, supplier_nm;
	String approval_log, approval_dt;
}
