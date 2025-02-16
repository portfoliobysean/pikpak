package kr.co.pikpak.dto;

import lombok.Data;

@Data
public class UserAddDTO {
	String user_id, user_nm, user_pw, user_tel, user_mail, user_type, user_co, company_cd, user_lv;
	
	String old_user_id;
	String target_table;
	
	String target_id, target_nm, target_pw, target_tel, target_mail, target_co, target_comcd, target_type, target_lv;
}
