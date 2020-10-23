package br.com.banco.api.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.com.banco.api.dto.ContaDTO;
import br.com.banco.api.model.Conta;
import br.com.banco.api.model.repository.ContaRepository;

@Service
public class ContaService {

    private BigDecimal limiteMaximo = new BigDecimal(500.00);

    @Autowired
    private ContaRepository contaRepository;

    @Transactional(readOnly = true)
    public List<ContaDTO> listarContas() {
        List<Conta> lista = contaRepository.findAll();
        return lista.stream().map(O -> new ContaDTO(O)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ContaDTO> listarPorNumeroConta(String numeroConta) {
        List<Conta> numConta = contaRepository.findByNumeroConta(numeroConta);
        return numConta.stream().map(O -> new ContaDTO(O)).collect(Collectors.toList());
    }

    @Transactional
    public ContaDTO salvar(ContaDTO contaDTO) {

        Conta conta = new Conta();

        if (contaDTO.getNome() == null || "".equals(contaDTO.getNome())) {
            conta.setNome("Nome não informado");
        } else {
            conta.setNome(contaDTO.getNome());
        }

        conta.setCpf(contaDTO.getCpf());
        conta.setSaldo(contaDTO.getSaldo());
        conta.setNumeroConta("0001");
        conta.setDtCadastro(contaDTO.getDtCadastro());
        conta = contaRepository.save(conta);
        return new ContaDTO(conta);
    }

    @Transactional
    public ContaDTO depositar(ContaDTO contaDTO, String numerConta) {
        List<Conta> numConta = contaRepository.findByNumeroConta(numerConta);
        Conta deposito = numConta.get(0);
        deposito.setSaldo(contaDTO.getSaldo().add(deposito.getSaldo()));
        contaRepository.save(deposito);
        return new ContaDTO(deposito);
    }

    @Transactional
    public ContaDTO sacar(ContaDTO saque, String numerConta) {
        List<Conta> numConta = contaRepository.findByNumeroConta(numerConta);
        Conta contadb = numConta.get(0);

        if (saque.getSaldo().compareTo(limiteMaximo) == 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Operação de saque tem um limite máximo de 500.00 por operação.");
        } else if (saque.getSaldo().compareTo(contadb.getSaldo()) == 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insuficiente para a operação");
        } else {
            contadb.setSaldo(contadb.getSaldo().subtract(saque.getSaldo()));
            contaRepository.save(contadb);
            return new ContaDTO(contadb);
        }
    }

    @Transactional
    public ContaDTO transferir(ContaDTO transferir, String numeroConta, String numeroConta2) {

        List<Conta> numConta = contaRepository.findByNumeroConta(numeroConta);
        Conta solicitante = numConta.get(0);

        List<Conta> numConta2 = contaRepository.findByNumeroConta(numeroConta2);
        Conta beneficiario = numConta2.get(0);

        if (transferir.getSaldo().compareTo(limiteMaximo) == 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Operação de saque tem um limite máximo de 500.00 por operação.");
        } else {
            if (transferir.getSaldo().compareTo(solicitante.getSaldo()) == 1) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insuficiente para a operação");
            }
        }
        solicitante.setSaldo(solicitante.getSaldo().subtract(transferir.getSaldo()));
        beneficiario.setSaldo(beneficiario.getSaldo().add(transferir.getSaldo()));

        contaRepository.save(solicitante);
        contaRepository.save(beneficiario);

        return new ContaDTO(solicitante);
    }

}