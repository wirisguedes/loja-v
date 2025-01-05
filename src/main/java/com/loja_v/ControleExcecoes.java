package com.loja_v;

import java.io.UnsupportedEncodingException;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.List;

import javax.mail.MessagingException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.loja_v.model.dto.ObjetoErroDTO;
import com.loja_v.service.ServiceSendEmail;


@RestControllerAdvice
@ControllerAdvice
public class ControleExcecoes extends ResponseEntityExceptionHandler{
	
	@Autowired
	private ServiceSendEmail serviceSendEmail;
	
	public ResponseEntity<Object> handleExceptionCustom(ExceptionLoja ex){
		ObjetoErroDTO objetoErroDTO = new ObjetoErroDTO();
		
		objetoErroDTO.setError(ex.getMessage());
		objetoErroDTO.setCode(HttpStatus.OK.toString());
		
		return new ResponseEntity<Object>(objetoErroDTO, HttpStatus.OK);
	}

	/*Captura exceções do projeto*/
	@ExceptionHandler({ Exception.class, RuntimeException.class, Throwable.class })
	public ResponseEntity<Object> handleCustomException(Exception ex, WebRequest request) {
		ObjetoErroDTO objetoErroDTO = new ObjetoErroDTO();
		String msg = "";

		if (ex instanceof MethodArgumentNotValidException) {
			List<ObjectError> list = ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors();

			for (ObjectError objectError : list) {
				msg += objectError.getDefaultMessage() + "\n";
			}
		} else if (ex instanceof HttpMessageNotReadableException) {

			msg = "Não está sendo enviado dados para o BODY corpo da requisição";
		} else {
			msg = ex.getMessage();
		}

		objetoErroDTO.setError(msg);
		objetoErroDTO.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value() + " ==> "
				+ HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());

		try {
			serviceSendEmail.enviarEmailHtml("Erro Loja V", ExceptionUtils.getStackTrace(ex), "lojanamidia@gmail.com");
		} catch (UnsupportedEncodingException | MessagingException e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>(objetoErroDTO, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler({DataIntegrityViolationException.class,
		ConstraintViolationException.class, SQLDataException.class})
	protected ResponseEntity<Object> handleExceptionDataIntegry(Exception ex) {

		ObjetoErroDTO objetoErroDTO = new ObjetoErroDTO();

		String msg = "";

		if(ex instanceof DataIntegrityViolationException) {
			msg = "Erro de integridade do banco: " + ((SQLException) ex).getCause().getCause().getMessage();
		}else {
			msg = ex.getMessage();
		}
		
		if(ex instanceof ConstraintViolationException) {
			msg = "Erro de chave estrangeira: " + ((SQLException) ex).getCause().getCause().getMessage();
		}else {
			msg = ex.getMessage();
		}
		
		if(ex instanceof SQLException) {
			msg = "Erro de SQL do Banco: " + ((SQLException) ex).getCause().getCause().getMessage();
		}else {
			msg = ex.getMessage();
		}
		
		objetoErroDTO.setError(msg);
		objetoErroDTO.setCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
		
		try {
			serviceSendEmail.enviarEmailHtml("Erro Loja V",
					ExceptionUtils.getStackTrace(ex),
					"lojanamidia@gmail.com");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			
			e.printStackTrace();
		}

		return new ResponseEntity<Object>(objetoErroDTO, HttpStatus.INTERNAL_SERVER_ERROR);

	}
}
