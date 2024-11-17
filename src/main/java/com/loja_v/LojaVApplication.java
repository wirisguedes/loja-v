package com.loja_v;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan(basePackages = "com.loja_v.model")
@ComponentScan(basePackages = {"com.*"})
@EnableJpaRepositories(basePackages = "com.loja_v.repository")
@EnableTransactionManagement
public class LojaVApplication {

	public static void main(String[] args) {
		
		//System.out.println(new BCryptPasswordEncoder().encode("123")); /*Gerar senha crip*/
		
		SpringApplication.run(LojaVApplication.class, args);
	}
 
}
