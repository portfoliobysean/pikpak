package kr.co.pikpak.dto;

import org.springframework.stereotype.Repository;

import lombok.Getter;
import lombok.Setter;

//재고 리스트 
@Getter
@Setter
@Repository("inventory_dto")
public class WarehouseInventory_dto {
	private Integer wh_warehouse_idx ;
	private String location_cd;
	private String location_sz;
	private String product_cd;
	private String product_nm;
	private String product_qty;
	private String supplier_nm;
	private String inventory_log;
	private String update_by;
	private String update_dt;
	
}
