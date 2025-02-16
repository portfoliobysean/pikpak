package kr.co.pikpak.dto;

import lombok.Data;

@Data
public class outgoing_cd_dto {
	int total_qty, purchase_pr;
	String product_cd, product_nm, supplier_nm, outgoing_dt;
}
