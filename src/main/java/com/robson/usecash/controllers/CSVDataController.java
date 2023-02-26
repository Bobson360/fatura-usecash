package com.robson.usecash.controllers;

import com.opencsv.exceptions.CsvException;
import com.robson.usecash.services.exeptions.CSVDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/")
public class CSVDataController {
    @PostMapping("/file_upload")
    public ResponseEntity<String> salvarClientes(@RequestParam("file") MultipartFile arquivo) throws IOException, CsvException {

        String[] cabecalhoEsperado = { "ID DA EMPRESA", "CNPJ (apenas os números)", "NOME FANTASIA",
                "NRO DE DIAS UTEIS PARA VECTO DO BOLETO", "EMAIL COBRANÇA 1", "EMAIL COBRANÇA 2", "TIPO DE MENSALIDADE",
                "MES DE REFERENCIA ", "ANO DE REFERENCIA ", "QTDE DE LOJA ", "VALOR MENSALIDADE",
                "VALOR UNITARIO TERMO DE ADESAO", "VALOR  UNITARIO EMISSAO CARTÃO", "QTDE DE TERMOS EMITIDOS ",
                "QTDE DE TERMOS CANCELADOS", "QTDE CARTAO EMITIDOS ", "CORREIOS 1 - VALOR TOTAL R$",
                "CORREIOS 2 - VALOR TOTAL R$", "CORREIOS 3 - VALOR TOTAL R$", "CORREIOS 4 - VALOR TOTAL R$",
                "TAXA DE REGIME ESPECIAL ", "TOTAL DE CRÉDITO ADQUIRIDO",
//				"TOTAL A PAGAR REGIME ESPECIAL R$",
//				"TOTAL A PAGAR CORREIOS R$ ",
//				"TOTAL A PAGAR TERMO",
//				"TOTAL A PAGAR EMISSAO DE CARTAO ",
//				"TOTAL DA MENSALIDADE R$ ",
//				"VALOR TOTAL FINAL FATURA ",
//				"DATA DE VECTO DA DA FATURA DE COBRANÇA"
        };
        CSVDataService csvService = new CSVDataService();
        csvService.importarCSV(arquivo.getInputStream(), cabecalhoEsperado);
        // Processar o arquivo CSV e salvar os clientes no banco de dados
        return ResponseEntity.ok("Clientes salvos com sucesso!");
    }
}
