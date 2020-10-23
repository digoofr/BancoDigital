package br.com.banco.api.model;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Conta extends PersistentObject implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "cpf", nullable = false, length = 11, unique = true)
    private String cpf;

    @Column(name = "nome", nullable = false, length = 225)
    private String nome;

    @Column(name = "numero_conta",length = 150,unique = true)
    private String numeroConta;

    @Column(name = "saldo", nullable = false, precision = 15, scale = 8)
    private BigDecimal saldo;

    @Column(name = "dt_cadastro")
    @Temporal(TemporalType.DATE)
    private Date dtCadastro;

    public Conta() {
    }

    public Conta(Integer conta) {
    }

    public Conta(long id,String nome, String cpf, BigDecimal saldo, String numeroConta, Date dtCadastro) {
        super();
        setId(id);
        this.cpf = cpf;
        this.nome = nome;
        this.saldo = saldo;
        this.numeroConta = numeroConta;
        this.dtCadastro = dtCadastro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Conta)) return false;
        if (!super.equals(o)) return false;
        Conta conta = (Conta) o;
        return Objects.equals(getCpf(), conta.getCpf()) &&
                Objects.equals(getNome(), conta.getNome()) &&
                Objects.equals(getNumeroConta(), conta.getNumeroConta()) &&
                Objects.equals(getSaldo(), conta.getSaldo()) &&
                Objects.equals(getDtCadastro(), conta.getDtCadastro());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getCpf(), getNome(), getNumeroConta(), getSaldo(), getDtCadastro());
    }
}
