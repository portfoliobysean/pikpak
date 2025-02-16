package kr.co.pikpak.dto;

import lombok.Data;

@Data
public class UserOperatorDTO {
	String operator_idx;
	String operator_nm, operator_id, operator_pw, operator_tel;
	String operator_mail, operator_co, user_type, operator_lv;
	String registrated_dt;
	String pw_change_yn;
}
