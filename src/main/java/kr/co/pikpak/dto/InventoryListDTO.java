package kr.co.pikpak.dto;

import org.springframework.stereotype.Repository;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Repository("InventoryListDTO")
public class InventoryListDTO {
    private int wh_warehouse_idx;
    private String location_cd;
    private String product_cd;
    private String product_nm;
    private int product_qty;
    private int safetyinventory_qty;
    private int product_price;
    private String company_name;
    private String product_type;
}
