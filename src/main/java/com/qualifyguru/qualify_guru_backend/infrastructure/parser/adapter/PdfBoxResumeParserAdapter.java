package com.qualifyguru.qualify_guru_backend.infrastructure.parser.adapter;

import com.qualifyguru.qualify_guru_backend.application.port.out.FileParserPort;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class PdfBoxResumeParserAdapter implements FileParserPort {

    private static final String INCORRECT_PDF_FORMAT = "The PDF cannot be empty or a scanned image without text.";
    private static final String FAILED_PDF_EXTRACTION = "Failed to process and extract text to PDF.";

    @Override
    public String extractText(InputStream pdfStream) {

        try (PDDocument document = PDDocument.load(pdfStream)) {

            PDFTextStripper stripper = new PDFTextStripper();

            String extractedText = stripper.getText(document);

            if (extractedText == null || extractedText.trim().isEmpty()) {
                throw new IllegalArgumentException(INCORRECT_PDF_FORMAT);
            }

            return extractedText.trim();

        } catch (IOException e) {
            throw new RuntimeException(FAILED_PDF_EXTRACTION, e);
        }
    }
}
