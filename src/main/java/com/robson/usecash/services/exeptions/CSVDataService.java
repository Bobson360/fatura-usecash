package com.robson.usecash.services.exeptions;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.robson.usecash.domain.CSVData;
import com.robson.usecash.repositories.CSVDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@Service
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
        if(validaHeader(cabecalhoEsperado, cabecalhoAtual)) {
            List<String[]> linhas = csvReader.readAll();
            for (String[] linha : linhas) {
                boolean cnpj = true;
                for (int i = 0; i < cabecalhoEsperado.length; i++) {
                    if (header.get(i).toLowerCase().contains("cnpj") && validarCnpj(linha[i])) {
                        System.out.print("CNPJ Valido");
                        cnpj = true;
                    } else if (header.get(i).toLowerCase().contains("cnpj")) {
                        System.out.print("CNPJ invalido");
                        cnpj = false;
                    }

                    /*
                     * inserir dados no map se chave vazia: cria a chave a add valor se chave
                     * existente: só add valor
                     */
                    if (data.containsKey(cabecalhoEsperado[i])) {
                        List<String> lista = new ArrayList<>(data.get(cabecalhoEsperado[i]));
                        String dd = cnpj ? linha[i] : "*"+linha[i];
                        lista.add(header.get(i).toLowerCase().contains("cnpj") ? (cnpj ? linha[i] : "*"+linha[i]) : linha[i]);
                        data.put(cabecalhoEsperado[i], lista);
                    }
                    else
                        data.put(cabecalhoEsperado[i], Arrays.asList(header.get(i).toLowerCase().contains("cnpj") ? (cnpj ? linha[i] : "*"+linha[i]) : linha[i]));
                }
                System.out.println(data);
            }

        }

        // Fecha o arquivo CSV
        csvReader.close();
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

