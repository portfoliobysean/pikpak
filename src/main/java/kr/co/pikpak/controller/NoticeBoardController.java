package kr.co.pikpak.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import kr.co.pikpak.service.NoticeBoardService;

@Controller
@RequestMapping("/notice")
public class NoticeBoardController {

	@Autowired
	private NoticeBoardService noticeBoardService;
	
	@Autowired
	private HttpSession session;

	@GetMapping("/list")	
	public String home(Model model) {
	    try {
	        // 세션에서 operator_id 값을 가져옴
	        String userId = (String) session.getAttribute("activeUserID");
	        if (userId == null) {
	        } else {
	        }

	        // operator_id를 기반으로 operator_lv 값을 조회 (예: 데이터베이스에서 조회)
	        Integer operatorLv = noticeBoardService.getOperatorLevelById(userId);

	        // 조회된 operator_lv 값을 모델에 추가
	        model.addAttribute("operatorLv", operatorLv);
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return "/notice/noticeTable";
	}

	@GetMapping("/write")
	public String home2() {
		return "/notice/writeForm";
	}

	@PostMapping(value = "/regnotice", consumes = "application/json")
	public @ResponseBody HashMap<String, Object> insertNotice(@RequestBody HashMap<String, Object> param) {

		String userId = (String) session.getAttribute("activeUserID");
		
		String operatorNm = noticeBoardService.getOperatorNameByUserId(userId);
		
		String noticeContent = (String) param.get("notice_con"); 
		
		param.put("userId", userId);
		param.put("operatorNm", operatorNm);
		param.put("notice_con", noticeContent);
		
		HashMap<String, Object> returnMap = noticeBoardService.insertNotice(param);
		
		return returnMap;
	}

	@PostMapping(value = "/list", consumes = "application/json")
	public @ResponseBody HashMap<String, Object> selectList(@RequestBody HashMap<String, Object> param) {
		int page = Integer.parseInt(param.getOrDefault("page", "1").toString());
		int size = Integer.parseInt(param.getOrDefault("size", "10").toString());

		int offset = (page - 1) * size;

		List<HashMap<String, Object>> notices = noticeBoardService.selectList(offset, size);

		int totalNotices = noticeBoardService.getTotalNoticeCount();

		HashMap<String, Object> returnMap = new HashMap<>();

		returnMap.put("notices", notices);
		returnMap.put("totalPages", (int) Math.ceil((double) totalNotices / size));

		return returnMap;
	}

	@GetMapping("/{nidx}")
	public String getNoticeById(@PathVariable int nidx, Model model) {
	
		String activeUserID = (String) session.getAttribute("activeUserID");
	    model.addAttribute("activeUserID", activeUserID);
		
		
		HashMap<String, Object> notice = noticeBoardService.getNoticeById(nidx);

		noticeBoardService.incrementViewCount(nidx);

		model.addAttribute("notice", notice);

		return "/notice/noticeDetail"; // 페이지 이동
	}

	@GetMapping("/update/{nidx}")
	public String updateNotice(@PathVariable("nidx") int nidx, Model model) {

		HashMap<String, Object> notice = noticeBoardService.getNoticeById(nidx);
		model.addAttribute("notice", notice); // notice 객체에 nidx가 포함되어 있는지 확인

		return "/notice/updateForm";
	}

	@PostMapping(value = "/updateNotice", consumes = "application/json")
	public @ResponseBody HashMap<String, Object> updateNotice(@RequestBody HashMap<String, Object> param) {

		HashMap<String, Object> returnMap = noticeBoardService.updateNotice(param);

		return returnMap;
	}

}