package kr.co.pikpak.dto;

import lombok.Data;

@Data
public class supplier_info_dto_lhwtemp {
	int supplier_idx;
	String supplier_cd, supplier_nm, rep_nm, supplier_tel, supplier_post;
	String supplier_addr, supplier_addr2, supplier_regno, supplier_regno2;
	String supplier_email, business_type, item_type, registrated_dt;
}
