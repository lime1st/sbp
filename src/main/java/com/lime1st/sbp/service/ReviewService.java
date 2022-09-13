package com.lime1st.sbp.service;

import com.lime1st.sbp.dto.ReviewDTO;
import com.lime1st.sbp.entity.Member;
import com.lime1st.sbp.entity.Movie;
import com.lime1st.sbp.entity.Review;

import java.util.List;

public interface ReviewService {

    //  영화의 모든 영화리뷰
    List<ReviewDTO> getListOfMovie(Long mno);

    //  영화 리뷰 추라
    Long register(ReviewDTO movieReviewDTO);

    //  특정 영화리뷰 수정
    void modify(ReviewDTO movieReviewDTO);


    //  리뷰 삭제
    void remove(Long reviewnum);

    default Review dtoToEntity(ReviewDTO movieReviewDTO){

        Review movieReview = Review.builder()
                .reviewnum(movieReviewDTO.getReviewnum())
                .movie(Movie.builder().mno(movieReviewDTO.getMno()).build())
                .member(Member.builder().email(movieReviewDTO.getEmail()).build())
                .grade(movieReviewDTO.getGrade())
                .text(movieReviewDTO.getText())
                .build();

        return movieReview;
    }

    default ReviewDTO entityToDto(Review movieReview){

        ReviewDTO reviewDTO = ReviewDTO.builder()
                .reviewnum(movieReview.getReviewnum())
                .mno(movieReview.getMovie().getMno())
                .name(movieReview.getMember().getName())
                .email(movieReview.getMember().getEmail())
                .grade(movieReview.getGrade())
                .text(movieReview.getText())
                .regDate(movieReview.getRegDate())
                .modDate(movieReview.getModDate())
                .build();

        return reviewDTO;
    }
}
