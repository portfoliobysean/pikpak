package kr.co.pikpak.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public interface NoticeBoardService {

	HashMap<String, Object> insertNotice(HashMap<String, Object> param);

	List<HashMap<String, Object>> selectList(int offset, int page);

	void incrementViewCount(int nidx);

	HashMap<String, Object> getNoticeById(int nidx);

	int getTotalNoticeCount();

	HashMap<String, Object> updateNotice(HashMap<String, Object> param);

	String getOperatorNameByUserId(String userId);

	Integer getOperatorLevelById(String userId);

}
