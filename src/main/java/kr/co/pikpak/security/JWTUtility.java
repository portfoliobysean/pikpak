package kr.co.pikpak.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTUtility {
	//Message : secret
	//Secret-key: 123123
	private String secretKey = "f826e963aea8e14cec02b74f8cc67bb9830adaed1191c0f29a41c38bc558e217";
	
	private int jwtExpiration = 3600000;	//1 HR
	//private int jwtExpiration = 30000;	//30 Sec

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
	
	public String generateToken(CustomUserDetails userDetails) {
		Map<String, Object> subject = new HashMap<>();
        Map<String, Object> claims = new HashMap<>();
        
        subject.put("alg", "HS256");
        subject.put("typ", "JWT");
        claims.put("uid", userDetails.getUserId());
        claims.put("uname", userDetails.getUsername());
        claims.put("utype", userDetails.getUserAuthority());
        
        return createToken(subject, claims);
    }
	
	public String generateOperatorToken(CustomUserDetails userDetails, String operatorLv) {
		Map<String, Object> subject = new HashMap<>();
        Map<String, Object> claims = new HashMap<>();
        
        subject.put("alg", "HS256");
        subject.put("typ", "JWT");
        claims.put("uid", userDetails.getUserId());
        claims.put("uname", userDetails.getUsername());
        claims.put("utype", userDetails.getUserAuthority());
        claims.put("ulevel", operatorLv);
        
        return createToken(subject, claims);
	}
    
    private String createToken(Map<String, Object> subject, Map<String, Object> claims) {
        return Jwts.builder()
        		.setHeader(subject)
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(),SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userId = extractUserId(token);
        return (userId.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
    
	public String extractUserId(String token) {
		final Claims claims = extractAllClaims(token);
		String result = (String) claims.get("uid");
        return result;
    }
	
	public String extractUserLv(String token) {
		final Claims claims = extractAllClaims(token);
		String result = (String) claims.get("ulevel");
        return result;
	}
	
	// 운영자 레벨 호출하는 메소드 추가?
	public Map<String, String> extractUserData(String token){
		Map<String,String> result = new HashMap<>();
		final Claims claims = extractAllClaims(token);
		result.put("uid", (String) claims.get("uid"));
		result.put("uname", (String) claims.get("uname"));
		result.put("utype", (String) claims.get("utype"));
		result.put("ulevel", (String) claims.get("ulevel"));
		
		return result;
	}

	private Claims extractAllClaims(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(getSignInKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    public Date extractExpiration(String token) {
    	return extractClaim(token, Claims::getExpiration);
    }


	
}
