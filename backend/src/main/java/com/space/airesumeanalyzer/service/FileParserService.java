package com.space.airesumeanalyzer.service;

import com.space.airesumeanalyzer.exception.InvalidFileTypeException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class FileParserService {

    public String extractText(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new InvalidFileTypeException("PDF 또는 docx(Word) 파일만 업로드할 수 있습니다.");
        }

        String lowerCaseName = originalFilename.toLowerCase();

        try {
            if (lowerCaseName.endsWith(".pdf")) {
                return extractTextFromPdf(file.getInputStream());
            } else if (lowerCaseName.endsWith(".docx")) {
                return extractTextFromDocx(file.getInputStream());
            } else {
                throw new InvalidFileTypeException("PDF 또는 docx(Word) 파일만 업로드할 수 있습니다.");
            }
        } catch (IOException e) {
            throw new RuntimeException("파일 텍스트 추출 중 오류가 발생했습니다.", e);
        }
    }

    private String extractTextFromPdf(InputStream inputStream) throws IOException {
        try (PDDocument document = Loader.loadPDF(inputStream.readAllBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    private String extractTextFromDocx(InputStream inputStream) throws IOException {
        try (XWPFDocument document = new XWPFDocument(inputStream)) {
            StringBuilder sb = new StringBuilder();
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                sb.append(paragraph.getText()).append("\n");
            }
            return sb.toString();
        }
    }
}