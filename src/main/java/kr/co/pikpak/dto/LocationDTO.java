package kr.co.pikpak.dto;

import lombok.Data;

@Data
public class LocationDTO {
	Integer la_idx;
	String location_cd;
	String supplier_cd;
	Integer max_capacity;
	Integer current_capacity;
	String assigned_dt;
	
    Boolean isOccupied; // 현재 위치가 사용 중인지 여부
    Boolean hasStock;    // 해당 위치에 재고가 있는지 여부	
    
    public void setOccupiedAndStockStatus() {
        this.isOccupied = (this.current_capacity != null && this.current_capacity > 0);
        this.hasStock = (this.current_capacity != null && this.current_capacity > 0);
    }
}
