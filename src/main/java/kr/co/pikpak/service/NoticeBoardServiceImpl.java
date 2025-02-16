package kr.co.pikpak.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.qos.logback.core.recovery.ResilientSyslogOutputStream;
import kr.co.pikpak.mapper.NoticeBoardMapper;

@Service
public class NoticeBoardServiceImpl implements NoticeBoardService {

	@Autowired
	private NoticeBoardMapper noticeBoardMapper;

	@Override
	public HashMap<String, Object> insertNotice(HashMap<String, Object> param) {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			int insertcount = noticeBoardMapper.insertNotice(param);
			if (insertcount > 0) {
				resultMap.put("success_yn", "success");
				resultMap.put("message", "게시글 저장완료");
			} else {
				resultMap.put("success_yn", "fail");
				resultMap.put("message", "게시글 저장실패");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultMap;
	}

	@Override
	public List<HashMap<String, Object>> selectList(int offset, int size) {

		return noticeBoardMapper.selectList(offset, size);
	}

	@Override
	public HashMap<String, Object> getNoticeById(int nidx) {

		return noticeBoardMapper.getNoticeById(nidx);
	}

	@Override
	public void incrementViewCount(int nidx) {

		noticeBoardMapper.incrementViewCount(nidx);
	}

	@Override
	public int getTotalNoticeCount() {

		return noticeBoardMapper.getTotalNoticeCount();
	}

	@Override
	public HashMap<String, Object> updateNotice(HashMap<String, Object> param) {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			int updatecount = noticeBoardMapper.updateNotice(param);

			if (updatecount > 0) {
				resultMap.put("success_yn", "success");
				resultMap.put("message", "수정완료");
			} else {
				resultMap = new HashMap<>();
				resultMap.put("success_yn", "fail");
				resultMap.put("message", "수정한 항목이 없습니다");
			}
		} catch (Exception e) {
			e.printStackTrace();

		}

		return resultMap;
	}

	@Override
	public String getOperatorNameByUserId(String userId) {
		
		return noticeBoardMapper.getOperatorNameByUserId(userId);
	}

	@Override
	public Integer getOperatorLevelById(String userId) {
		return noticeBoardMapper.getOperatorLevelById(userId);
	}

}
