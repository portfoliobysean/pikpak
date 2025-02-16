package kr.co.pikpak.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.co.pikpak.device.CookieUtility;
import kr.co.pikpak.dto.LoginAccessDTO;
import kr.co.pikpak.dto.LoginDTO;
import kr.co.pikpak.repo.LoginAccessRepo;
import kr.co.pikpak.security.CustomUserDetails;
import kr.co.pikpak.security.CustomUserDetailsService;
import kr.co.pikpak.security.JWTUtility;
import kr.co.pikpak.service.LoginService;

@RestController
public class LoginController {
	@Resource(name="LoginDTO")
	LoginDTO ldto;
	
	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@Autowired
	private HttpSession session;
	
    @Autowired
    private AuthenticationManager authenticationManager;
	
	@Autowired
	private LoginAccessRepo lar;
    
    @Autowired 
    private JWTUtility JWTUtil;
    
	
	PrintWriter pw = null;
	
	// 회원 로그인 및 토큰 생성
	@PostMapping("/login/auth")
	public ResponseEntity<?> createAuthenticationToken(LoginDTO logindto, HttpServletRequest req, HttpServletResponse res, HttpSession sess) throws Exception {
		// Fetch API 결과 핸들링
		String responseMsg = "";
		Date expiryDate = null;
		Map<String, Object> authResponse = new HashMap<>();
		
		try {
			// 회원 정보 확인
			Authentication authenticate = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(logindto.getUser_id(), logindto.getUser_pw())
			);
			//SecurityContextHolder.getContext().setAuthentication(authenticate);
			
			// 회원 DB 접속
			final CustomUserDetails userDetails = userDetailsService.loadUserByUsername(logindto.getUser_id());
			
			// 생성할 JWT 토큰
			String token = null;
			
			// 회원이 운영자 일경우 운영자 권한 레벨 까지 저장
			if (userDetails.getUserAuthority().equals("operator") || userDetails.getUserAuthority().equals("admin")) {
				String operatorLv = userDetailsService.operatorLvByUserId(logindto.getUser_id());
				token = JWTUtil.generateOperatorToken(userDetails, operatorLv);
			}
			else {
				token = JWTUtil.generateToken(userDetails);
			}
			
			// 로그인 유호기간
			expiryDate = JWTUtil.extractExpiration(token);
			
			if (expiryDate != null) {
				authResponse.put("expiryTime", expiryDate.getTime());
			}
			else {
				throw new BadCredentialsException("Expiry Date is null");
			}
			
			// 클라이언트 사이드 토큰 저장 (Request 해더 미사용시 대신 사용)
			CookieUtility.setCookie(res, "accessToken", token, "/");
			
			// 세션에 아이디 정보 저장
			sess.setAttribute("activeUserID", logindto.getUser_id());
			sess.setMaxInactiveInterval(60*60);
			
			// 로그인 로그기록 등록
			LoginAccessDTO ldto = new LoginAccessDTO();
			ldto.setUser_id(logindto.getUser_id());
			ldto.setJsession_id(sess.getId());
			lar.addAccessLog(ldto);
			
			responseMsg = "Y";
		} catch (BadCredentialsException e) {
			responseMsg = "회원정보가 일치하지 않습니다";
		} catch (Exception e) {
			responseMsg = "서버 문제로 로그인이 실패하였습니다. 관리자에게 문의하세요";
		}
		
		authResponse.put("responseMsg", responseMsg);
		
		return ResponseEntity.ok(authResponse);
	}
	
	// 이 경로는 따로 접속 차단 필수
	@GetMapping("/login/refresh")
	public ResponseEntity<?> refreshAuthenticationToken (HttpServletRequest req, HttpServletResponse res, HttpSession sess){
		String responseMsg = "N";
		String token = CookieUtility.getCookie(req, "accessToken");
		String refreshToken = null;
		Date expiryDate = null;
		
		if (token == null || JWTUtil.isTokenExpired(token)) {
			responseMsg = "expired";
			// or just redirect here
		}
		else {
			try {
				String activeUserId = JWTUtil.extractUserId(token);
				final CustomUserDetails userDetails = userDetailsService.loadUserByUsername(activeUserId);
				if (userDetails.getUserAuthority().equals("operator") || userDetails.getUserAuthority().equals("admin")) {
					String operatorLv = JWTUtil.extractUserLv(token);
					refreshToken = JWTUtil.generateOperatorToken(userDetails, operatorLv);
				}
				else {
					refreshToken = JWTUtil.generateToken(userDetails);
				}
				
				expiryDate = JWTUtil.extractExpiration(refreshToken);
				
				CookieUtility.setCookie(res, "accessToken", refreshToken, "/");
				
				sess.setAttribute("activeUserID", activeUserId);
				sess.setMaxInactiveInterval(60*60*24+7);
				
				responseMsg = "Y";
			} catch (Exception e) {
				e.printStackTrace();
				responseMsg = "invalid";
			}
			
		}
		
		Map<String, Object> authResponse = new HashMap<>();
		authResponse.put("responseMsg", responseMsg);
		authResponse.put("expiryTime", expiryDate.getTime());
		
		return ResponseEntity.ok(authResponse);
	}
	
	
	
	
}
