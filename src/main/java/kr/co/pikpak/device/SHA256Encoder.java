package kr.co.pikpak.device;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SHA256Encoder implements PasswordEncoder{
	@Override
	public String encode(CharSequence rawPassword) {
		return DigestUtils.sha256Hex(rawPassword.toString());
	}
	
	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		String hashedPassword = encode(rawPassword);
		return hashedPassword.equals(encodedPassword);
	}
}
