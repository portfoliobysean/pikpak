package kr.co.pikpak.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pikpak.mapper.StockLogRecordMapper;

@Service
public class StockLogRecordServiceImpl implements StockLogRecordService {

	@Autowired
	StockLogRecordMapper stockLogRecordMapper;

	@Override
	public List<HashMap<String, Object>> selectList() {
		
		return stockLogRecordMapper.selectList();
	}

	@Override
	public List<HashMap<String, Object>> selectLogSearch(HashMap<String, Object> param) {
		
		return stockLogRecordMapper.selectLogSearch(param);
	}
}
