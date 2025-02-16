package kr.co.pikpak.security;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import kr.co.pikpak.dto.LoginDTO;

public class CustomUserDetails implements UserDetails{
	
	String userId, userName, userPassword, userType;
	 
	public CustomUserDetails(List<LoginDTO> userData) {
		this.userId = userData.get(0).getUser_id();
		this.userName = userData.get(0).getUser_nm();
		this.userPassword = userData.get(0).getUser_pw();
		this.userType = userData.get(0).getUser_type();
	}
	
	public String getUserId() {
		return this.userId;
	}
	
	public String getUserAuthority() {
		return this.userType;
	}
	
	@Override
	public String getPassword() {
		return this.userPassword;
	}
	
	@Override
	public String getUsername() {
		return this.userName;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority(this.userType));
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return false;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return false;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}
	
	@Override
	public boolean isEnabled() {
		return false;
	}
}
