package com.robson.usecash.services.exeptions;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.robson.usecash.domain.CSVData;
import com.robson.usecash.repositories.CSVDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

//@Service
@Component
public class CSVDataService {

    @Autowired
    private CSVDataRepository csvDataRepository;

    public void importarCSV(InputStream inputStream, String[] cabecalhoEsperado) throws IOException, CsvException {
        InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
        CSVReader csvReader = new CSVReader(reader);

        Map<String, List<String>> data = new LinkedHashMap<String, List<String>>();
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
//                c.setAll(data2);
//                System.out.println(c.toString());
//			System.out.println(data2);
            }
        }
//        System.out.println(data2);
        salvarCsvEntity(data2);


        // Fecha o arquivo CSV
        csvReader.close();
//        salvarCsvEntity(data);
    }


    public void salvarCsvEntity(List<Map<String, String>> data) {
// popule a lista de dados aqui

        List<CSVData> csvDataList = new ArrayList<CSVData>();
        for (Map<String, String> map : data) {
//            CSVData csvData = new CSVData();
//            csvData.setCNPJ(map.get("CNPJ (apenas os números)"));
//            csvData.setNOME_FANTASIA(map.get("NOME FANTASIA"));
//            csvData.setDIAS_UTEIS_VECTO_BOLETO(Integer.parseInt(map.get("NRO DE DIAS UTEIS PARA VECTO DO BOLETO")));
//            csvData.setEMAIL_COBRANCA_1(map.get("EMAIL COBRANÇA 1"));
//            csvData.setEMAIL_COBRANCA_2(map.get("EMAIL COBRANÇA 2"));
//            csvData.setTIPO_MENSALIDADE(map.get("TIPO DE MENSALIDADE"));
//            csvData.setMES_REFERENCIA(Integer.parseInt(map.get("MES DE REFERENCIA ")));
//            csvData.setANO_REFERENCIA(Integer.parseInt(map.get("ANO DE REFERENCIA ")));
//            csvData.setQTDE_LOJA(Integer.parseInt(map.get("QTDE DE LOJA ")));
//            csvData.setVALOR_MENSALIDADE(Double.parseDouble(map.get("VALOR MENSALIDADE")));
//            csvData.setVALOR_UNITARIO_TERMO_ADESAO(Double.parseDouble(map.get("VALOR UNITARIO TERMO DE ADESAO")));
//            csvData.setVALOR_UNITARIO_EMISSAO_CARTAO(Double.parseDouble(map.get("VALOR  UNITARIO EMISSAO CARTÃO")));
//            csvData.setQTDE_TERMOS_EMITIDOS(Integer.parseInt(map.get("QTDE DE TERMOS EMITIDOS ")));
//            csvData.setQTDE_TERMOS_CANCELADOS(Integer.parseInt(map.get("QTDE DE TERMOS CANCELADOS")));
//            csvData.setQTDE_CARTAO_EMITIDOS(Integer.parseInt(map.get("QTDE CARTAO EMITIDOS ")));
//            csvData.setCORREIOS_1(Double.parseDouble(map.get("CORREIOS 1 - VALOR TOTAL R$")));
//            csvData.setCORREIOS_2(Double.parseDouble(map.get("CORREIOS 2 - VALOR TOTAL R$")));
//            csvData.setCORREIOS_3(Double.parseDouble(map.get("CORREIOS 3 - VALOR TOTAL R$")));
//            csvData.setCORREIOS_4(Double.parseDouble(map.get("CORREIOS 4 - VALOR TOTAL R$")));
//            csvData.setTAXA_REGIME_ESPECIAL(Double.parseDouble(map.get("TAXA DE REGIME ESPECIAL ")));
//            csvData.setTOTAL_CREDITO_ADQUIRIDO(Double.parseDouble(map.get("TOTAL DE CRÉDITO ADQUIRIDO")));
//            csvDataList.add(csvData);
        }
        CSVData csvData2 = new CSVData();
        csvData2.setCNPJ("teste");
        csvData2.setNOME_FANTASIA("teste");
        csvData2.setDIAS_UTEIS_VECTO_BOLETO(15);
        csvData2.setEMAIL_COBRANCA_1("EMAIL COBRANÇA 1");
        csvData2.setEMAIL_COBRANCA_2("EMAIL COBRANÇA 2");
        csvData2.setTIPO_MENSALIDADE("TIPO DE MENSALIDADE");
        csvData2.setMES_REFERENCIA(7);
        csvData2.setANO_REFERENCIA(10);
        csvData2.setQTDE_LOJA(15);
        csvData2.setVALOR_MENSALIDADE(2.35);
        csvData2.setVALOR_UNITARIO_TERMO_ADESAO(5.65);
        csvData2.setVALOR_UNITARIO_EMISSAO_CARTAO(7.56);
        csvData2.setQTDE_TERMOS_EMITIDOS(78);
        csvData2.setQTDE_TERMOS_CANCELADOS(100);
        csvData2.setQTDE_CARTAO_EMITIDOS(50);
        csvData2.setCORREIOS_1(78.54);
        csvData2.setCORREIOS_2(15.74);
        csvData2.setCORREIOS_3(4.56);
        csvData2.setCORREIOS_4(456.43);
        csvData2.setTAXA_REGIME_ESPECIAL(78.65);
        csvData2.setTOTAL_CREDITO_ADQUIRIDO(12.45);


        System.out.println(csvData2.toString());
        this.csvDataRepository.save(csvData2);
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

}

