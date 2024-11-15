package com.loja_v.security;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.loja_v.ApplicationContextLoad;
import com.loja_v.model.Usuario;
import com.loja_v.repository.UsuarioRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


/*Criar autenticação e retornar a autenticação JWT*/
@Service
@Component
public class JWTTokenAutenticacaoService {
	
	/*Token de validade 11 dias*/
	private static final long EXPIRATION_TIME = 259990000;
	
	/*Chave senha para juntar com JWT*/
	private static final String SECRET = "aaaaiiii";
	
	private static final String TOKEN_PREFIX = "Bearer";
	
	private static final String HEADER_STRING = "Authorization";
	
	/*gerar token e da resposta ao cliente*/
	public void addAuthentication(HttpServletResponse response, String username	)throws Exception {
		
		String JWT = Jwts.builder()./*chama gerador de token*/
				setSubject(username) /*adiciona o user*/
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) /*Tempo expiração*/
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();
		
		String token = TOKEN_PREFIX + " " + JWT;
		
		/*Dá resposta para tela e para o cliente*/
		response.addHeader(HEADER_STRING, token);
		
		liberacaoCors(response);
		
		/*usado para test no Postman*/
		response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
		
	}
	
	/*Retorna usuário váçido*/
	public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {
		
		String token = request.getHeader(HEADER_STRING);
		
		if(token != null) {
			
			String tokenLimpo = token.replace(TOKEN_PREFIX, "").trim();
			
			/*validação token usuário na rquisição e obtem USER*/
			String user = Jwts.parser().
					setSigningKey(SECRET)
					.parseClaimsJws(tokenLimpo)
					.getBody().getSubject();
			
			if(user != null) {
				
				Usuario usuario = ApplicationContextLoad.
						getApplicationContext().
						getBean(UsuarioRepository.class).findUserByLogin(user);
				
				if(user != null) {
					return new UsernamePasswordAuthenticationToken(
							usuario.getLogin(), 
							usuario.getSenha(),
							usuario.getAuthorities());
				}
			}
			
			
			
		}
		
		liberacaoCors(response);
		return null;
	}
	
	/*Liberação contra erro de cors no browser*/
	private void liberacaoCors(HttpServletResponse response) {
		
		if(response.getHeader("Access-Control-Allow-Origin") == null) {
			response.addHeader("Access-Control-Allow-Origin", "*");
		}
		
		if(response.getHeader("Access-Control-Allow-Headers") == null) {
			response.addHeader("Access-Control-Allow-Headers", "*");
		}
		
		if(response.getHeader("Access-Control-Request-Headers") == null) {
			response.addHeader("Access-Control-Request-Headers", "*");
		}
		
		if(response.getHeader("Access-Control-Allow-Methods") == null) {
			response.addHeader("Access-Control-Allow-Methods", "*");
		}
		
	}

}
