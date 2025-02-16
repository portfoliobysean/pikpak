package kr.co.pikpak.dto;

import lombok.Data;

@Data
public class UserTraderDTO {
	String trader_idx;
	String trader_nm, trader_id, trader_pw, trader_tel;
	String trader_mail, trader_co, user_type;
	String registrated_dt;
	String pw_change_yn;
}
