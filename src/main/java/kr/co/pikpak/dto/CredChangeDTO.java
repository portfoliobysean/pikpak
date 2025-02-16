package kr.co.pikpak.dto;

import lombok.Data;

@Data
public class CredChangeDTO {
	String user_id, user_pw, new_user_pw;
	
	String user_type;
	
	String target_table;
	
	String target_id, target_pw;
}
