package kr.co.pikpak.controller;

import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.co.pikpak.dto.order_list_dto;
import kr.co.pikpak.dto.outgoing_cd_dto;
import kr.co.pikpak.dto.return_dto;
import kr.co.pikpak.dto.return_list_dto;
import kr.co.pikpak.service.order_service;

@Controller
@RequestMapping("/return")
public class ReturnController {
	
	@Autowired
	private kr.co.pikpak.service.return_service return_service;
	@Autowired
	private kr.co.pikpak.service.order_service order_service;
	
	PrintWriter pw = null;
	
	//반품 승인 리스트 페이지
	@GetMapping("/return_aplist")
	public String return_aplist(Model m) {
		List<return_list_dto> return_alllist = return_service.return_list_all();
		List<order_list_dto> product_list = order_service.product_search();
		m.addAttribute("return_cklist",return_alllist);
		m.addAttribute("product_list",product_list);
		
		return "/return/return_aplist";
	}	
	
	//반품 리스트 페이지 - 아이디 값에서 회사명 받아오기
	@GetMapping("/return_list")
	public String return_list(Model m, HttpSession sess) {
		String activeUserID = (String)sess.getAttribute("activeUserID");
		List<order_list_dto> member_list = order_service.search_login(activeUserID);
		String user_company = member_list.get(0).getUser_company();
		
		//물류사일 경우 전체 리스트 출력
		if(user_company.equals("PikPak")) {
			List<return_list_dto> return_alllist = return_service.return_list_all();
			m.addAttribute("return_cklist",return_alllist);
		}
		//물류사가 아닐 경우 자신의 회사 리스트만 출력
		else {
			List<return_list_dto> return_cklist = return_service.return_list(user_company);
			m.addAttribute("return_cklist",return_cklist);			
		}
		List<order_list_dto> product_list = order_service.product_search();
		
		m.addAttribute("activeUserID",activeUserID);
		m.addAttribute("member_list",member_list);
		m.addAttribute("product_list",product_list);
		
		return "/return/return_list";
	}

	//반품 승인
	@PostMapping("/return_approval_change")
	public String return_approval_change(@ModelAttribute return_dto retrun_dto,
			ServletResponse res) {
		res.setContentType("text/html;charset=utf-8");
		int result = 0;
		int result2 = 0;
		int type = 0;
		
		//대기 상태
		if(retrun_dto.getReturn_st().equals("승인") || retrun_dto.getReturn_st().equals("거절")) {
			type = 1;
			result = return_service.return_approval(retrun_dto, type);
		}
		//승인 상태
		else if(retrun_dto.getReturn_st().equals("완료")){
			//supplier_cd 값 찾기
			String supplier_nm = retrun_dto.getSupplier_nm();
			String supplier_cd = return_service.supplier_cd_search(supplier_nm);
			retrun_dto.setSupplier_cd(supplier_cd);

			String reprocess_wk = retrun_dto.getReprocess_wk();
			type = 2;
			result = return_service.return_approval(retrun_dto, type);
			if(reprocess_wk.equals("재입고")) {
				String for_exreceiving_cd = "ER-";
				LocalDate ymd = LocalDate.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
				String formattedDate = ymd.format(formatter);
				for(int f=0; f<4; f++) {
					int ran = (int)(Math.random()*10);
					formattedDate += ran;
				}
				String exreceiving_cd = for_exreceiving_cd + formattedDate;
				retrun_dto.setExreceiving_cd(exreceiving_cd);
				
				result2 = return_service.return_restock(retrun_dto);
			}
		}
		try {
			this.pw = res.getWriter();
			if(result > 0 && result2 > 0) {
				this.pw.print("<script>"
						+ "alert('반품 승인 정보가 변경 및 재입고 처리가 완료되었습니다.');"
						+ "location='/return/return_aplist';"
						+ "</script>");
			}
			else if(result >0 && result2 == 0) {
				this.pw.print("<script>"
						+ "alert('반품 승인 정보가 변경되었습니다.');"
						+ "location='/return/return_aplist';"
						+ "</script>");
			}
			else {
				this.pw.print("<script>"
						+ "alert('오류로 인하여 반품 승인 정보 변경을 실패하였습니다.');"
						+ "location='/return/return_aplist';"
						+ "</script>");
			}
		}catch(Exception e) {
			this.pw.print("<script>"
					+ "alert('오류로 인하여 반품 승인 정보 변경을 실패하였습니다.');"
					+ "location='/return/return_aplist';"
					+ "</script>");
		}
		finally {
			this.pw.close();
		}
		
		return null;
	}
	
