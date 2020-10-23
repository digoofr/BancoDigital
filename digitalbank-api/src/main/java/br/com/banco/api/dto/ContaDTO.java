package br.com.banco.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.banco.api.model.Conta;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class ContaDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private long id;
	private String cpf;
	private String nome;
	private BigDecimal saldo;
	private String numeroConta;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date dtCadastro;

	public ContaDTO(Conta conta) {
		id = conta.getId();
		cpf = conta.getCpf();
		nome = conta.getNome();
		saldo = conta.getSaldo();
		numeroConta = conta.getNumeroConta();
		dtCadastro = conta.getDtCadastro();
	}

	public ContaDTO(){}

	public static List<ContaDTO> converter(List<Conta> contas) {
        return contas.stream().map(ContaDTO::new).collect(Collectors.toList());
    }

}
