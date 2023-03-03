package com.robson.usecash.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "registry_id")
    private Registry registry;
    
    Double totalPagarRegimeEspecial;
    Double totalPagarCorreios;
    Double totalPAgarTermo;
    Double totalPagarEmissaoCartao;
    Double totalMensalidade;
    Double valorTotalFatura;
    LocalDate dataVencimentoFatura;
    
    @Column(name = "created_at")
    private LocalDateTime dataCriacao;

    @PrePersist
    public void prePersist() {
        dataCriacao = LocalDateTime.now(ZoneId.systemDefault());
    }
}
