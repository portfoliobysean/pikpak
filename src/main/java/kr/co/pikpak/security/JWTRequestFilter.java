package kr.co.pikpak.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.pikpak.device.CookieUtility;
import kr.co.pikpak.service.LoginService;

@Component
public class JWTRequestFilter extends OncePerRequestFilter{
	@Autowired
	private JWTUtility JWTUtil;
	
	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain)
			throws ServletException, IOException {
		// Do I need this?
		String requestURI = req.getRequestURI();
		if (requestURI.startsWith("/resources/") || requestURI.startsWith("/login") || requestURI.startsWith("/auth")) {
			filterChain.doFilter(req, res);
			return;
		}
		
		// Request 헤더에 Authorization 있는 경우
		String authHeader = req.getHeader("Authorization");
		
		// Request 헤더에 Authorization 없는 경우 
		if (authHeader == null) {
			String accessToken = CookieUtility.getCookie(req, "accessToken");
			if (accessToken != null) {
				authHeader = "Bearer " + accessToken;
				res.setHeader("Authorization", authHeader);
			}
		}

		String userId = null;
		String token = null;
		
		// Authorization 헤더가 존재하고 Bearer로 시작할 때
		if (authHeader != null && authHeader.startsWith("Bearer ")) { //request에 authorization header있는경우
			token = authHeader.substring(7);
			
			// 세션 초과!
            try {
            	userId = JWTUtil.extractUserId(token);
            } catch (ExpiredJwtException e) {
                req.setAttribute("expired", true);
            }
			
            // 유저 이름이 존재하고 현재 인증 정보가 없는 경우
            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            	CustomUserDetails userDetails = userDetailsService.loadUserByUsername(userId);
    				
            	
                // 유효한 토큰일 경우
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
        		//System.out.println(SecurityContextHolder.getContext().getAuthentication());
            }			
		}
		else {	// Request에 authorization 해더 없는 경우
			//System.out.println("Bearer string not found, ignoring the header");
		}

		filterChain.doFilter(req,res);
	}
	
	
}
