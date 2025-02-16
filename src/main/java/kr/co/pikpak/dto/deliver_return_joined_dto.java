package kr.co.pikpak.dto;

import lombok.Data;

@Data
public class deliver_return_joined_dto {
	int d_return_idx, exreceiving_qty, d_return_qty;
	String deliver_cd, suppler_cd, product_cd, product_nm, d_return_type;
	String departure_dt, d_return_dt;
}
