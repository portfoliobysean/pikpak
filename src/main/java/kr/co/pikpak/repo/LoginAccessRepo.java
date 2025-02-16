package kr.co.pikpak.repo;

import org.apache.ibatis.annotations.Mapper;

import kr.co.pikpak.dto.LoginAccessDTO;

@Mapper
public interface LoginAccessRepo {
	
	void addAccessLog(LoginAccessDTO ldto);
	
	void updateAccessLog(LoginAccessDTO ldto);
}
