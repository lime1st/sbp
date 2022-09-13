package com.lime1st.sbp.repository;

import com.lime1st.sbp.entity.Member;
import com.lime1st.sbp.service.BoardServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@SpringBootTest
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BoardServiceImpl boardService;

    @Test
    public void insertMembers(){

        IntStream.rangeClosed(1, 100).forEach(i ->{

            Member member = Member.builder()
                    .email("user" + i + "@aaa.com")
                    .password("1111")
                    .name("USER" + i)
                    .build();

            memberRepository.save(member);
        });
    }

    @Commit
    @Transactional
    @Test
    public void testDeleteMember(){
        //  email로 찾아서 삭제
//        Member member = Member.builder().email(email).build();


//        boardService.removeWithReplies(3L);
//        reviewRepository.deleteByMember(member);
//        memberRepository.deleteByEmail(email);
    }
}
