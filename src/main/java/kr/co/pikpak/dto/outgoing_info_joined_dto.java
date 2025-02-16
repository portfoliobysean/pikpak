package kr.co.pikpak.dto;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class outgoing_info_joined_dto {
	int total_qty;
	String outenroll_cd, order_cd, product_cd, product_nm, expect_dt;
	String exoutgoing_area, outenroll_log, outenroll_st;
	
	List<Map<String, Object>> pickings;
	String pickings_json;
	//wh_warehouse_idx, receivig_cd, lot_no, outpick_qty, is_deleted, location_cd
}
