package kr.co.pikpak.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoticeBoardMapper {

	int insertNotice(HashMap<String, Object> param);

	List<HashMap<String, Object>> selectList(int offset, int size);

	String viewpage(int nidx);

	HashMap<String, Object> getNoticeById(int nidx);

	void incrementViewCount(int nidx);

	int getTotalNoticeCount();

	int updateNotice(HashMap<String, Object> param);

	String getOperatorNameByUserId(String userId);

	Integer getOperatorLevelById(String userId);

}
