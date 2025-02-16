package kr.co.pikpak.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StockLogRecordMapper {

	List<HashMap<String, Object>> selectList();

	List<HashMap<String, Object>> selectLogSearch(HashMap<String, Object> param);

}
