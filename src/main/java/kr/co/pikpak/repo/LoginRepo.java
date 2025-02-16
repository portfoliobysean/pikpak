package kr.co.pikpak.repo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.pikpak.dto.LoginDTO;

@Mapper
public interface LoginRepo {
	// 회원 정보
	List<LoginDTO> userDataById(String user_id);
	
	// 회원 등급
	String operatorLvById (String user_id);
}
