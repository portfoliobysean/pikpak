package kr.co.pikpak.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public interface StockLogRecordService {

	List<HashMap<String, Object>> selectList();

	List<HashMap<String, Object>> selectLogSearch(HashMap<String, Object> param);

}
