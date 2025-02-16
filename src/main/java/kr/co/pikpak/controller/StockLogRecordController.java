package kr.co.pikpak.controller;

import java.util.HashMap;
import java.util.List;

import javax.print.attribute.HashPrintJobAttributeSet;

import org.apache.commons.net.imap.IMAPClient.STATUS_DATA_ITEMS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.pikpak.service.StockLogRecordService;

@Controller
@RequestMapping("/stocklogrecord")
public class StockLogRecordController {

	@Autowired
	StockLogRecordService stockLogRecordService;
	
	@GetMapping("/list")
	public String home() {
		return "/stocklogrecord/stocklog";
	}
	
	@PostMapping(value="/list", consumes="application/json")
	public @ResponseBody List<HashMap<String, Object>> getList(){
		
		return stockLogRecordService.selectList();
	}
	
	@PostMapping(value="/search", consumes="application/json")
	public @ResponseBody List<HashMap<String, Object>> searchLog(@RequestBody HashMap<String, Object> param){
		
		List<HashMap<String, Object>> returnMap = stockLogRecordService.selectLogSearch(param);
		
		return returnMap;
	}
	
}
