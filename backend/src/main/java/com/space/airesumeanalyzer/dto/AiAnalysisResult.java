package com.space.airesumeanalyzer.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class AiAnalysisResult {

    private String summary;
    private List<String> strengths;
    private List<String> weaknesses;
    private int passRate;
}