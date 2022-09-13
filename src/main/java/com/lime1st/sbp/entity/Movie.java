package com.lime1st.sbp.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "posterList")
public class Movie extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;

    private String title;

    //  movie 와 poster 의 관계를 1:N 으로 지정, mappedBy 속성을 이용해 자신(Movie)은 '연관관계의 주인(owner)'이 아니라는 것을 명시함
    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "movie")
    private List<Poster> posterList = new ArrayList<>();

    public void addPoster(Poster poster){

        poster.setIdx(this.posterList.size());
        poster.setMovie(this);
        posterList.add(poster);
    }
}
