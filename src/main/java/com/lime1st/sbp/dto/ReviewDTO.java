package com.lime1st.sbp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {

    //  review
    private Long reviewnum;

    //  movie
    private Long mno;

    //  member
    private String name;
    private String email;

    private int grade;
    private String text;

    private LocalDateTime regDate, modDate;
}
