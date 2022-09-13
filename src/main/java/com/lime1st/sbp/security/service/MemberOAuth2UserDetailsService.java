package com.lime1st.sbp.security.service;

import com.lime1st.sbp.entity.Member;
import com.lime1st.sbp.entity.MemberRole;
import com.lime1st.sbp.repository.MemberRepository;
import com.lime1st.sbp.security.dto.AuthMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberOAuth2UserDetailsService extends DefaultOAuth2UserService {

    private final MemberRepository repository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{

        log.info("-----------OAuth2User loadUser-----------------------");
        log.info("userRequest: " + userRequest);

        String clientName = userRequest.getClientRegistration().getClientName();
        log.info("clientName: " + clientName);      //  로그인한 소셜 이름(지금 "Google"로 출력됨)
        log.info(userRequest.getAdditionalParameters());

        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("=====OAuth2User info=================================");
        oAuth2User.getAttributes().forEach((k, v)->{
           log.info(k + ":" + v);   // sub, picture, email, email_verified, EMAIL 등이 출력됨
        });

        String email = null;

        if(clientName.equals("Google")){    //  Google을 이용하는 경우
            email = oAuth2User.getAttribute("email");
        }

        log.info(("EMAIL: " + email));

        Member member = saveSocialMember(email);

        AuthMemberDTO authMemberDTO = new AuthMemberDTO(
                member.getEmail(),
                member.getPassword(),
                true,   //  fromSocial
                member.getRoleSet().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                        .collect(Collectors.toList()),
                oAuth2User.getAttributes()
        );
        authMemberDTO.setName(member.getName());

        return authMemberDTO;
    }

    private Member saveSocialMember(String email){

        //  기존에 동일한 이메일로 가입한 회원이 있는 경우에는 그대로 조회만
        Optional<Member> result = repository.findByEmail(email, true);

        if(result.isPresent()){
            return result.get();
        }

        //  없다면 회원 추가 패스워드는 1111 이름은 그냥 이메일 주소로
        Member member = Member.builder()
                .email(email)
                .name(email)
                .password(passwordEncoder.encode("1111"))
                .fromSosial(true)
                .build();

        member.addMemberRole(MemberRole.USER);

        log.info("member confirm" + member);

        repository.save(member);

        return member;
    }
}
