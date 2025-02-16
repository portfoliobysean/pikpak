package kr.co.pikpak.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import kr.co.pikpak.dto.LoginDTO;
import kr.co.pikpak.repo.LoginRepo;

@Service
public class CustomUserDetailsService implements UserDetailsService{
	@Autowired
	private LoginRepo lr;
	
	@Override
	public CustomUserDetails loadUserByUsername(String user_id) throws UsernameNotFoundException {
		List<LoginDTO> user = lr.userDataById(user_id);
		
		if (user.size()==0) {
			throw new UsernameNotFoundException("사용자 정보 찾을 수 없음");
		}
		
		return new CustomUserDetails(user);
	}
	
	public String operatorLvByUserId(String user_id) {
		String result = lr.operatorLvById(user_id);
		return result;
	}
	
    public String userNameFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        return null;
    }
    
    public String userTypeFromContext() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
        	CustomUserDetails activeUserDetails = ((CustomUserDetails) authentication.getPrincipal());
        	String result = activeUserDetails.getUserAuthority();
            return result;
        }
    	return null;
    }
}
