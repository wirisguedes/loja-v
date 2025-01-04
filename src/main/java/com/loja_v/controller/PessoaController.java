package com.loja_v.controller;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.loja_v.ExceptionLoja;
import com.loja_v.enums.TipoPessoa;
import com.loja_v.model.Endereco;
import com.loja_v.model.PessoaFisica;
import com.loja_v.model.PessoaJuridica;
import com.loja_v.model.Usuario;
import com.loja_v.model.dto.CepDTO;
import com.loja_v.model.dto.ConsultaCnpjDTO;
import com.loja_v.model.dto.ObjetoMsgGeral;
import com.loja_v.repository.EnderecoRepository;
import com.loja_v.repository.PessoaFisicaRepository;
import com.loja_v.repository.PessoaRepository;
import com.loja_v.repository.UsuarioRepository;
import com.loja_v.service.PessoaUserService;
import com.loja_v.service.ServiceContagemAcessoApi;
import com.loja_v.service.ServiceSendEmail;
import com.loja_v.util.ValidaCNPJ;
import com.loja_v.util.ValidarCPF;

@RestController
public class PessoaController {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private PessoaUserService pessoaUserService;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;
	
	@Autowired
	private ServiceContagemAcessoApi serviceContagemAcessoApi;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private ServiceSendEmail serviceSendEmail;
	
