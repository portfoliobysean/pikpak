package kr.co.pikpak.dto;

import lombok.Data;

@Data
public class warehouse_dto_lhwtemp {
	int wh_warehouse_idx, product_qty, intransit_qty, allocated_qty, incoming_qty;
	String location_cd, product_cd, product_nm, supplier_nm, inventory_log, update_dt, update_by, supplier_cd;
	
}
