package kr.co.pikpak.dto;

import lombok.Data;

@Data
public class product_dto_lhwtemp {
	int product_idx, purchase_pr, safetyinventory_qty;
	String supplier_cd, product_cd, product_nm, product_img, operator_nm, registrated_dt;
	String update_by, lastmodified_at, visibility_yn, product_log;
}