	//반품 취소
	@PostMapping("/return_delete")
	public String return_delete(@RequestParam(value="return_idx") int return_idx,
			ServletResponse res) {
		res.setContentType("text/html;charset=utf-8");
		int result = return_service.return_delete(return_idx);
		try {
			this.pw = res.getWriter();
			if(result > 0) {
				this.pw.print("<script>"
						+ "alert('정상적으로 반품 신청이 취소되었습니다.');"
						+ "location='/return/return_list';"
						+ "</script>");
			}
			else {
				this.pw.print("<script>"
						+ "alert('오류로 인하여 반품 신청 취소를 실패하였습니다.');"
						+ "location='/return/return_list';"
						+ "</script>");
			}
		}catch(Exception e) {
			this.pw.print("<script>"
					+ "alert('오류로 인하여 반품 신청 취소를 실패하였습니다.');"
					+ "location='/return/return_list';"
					+ "</script>");
		}
		finally {
			this.pw.close();
		}
		
		return null;
	}
	
	
	//출구 코드 조회
	@PostMapping("/outgoing_cd_searchck")
	@ResponseBody
	public String outgoing_cd_searchck(@RequestParam(value="outgoing_cd", required=false)
	String outgoing_cd) {
		String result = "";
		//출고 코드 중복확인
		Integer check = return_service.outgoing_cd_check(outgoing_cd);
		if(check != null) {
			result = "overlap";
		}
		else {
			List<outgoing_cd_dto> outgoing_list = return_service.outgoing_cd_search(outgoing_cd);
			
			if(outgoing_list.size() > 0) {
				StringBuilder ol = new StringBuilder();
				for(outgoing_cd_dto outgoing : outgoing_list) {
					ol.append(outgoing.getProduct_cd()).append(",");
					ol.append(outgoing.getProduct_nm()).append(",");
					ol.append(outgoing.getSupplier_nm()).append(",");
					ol.append(outgoing.getOutgoing_dt()).append(",");
					ol.append(outgoing.getTotal_qty()).append(",");
					ol.append(outgoing.getPurchase_pr());
				}
				result = ol.toString();
			}
			else if(outgoing_list.size() == 0) {
				result = "no";			
			}
		}
		
		return result;
	}
	
	//반품등록
	@PostMapping("/return_enroll")
	public String return_enroll(@ModelAttribute kr.co.pikpak.dto.return_dto return_dto,
			ServletResponse res) {
		res.setContentType("text/html;charset=utf-8");
		String for_return_cd = "RT-";
		LocalDate ymd = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
		String formattedDate = ymd.format(formatter);
		for(int f=0; f<4; f++) {
			int ran = (int)(Math.random()*10);
			formattedDate += ran;
		}
		String return_cd = for_return_cd + formattedDate;
		return_dto.setReturn_cd(return_cd);
		
		int result = return_service.return_enroll(return_dto);
		try {
			this.pw = res.getWriter();
			if(result > 0) {
				this.pw.print("<script>"
						+ "alert('반품 등록이 완료되었습니다.');"
						+ "location='/return/return_list';"
						+ "</script>");
			}
			else {
				this.pw.print("<script>"
						+ "alert('오류로 인하여 반품 등록을 실패하였습니다.');"
						+ "location='/return/return_list';"
						+ "</script>");
			}
		}catch(Exception e) {
			this.pw.print("<script>"
					+ "alert('오류로 인하여 반품 등록을 실패하였습니다.');"
					+ "location='/return/return_list';"
					+ "</script>");
		}
		finally {
			this.pw.close();
		}
		
		
		return null;
	}
	
}
