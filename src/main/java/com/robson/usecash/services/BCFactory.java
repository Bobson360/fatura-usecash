package com.robson.usecash.services;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;

import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;
import net.sourceforge.barbecue.output.OutputException;


public class BCFactory {
	
	
	public static BufferedImage generateEAN13BarcodeImage(String barcodeText) throws Exception {
		Font BARCODE_TEXT_FONT = new Font("Arial", Font.PLAIN, 12);
	    Barcode barcode = BarcodeFactory.createEAN13(barcodeText);
	    barcode.setFont(BARCODE_TEXT_FONT);

	    return BarcodeImageHandler.getImage(barcode);
	}
	public static void main(String[] args) throws BarcodeException, OutputException {
		boleto();
	}
	
	public static void boleto() throws BarcodeException, OutputException {
		String valor = "100.00";
		String dataVencimento = "01/03/2023";
		String banco = "Santander";
		String multaAtraso = "5.00";

		// Concatene os valores em uma única String
		String codigoBarrasTexto = banco + " " + valor + " " + dataVencimento + " " + multaAtraso;

		// Crie o código de barras Code 128
		Barcode barcode = BarcodeFactory.createCode128(codigoBarrasTexto);
		System.out.println(barcode);
		// Salve o código de barras em um arquivo PNG
		File outputFile = new File("codigo_barras.png");
		BarcodeImageHandler.savePNG(barcode, outputFile);

	}
}
