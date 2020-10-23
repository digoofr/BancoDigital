package br.com.banco.api.model;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.PreDestroy;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Classe pai das demais Entidades do sistema, é responsável por incluir os vg
 * campos padrões das Entidades, como <b>ID</b>, <b>DT_REGISTRO</b>
 * e <b>DT_ATUALIZACAO</b>, assim como realizar a persistência de data de
 * Registro e data de Atualização.
 *
 * @author Rodrigo Freitas
 * @version 1.0
 */
@Getter
@Setter
@MappedSuperclass
public abstract class PersistentObject implements Serializable {

    private static final long serialVersionUID = -7554199787636222273L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "id_agrupador")
    private Long idAgrupador;

    @Column(name = "dt_registro")
    @Temporal(TemporalType.DATE)
    private Date dtRegistro;

    @Column(name = "dt_atualizacao")
    @Temporal(TemporalType.DATE)
    private Date dtAtualizacao;


   /* @PostLoad
    private void salvarStatusAnterior() {

    }*/

    @PrePersist
    protected void prePersist() {
        valideRegras();
        this.setDtRegistro(new Date());
        this.setDtAtualizacao(new Date());
    }

    @PreUpdate
    protected void preUpdate() {
        valideRegras();

        if (this.getDtRegistro() == null) {
            this.setDtRegistro(new Date());
        }
        this.setDtAtualizacao(new Date());

    }

    @PreDestroy
    protected void preDelete() {

    }

    @PostPersist
    protected void postPersist() {

    }

    protected void valideRegras() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersistentObject that = (PersistentObject) o;
        return id == that.id &&
                Objects.equals(dtRegistro, that.dtRegistro);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, dtRegistro);
    }
}