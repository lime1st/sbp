package com.lime1st.sbp.repository;

import com.lime1st.sbp.entity.Member;
import com.lime1st.sbp.entity.Movie;
import com.lime1st.sbp.entity.Review;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
public class ReviewRepositoryTests {

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    public void insertMovieReviews(){

        //  200개의 리뷰를 등록
        IntStream.rangeClosed(1, 200).forEach(i -> {

            //  영화 번호
            Long mno = (long)(Math.random() * 100) + 1;
            //  리뷰어 이메일 주소
            String email = "USER" + ((Math.random() * 100) + 1) + "@lime.com";

            Review movieReview = Review.builder()
                    .member(Member.builder().email(email).build())
                    .movie(Movie.builder().mno(mno).build())
                    .grade((int)(Math.random() * 5) + 1)
                    .text("이 영화에 대한 느낀....." + i)
                    .build();

            reviewRepository.save(movieReview);

        });
    }

    @Transactional
    @Test
    public void testGetMovieReview(){

        Movie movie = Movie.builder().mno(97L).build();

        List<Review> result = reviewRepository.findByMovie(movie);

        result.forEach(movieReview -> {

            System.out.println(movieReview.getReviewnum());
            System.out.println("\t" + movieReview.getGrade());
            System.out.println("\t" + movieReview.getText());
            System.out.println("\t" + movieReview.getMember().getEmail());
            System.out.println("-----------------------------------");
        });
    }
}
