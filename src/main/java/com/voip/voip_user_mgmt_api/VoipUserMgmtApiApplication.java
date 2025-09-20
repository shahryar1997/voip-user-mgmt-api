package com.voip.voip_user_mgmt_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.voip.voip_user_mgmt_api.config.SecurityConfig;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class VoipUserMgmtApiApplication {


	public static void main(String[] args) {
		SpringApplication.run(VoipUserMgmtApiApplication.class, args);
////		BCryptPasswordEncoder encoder = SecurityConfig.passwordEncoder();
//		String rawPassword = "password123";
//		String encodedPassword = passwordEncoder.encode(rawPassword);
//		System.out.println("Encoded password for '" + rawPassword + "': " + encodedPassword);
	}

}
