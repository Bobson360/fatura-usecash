package com.robson.usecash.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.robson.usecash.domain.Registry;
import com.robson.usecash.repositories.RegistryRepository;

@Service
public class RegistryService {

    @Autowired
    private RegistryRepository csvDataRepository;


    public List<Registry> importarCSV(InputStream inputStream, String[] cabecalhoEsperado) throws IOException, CsvException {
        InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
        CSVReader csvReader = new CSVReader(reader);

        List<String> header;


        // Lê a primeira linha do arquivo (o cabeçalho)
        String[] cabecalhoAtual = csvReader.readNext();
        header = Arrays.asList(cabecalhoAtual);
        List<Map<String, String>> data2 = new ArrayList<Map<String, String>>();

        if(validaHeader(cabecalhoEsperado, cabecalhoAtual)) {
            List<String[]> linhas = csvReader.readAll();

            for (String[] linha : linhas) {
                Map<String, String> lista2 = new LinkedHashMap<String, String>();
                boolean cnpj = true;
//				percorre as colunas
                for (int i = 0; i < cabecalhoEsperado.length; i++) {

                    if (header.get(i).toLowerCase().contains("cnpj"))
                        cnpj = validarCnpj(linha[i]);

                    lista2.put(cabecalhoEsperado[i],
                            cnpj ? linha[i].length() == 0 ? "0": linha[i]
                                    : header.get(i).toLowerCase().contains("cnpj")
                                    ? "Número CNPJ invalido. Cobrança não gerada"
                                    : "0");
                }


                if(data2.size()>0)
                    data2.add(lista2);
                else
                    data2.add(lista2);
            }
        }
        csvReader.close();
        return salvarCsvEntity(data2);


        // Fecha o arquivo CSV
    }


    public List<Registry> salvarCsvEntity(List<Map<String, String>> data) {
// popule a lista de dados aqui
        List<Registry> csvDataList = new ArrayList<Registry>();
        for (Map<String, String> map : data) {
            Registry csvData = new Registry();
            csvData.setCNPJ(map.get("CNPJ (apenas os números)"));
            csvData.setNOME_FANTASIA(map.get("NOME FANTASIA"));
            csvData.setDIAS_UTEIS_VECTO_BOLETO(Integer.parseInt(replace(map.get("NRO DE DIAS UTEIS PARA VECTO DO BOLETO"))));
            csvData.setEMAIL_COBRANCA_1(replace(map.get("EMAIL COBRANÇA 1")));
            csvData.setEMAIL_COBRANCA_2(replace(map.get("EMAIL COBRANÇA 2")));
            csvData.setTIPO_MENSALIDADE(map.get("TIPO DE MENSALIDADE"));
            csvData.setMES_REFERENCIA(Integer.parseInt(replace(map.get("MES DE REFERENCIA "))));
            csvData.setANO_REFERENCIA(Integer.parseInt(replace(map.get("ANO DE REFERENCIA "))));
            csvData.setQTDE_LOJA(Integer.parseInt(replace(map.get("QTDE DE LOJA "))));
            csvData.setVALOR_MENSALIDADE(Double.parseDouble(replace(map.get("VALOR MENSALIDADE"))));
            csvData.setVALOR_UNITARIO_TERMO_ADESAO(Double.parseDouble(replace(map.get("VALOR UNITARIO TERMO DE ADESAO"))));
            csvData.setVALOR_UNITARIO_EMISSAO_CARTAO(Double.parseDouble(replace(map.get("VALOR  UNITARIO EMISSAO CARTÃO"))));
            csvData.setQTDE_TERMOS_EMITIDOS(Integer.parseInt(replace(map.get("QTDE DE TERMOS EMITIDOS "))));
            csvData.setQTDE_TERMOS_CANCELADOS(Integer.parseInt(replace(map.get("QTDE DE TERMOS CANCELADOS"))));
            csvData.setQTDE_CARTAO_EMITIDOS(Integer.parseInt(replace(map.get("QTDE CARTAO EMITIDOS "))));
            csvData.setCORREIOS_1(Double.parseDouble(replace(map.get("CORREIOS 1 - VALOR TOTAL R$"))));
            csvData.setCORREIOS_2(Double.parseDouble(replace(map.get("CORREIOS 2 - VALOR TOTAL R$"))));
            csvData.setCORREIOS_3(Double.parseDouble(replace(map.get("CORREIOS 3 - VALOR TOTAL R$"))));
            csvData.setCORREIOS_4(Double.parseDouble(replace(map.get("CORREIOS 4 - VALOR TOTAL R$"))));
            csvData.setTAXA_REGIME_ESPECIAL(Double.parseDouble(replace(map.get("TAXA DE REGIME ESPECIAL "))));
            csvData.setTOTAL_CREDITO_ADQUIRIDO(Double.parseDouble(replace(map.get("TOTAL DE CRÉDITO ADQUIRIDO"))));
            csvData.setSTATUS(csvData.getCNPJ().contains("invalido") ? "ERRO": "pendente");
            csvDataList.add(csvData);
        }

        return csvDataRepository.saveAll(csvDataList);



    }

    public boolean validaHeader(String[] headerEsperado, String[] headerAtual) {

        // Compara o cabeçalho atual com o cabeçalho esperado
        if (headerAtual != null) {
            boolean cabecalhosIguais = true;

            for (int i = 0; i < headerEsperado.length; i++) {
                if (!headerAtual[i].replace("\n", "").trim().equals(headerEsperado[i].trim())) {
                    cabecalhosIguais = false;
                    break;
                }
            }

            /*
             * Implementar exceções
             */
            if (cabecalhosIguais) {
                System.out.println("O cabeçalho é válido.");
                return true;
//
            } else {
                System.out.println("O cabeçalho é inválido.");
                return false;
            }
        } else {
            System.out.println("O arquivo não possui um cabeçalho válido.");
            return false;
        }
    }

    public static boolean validarCnpj(String cnpj) {
        // Remove qualquer caractere que não seja número
        cnpj = cnpj.replaceAll("[^0-9]+", "");

        // Verifica se o CNPJ possui 14 dígitos
        if (cnpj.length() != 14) {
            return false;
        }

        // Calcula o primeiro dígito verificador
        int soma = 0;
        int peso = 5;
        for (int i = 0; i < 12; i++) {
            soma += (cnpj.charAt(i) - '0') * peso--;
            if (peso < 2) {
                peso = 9;
            }
        }
        int primeiroDigito = 11 - (soma % 11);
        if (primeiroDigito > 9) {
            primeiroDigito = 0;
        }

        // Calcula o segundo dígito verificador
        soma = 0;
        peso = 6;
        for (int i = 0; i < 13; i++) {
            soma += (cnpj.charAt(i) - '0') * peso--;
            if (peso < 2) {
                peso = 9;
            }
        }
        int segundoDigito = 11 - (soma % 11);
        if (segundoDigito > 9) {
            segundoDigito = 0;
        }

        // Verifica se os dígitos verificadores são válidos
        return cnpj.charAt(12) - '0' == primeiroDigito && cnpj.charAt(13) - '0' == segundoDigito;
    }

    public String replace(String input) {
        return  input.replaceAll("[^\\d,.]+", "").isEmpty() ? "0" : input.replaceAll("[^\\d,.]+", "").replace(",", ".");
    }
}

