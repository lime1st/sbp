package com.lime1st.sbp.security.service;

import com.lime1st.sbp.entity.Member;
import com.lime1st.sbp.repository.MemberRepository;
import com.lime1st.sbp.security.dto.AuthMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{

        log.info("=======UserDetailsService loadUserByUsername===========");
        log.info(username);

        // 소셜로그인으로 자동 가입이 된 상황(social: true 상태임)에서는 일반 로그인이 되지 않는다.
        Optional<Member> result = memberRepository.findByEmail(username, false);

        if(!result.isPresent()){
            throw new UsernameNotFoundException("Check Email or Social");
        }

        Member member = result.get();

        log.info("=========UserDetails loadUserByUsername================");
        log.info(member);

        AuthMemberDTO authMemberDTO = new AuthMemberDTO(
                member.getEmail(),
                member.getPassword(),
                member.isFromSosial(),
                member.getRoleSet().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                        .collect(Collectors.toSet())
        );

        authMemberDTO.setName(member.getName());
        authMemberDTO.setFromSocial(member.isFromSosial());
        log.info(authMemberDTO);

        return authMemberDTO;
    }
}
