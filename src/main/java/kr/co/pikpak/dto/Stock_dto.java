package kr.co.pikpak.dto;

import org.springframework.stereotype.Repository;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Repository("Stock_dto ")
public class Stock_dto {
	private Integer wh_warehouse_idx ;
	private String area_cd;
	private String location_cd;
	private String product_cd;
	private String product_nm;
	private String supplier_nm;
	private String product_qty;
	private String inventory_log;
	private String update_dt;
	private String update_by;
}
