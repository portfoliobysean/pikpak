package kr.co.pikpak.dto;

import lombok.Data;

@Data
public class outgoing_picking_dto {
	int outpick_idx, outpick_qty;
	String outenroll_cd, wh_warehouose_idx, product_cd, location_cd, receiving_cd;
}
