package com.robson.usecash.domain;

import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Registry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    String CNPJ;
    String NOME_FANTASIA;
    Integer DIAS_UTEIS_VECTO_BOLETO;
    String EMAIL_COBRANCA_1;
    String EMAIL_COBRANCA_2;
    String TIPO_MENSALIDADE;
    Integer MES_REFERENCIA;
    Integer ANO_REFERENCIA;
    Integer QTDE_LOJA;
    Double VALOR_MENSALIDADE;
    Double VALOR_UNITARIO_TERMO_ADESAO;
    Double VALOR_UNITARIO_EMISSAO_CARTAO;
    Integer QTDE_TERMOS_EMITIDOS;
    Integer QTDE_TERMOS_CANCELADOS;
    Integer QTDE_CARTAO_EMITIDOS;
    Double CORREIOS_1;
    Double CORREIOS_2;
    Double CORREIOS_3;
    Double CORREIOS_4;
    Double TAXA_REGIME_ESPECIAL;
    Double TOTAL_CREDITO_ADQUIRIDO;
    String STATUS;
    
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @PrePersist
    public void prePersist() {
        dataCriacao = LocalDateTime.now(ZoneId.systemDefault());
    }
}
