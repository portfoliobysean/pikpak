package kr.co.pikpak.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import kr.co.pikpak.dto.LoginAccessDTO;
import kr.co.pikpak.repo.LoginAccessRepo;

@Component
public class SessionExpirationListener implements HttpSessionListener{
	@Autowired
	private LoginAccessRepo lar;
	
	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		
		LoginAccessDTO ldto = new LoginAccessDTO();
 		ldto.setUser_id(session.getAttribute("activeUserID").toString()); 
  		ldto.setJsession_id(session.getId());
  		
  		lar.updateAccessLog(ldto);
	}
}
