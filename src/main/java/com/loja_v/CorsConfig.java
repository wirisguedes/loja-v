package com.loja_v;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer{
	
	 @Override
	    public void addCorsMappings(CorsRegistry registry) {
	        registry.addMapping("/**")
	            // Permite requisições de origens específicas (ajuste conforme necessário)
	            .allowedOrigins("http://localhost:4200", "https://especificque-site.com")
	            // Permite qualquer cabeçalho
	            .allowedHeaders("*")
	            // Permite os métodos HTTP padrão
	            .allowedMethods("GET", "POST", "PUT", "DELETE")
	            // Expõe cabeçalhos específicos se necessário
	            .exposedHeaders("*");
	    }
	

}
