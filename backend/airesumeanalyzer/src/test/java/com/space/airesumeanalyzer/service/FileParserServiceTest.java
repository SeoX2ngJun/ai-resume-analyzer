package com.space.airesumeanalyzer.service;

import com.space.airesumeanalyzer.exception.InvalidFileTypeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.*;

class FileParserServiceTest {

    private final FileParserService fileParserService = new FileParserService();

    @Test
    @DisplayName("지원하지 않는 파일 형식(.txt) 업로드 시 예외 발생 검증")
    void invalidFileExtensionTest() {
        // given
        MockMultipartFile invalidFile = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "Hello World".getBytes()
        );

        // when & then
        InvalidFileTypeException exception = assertThrows(
                InvalidFileTypeException.class,
                () -> fileParserService.extractText(invalidFile)
        );

        assertEquals("PDF 또는 docx(Word) 파일만 업로드할 수 있습니다.", exception.getMessage());
    }
}