package kr.co.pikpak.dto;

import lombok.Data;

@Data
public class deliver_return_dto {
	int d_return_idx;
	String deliver_cd, exreceiving_cd, d_return_cd, d_return_qty, d_return_type, d_return_dt, d_enroll_dt, operator_id;
}
