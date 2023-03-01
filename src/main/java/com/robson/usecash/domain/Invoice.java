package com.robson.usecash.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    Double TOTAL_PAGAR_REGIME_ESPECIAL;
    Double TOTAL_PAGAR_CORREIOS;
    Double TOTAL_PAGAR_TERMO;
    Double TOTAL_PAGAR_EMISSAO_CARTAO;
    Double TOTAL_MENSALIDADE;
    Double VALOR_TOTAL_FINAL_FATURA;
    LocalDate DATA_VECTO_FATURA_COBRANCA;
    
    @Column(name = "created_at")
    private LocalDateTime dataCriacao;

    @PrePersist
    public void prePersist() {
        dataCriacao = LocalDateTime.now(ZoneId.systemDefault());
    }
}