	@ResponseBody
	@GetMapping(value = "**/consultaPfNome/{nome}")
	public ResponseEntity<List<PessoaFisica>> consultaPfNome(@PathVariable("nome") String nome){
		
		List<PessoaFisica> fisicas = pessoaFisicaRepository.pesquisaPorNomePF(nome.trim().toUpperCase());
		
		serviceContagemAcessoApi.atualizaAcessoEndPointPF();
		
		return new ResponseEntity<List<PessoaFisica>>(fisicas, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/consultaPfCpf/{cpf}")
	public ResponseEntity<List<PessoaFisica>> consultaPfCpf(@PathVariable("cpf") String cpf){
		
		List<PessoaFisica> fisicas = pessoaFisicaRepository.pesquisaPorCpfPF(cpf);
		return new ResponseEntity<List<PessoaFisica>>(fisicas, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/consultaNomePJ/{nome}")
	public ResponseEntity<List<PessoaJuridica>> consultaNomePJ(@PathVariable("nome") String nome){
		
		List<PessoaJuridica> fisicas = pessoaRepository.pesquisaPorNomePJ(nome.trim().toUpperCase());
		return new ResponseEntity<List<PessoaJuridica>>(fisicas, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/consultaCnpjPJ/{cnpj}")
	public ResponseEntity<List<PessoaJuridica>> consultaCnpjPJ(@PathVariable("cnpj") String cnpj){
		
		List<PessoaJuridica> fisicas = pessoaRepository.existeCnpjCadastradoList(cnpj);
		return new ResponseEntity<List<PessoaJuridica>>(fisicas, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/consultaCep/{cep}")
	public ResponseEntity<CepDTO> consultaCep(@PathVariable("cep") String cep){
		
		return new ResponseEntity<CepDTO>(pessoaUserService.consultaCep(cep), HttpStatus.OK);
		
	}
	
	@ResponseBody
	@GetMapping(value = "**/consultaCnpjReceitaWs/{cnpj}")
	public ResponseEntity<ConsultaCnpjDTO> consultaCnpjReceitaWs(@PathVariable("cnpj") String cnpj){
		
	  return new ResponseEntity<ConsultaCnpjDTO>(pessoaUserService.consultaCnpjReceitaWS(cnpj), HttpStatus.OK);
		
	}
	
	@ResponseBody
	@PostMapping(value = "**/salvarPj")
	public ResponseEntity<PessoaJuridica> salvarPj(@RequestBody @Valid PessoaJuridica pessoaJuridica) throws ExceptionLoja{
		
		if(pessoaJuridica == null) {
			throw new ExceptionLoja("Pessoa Juridica não pode ser NULL");
		}
		
		if(pessoaJuridica.getTipoPessoa() == null) {
			throw new ExceptionLoja("Informe o tipo");
		}
		
		if(pessoaJuridica.getId() == null && pessoaRepository.existeCnpjCadastrado(pessoaJuridica.getCnpj()) != null) {
			throw new ExceptionLoja("Já existe CNPJ cadastrado com o númeo: " + pessoaJuridica.getCnpj());
		}
		
		if(pessoaJuridica.getId() == null && pessoaRepository.existeInscEstadualCadastrado(pessoaJuridica.getInscEstadual()) != null) {
			throw new ExceptionLoja("Já existe Inscrição Estadual cadastrado com o númeo: " + pessoaJuridica.getInscEstadual());
		}
		
		if(!ValidaCNPJ.isCNPJ(pessoaJuridica.getCnpj())) {
			throw new ExceptionLoja("CNPJ: " + pessoaJuridica.getCnpj() + " inválido");
		}
		
		if(pessoaJuridica.getId() == null || pessoaJuridica.getId() <=0) {
			for(int p = 0; p <pessoaJuridica.getEnderecos().size(); p++) {
				CepDTO cepDTO  = pessoaUserService.consultaCep(pessoaJuridica.getEnderecos().get(p).getCep());
				pessoaJuridica.getEnderecos().get(p).setBairro(cepDTO.getBairro());
				pessoaJuridica.getEnderecos().get(p).setCidade(cepDTO.getLocalidade());
				pessoaJuridica.getEnderecos().get(p).setComplemento(cepDTO.getComplemento());
				pessoaJuridica.getEnderecos().get(p).setRuaLogra(cepDTO.getLogradouro());
				pessoaJuridica.getEnderecos().get(p).setUf(cepDTO.getUf());
			}
		}else {
			for(int p = 0; p <pessoaJuridica.getEnderecos().size(); p++) {
				
				Endereco enderecoTemp = enderecoRepository.findById(pessoaJuridica.getEnderecos().get(p).getId()).get();
				
				if(!enderecoTemp.getCep().equals(pessoaJuridica.getEnderecos().get(p).getCep())) {
					
					CepDTO cepDTO  = pessoaUserService.consultaCep(pessoaJuridica.getEnderecos().get(p).getCep());
					pessoaJuridica.getEnderecos().get(p).setBairro(cepDTO.getBairro());
					pessoaJuridica.getEnderecos().get(p).setCidade(cepDTO.getLocalidade());
					pessoaJuridica.getEnderecos().get(p).setComplemento(cepDTO.getComplemento());
					pessoaJuridica.getEnderecos().get(p).setRuaLogra(cepDTO.getLogradouro());
					pessoaJuridica.getEnderecos().get(p).setUf(cepDTO.getUf());
				}
			}
			
		}
		
		pessoaJuridica = pessoaUserService.salvarPessoaJuridica(pessoaJuridica);
		
		return new ResponseEntity<PessoaJuridica>(pessoaJuridica, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/possuiAcesso/{username}/{role}")
	public ResponseEntity<Boolean> possuiAcesso(@PathVariable("username") String username,
			@PathVariable("role") String role){
		
		String sqlRole = "'" + role.replaceAll(",", "','") + "'";
		
		Boolean possuiAcesso = pessoaUserService.possuiAcesso(username, sqlRole);
		
		return new ResponseEntity<Boolean>(possuiAcesso, HttpStatus.OK);
		
	}
	
	@ResponseBody
	@PostMapping(value = "**/recuperarSenha")
	public ResponseEntity<ObjetoMsgGeral> recuperarAcesso(@RequestBody String login) throws Exception {
		
		Usuario usuario = usuarioRepository.findUserByLogin(login);
		
		if (usuario == null) {
			return new ResponseEntity<ObjetoMsgGeral>(new ObjetoMsgGeral("Usuário não encontrado"), HttpStatus.OK);
		}
		
		String senha = UUID.randomUUID().toString();
		
		senha = senha.substring(0, 6);
		
		String senhaCriptografada = new BCryptPasswordEncoder().encode(senha);
		
		usuarioRepository.updateSenhaUser(senhaCriptografada, login);
		
		StringBuilder msgEmail = new StringBuilder();
		msgEmail.append("<b>Nova senha é:</b>")
		.append(senha);
		
		serviceSendEmail.enviarEmailHtml("Sua nova senha", msgEmail.toString(), usuario.getPessoa().getEmail());
		
		
		return new ResponseEntity<ObjetoMsgGeral>(new ObjetoMsgGeral("Senha enviada para o seu e-mail"), HttpStatus.OK);
		
	}
	
	@ResponseBody
	@PostMapping(value = "**/salvarPf")
	public ResponseEntity<PessoaFisica> salvarPf(@RequestBody PessoaFisica pessoaFisica) throws ExceptionLoja{
		
		if(pessoaFisica == null) {
			throw new ExceptionLoja("Pessoa Fisica não pode ser NULL");
		}
		
		if(pessoaFisica.getTipoPessoa() == null) {
			pessoaFisica.setTipoPessoa(TipoPessoa.FISICA.name());
		}
		
		if(pessoaFisica.getId() == null && pessoaRepository.existeCpfCadastrado(pessoaFisica.getCpf()) != null) {
			throw new ExceptionLoja("Já existe CPF cadastrado com o númeo: " + pessoaFisica.getCpf());
		}
						
		if(!ValidarCPF.isCPF(pessoaFisica.getCpf())) {
			throw new ExceptionLoja("CPF: " + pessoaFisica.getCpf() + " inválido");
		}
		
		pessoaFisica = pessoaUserService.salvarPessoaFisica(pessoaFisica);
		
		return new ResponseEntity<PessoaFisica>(pessoaFisica, HttpStatus.OK);
	}

}
