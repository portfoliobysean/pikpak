package kr.co.pikpak.dto;

import lombok.Data;

@Data
public class warehouse_locations_dto_lhwtemp {
	int la_idx;
	String location_cd, max_capacity, current_capacity, supplier_cd, assigned_dt;
}
