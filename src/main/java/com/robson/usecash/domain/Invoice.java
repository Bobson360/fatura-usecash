package com.robson.usecash.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
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
    Date DATA_VECTO_FATURA_COBRANCA;
}
