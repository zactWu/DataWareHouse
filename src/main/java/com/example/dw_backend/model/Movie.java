package com.example.dw_backend.model;

import lombok.Data;

@Data
public class Movie {

    private String productId;

    private String title;

    private int version;

    private int score;

    private int score_count;

    private int emotionScore;

    private int emotionScore_count;

}
