package com.wj.updater;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 */
@SpringBootApplication
@MapperScan("com.wj.updater.mapper")
public class ClientUpdaterApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientUpdaterApplication.class, args);
	}

}
