package com.loja_v.security;

import java.io.IOException;
import java.net.Authenticator;

import javax.servlet.FilterChain;
import javax.servlet.GenericFilter;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

/*Filtro onde todas as requisições serão capturadas para autenticar*/
public class JWTApiAutenticacaoFilter extends GenericFilterBean{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		/*Estabele a autenticao do user*/
		
		Authentication authentication = new JWTTokenAutenticacaoService().
				getAuthetication((HttpServletRequest) request, (HttpServletResponse) response);
		
		/*Coloca o processo de autenticacao para o spring secutiry*/
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		chain.doFilter(request, response);
		
	}


}
