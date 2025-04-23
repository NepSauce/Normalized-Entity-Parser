package nep.rosterconversion;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFConversion{

    public static void main(String[] args) {
        String pdfPath = "Media/roster.pdf";
        String outputTextPath = "Media/output.txt"; 

        try {
            try ( 
                    PDDocument document = PDDocument.load(new File(pdfPath))){
                if (!document.isEncrypted()){
                    PDFTextStripper textStripper = new PDFTextStripper();
                    String text = textStripper.getText(document);
                    
                    java.nio.file.Files.writeString(new File(outputTextPath).toPath(), text);
                } else {
                    System.out.println("The PDF is encrypted and cannot be processed.");
                }
            }
        } catch (IOException e){
            System.err.println("Error processing the PDF: " + e.getMessage());
        }
    }
}
