package kr.co.pikpak;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration	//환경설정
@PropertySource("classpath:/application.properties")	//properties를 로드
public class dbinfos {
	
	@ConfigurationProperties(prefix="spring.datasource")	//properties에서 사용하는 클래스명
	
	@Bean	//bean을 이용하여 환경설정 연결(=config.xml)
	public DataSource datasource() {
		
		return DataSourceBuilder.create().build();
	}
}