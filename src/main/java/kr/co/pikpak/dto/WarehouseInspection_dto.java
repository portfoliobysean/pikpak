package kr.co.pikpak.dto;

import org.springframework.stereotype.Repository;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Repository("warehouse_check_dto")
public class WarehouseInspection_dto {
	private Integer a_check_idx;
	private String area_cd;
	private String manager_nm;
	private String statement;
	private String temperature;
	private String humidity;
	private String type1_stock;
	private String type2_stock;
	private String type3_stock;
	private String check_log;
	private String check_start_dt;
	private String check_end_dt;
}
