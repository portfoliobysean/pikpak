package kr.co.pikpak.dto;

import org.springframework.stereotype.Repository;

import lombok.Data;

@Data
@Repository("deliver_dto")
public class deliver_enroll_dto {
	int deliver_idx, deliver_qty;
	String request_cd, supplier_cd, deliver_cd, product_cd, product_nm, deliver_st, make_dt, expect_dt;
	String deliver_size, deliver_area, operator_id, update_id, deliver_dt, departure_dt;
}
