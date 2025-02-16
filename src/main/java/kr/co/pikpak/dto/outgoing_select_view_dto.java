package kr.co.pikpak.dto;

import lombok.Data;

@Data
public class outgoing_select_view_dto {
	String wh_warehouse_idx, receiving_cd, product_cd, product_nm, lot_no, location_cd, product_qty,is_deleted;
}
