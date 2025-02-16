package kr.co.pikpak.dto;

import lombok.Data;

@Data
public class LoginAccessDTO {
	String access_idx;
	String login_dt, logout_dt;
	String jsession_id;
	String user_id;
}
