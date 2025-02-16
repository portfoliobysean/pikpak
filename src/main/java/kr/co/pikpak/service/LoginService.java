package kr.co.pikpak.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import kr.co.pikpak.dto.LoginDTO;

public interface LoginService extends UserDetailsService{
	//List<LoginDTO> userDataById(String user_id);
	UserDetails loadUserByUsername(String user_id);
}
