package kr.co.pikpak;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class test_controller {
	@GetMapping("/test.do")
	public String test(){
		return null;
	}
	
	@GetMapping("/index")
	public String indextest() {
		return null;
	}
}
