package kr.co.pikpak.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import kr.co.pikpak.dto.LoginDTO;
import kr.co.pikpak.repo.LoginRepo;

@Service
public class LoginServiceImpl implements LoginService {
	@Autowired
	private LoginRepo lr;
	
	/*
	@Override
	public List<LoginDTO> userDataById(String user_id) {
		List<LoginDTO> result = lr.userDataById(user_id);
		return result;
	}
	*/
	
	@Override
	public UserDetails loadUserByUsername(String user_id) throws UsernameNotFoundException {
		List<LoginDTO> user = lr.userDataById(user_id);
		if (user.size()==0) {
			throw new UsernameNotFoundException("User not found");
		}
		List<GrantedAuthority> authorities = user.stream()
				.map(role -> new SimpleGrantedAuthority(role.getUser_type()))
				.collect(Collectors.toList());
		
		UserDetails result = new User(user.get(0).getUser_id(), user.get(0).getUser_pw(), authorities);
		return result;
	}

	
}
