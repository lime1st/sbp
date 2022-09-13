package com.lime1st.sbp.security;

import com.lime1st.sbp.entity.Member;
import com.lime1st.sbp.entity.MemberRole;
import com.lime1st.sbp.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemberTests {

    @Autowired
    private MemberRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //  더미 회원 추가
    @Test
    public void insertDummies(){

        //  회원의 권한 지정
        //  1-80까지는 USER
        //  81-90까지는 USER, MANAGER
        //  91-100까지는 USER, MANAGER, ADMIN
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Member member = Member.builder()
                    .email("user" + i + "@lime.com")
                    .name("사용자" + i)
                    .fromSosial(false)
                    .password(passwordEncoder.encode("1111"))
                    .build();

            //  default role
            member.addMemberRole(MemberRole.USER);

            if(i > 80){
                member.addMemberRole(MemberRole.MANAGER);
            }
            if(i > 90){
                member.addMemberRole(MemberRole.ADMIN);
            }

            repository.save(member);
        });
    }

    @Test
    public void testRead(){

        Optional<Member> result = repository.findByEmail("user95@lime.com", false);

        Member member = result.get();

        System.out.println(member);
    }
}
