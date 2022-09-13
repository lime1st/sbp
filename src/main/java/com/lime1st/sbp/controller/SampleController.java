package com.lime1st.sbp.controller;

import com.lime1st.sbp.security.dto.AuthMemberDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequestMapping("/sample")
public class SampleController {

    // 특정 사용자만 접근을 허용할 때
    @PreAuthorize("#authMemberDTO != null && #authMemberDTO.username eq \"user95@lime.com\"")
    @GetMapping("/exOnly")
    public String exMemberOnly(@AuthenticationPrincipal AuthMemberDTO authMemberDTO){
        log.info("exMemberOnly.....................");
        log.info(authMemberDTO);

        return "/sample/admin";
    }



    @PreAuthorize("permitAll()")    //  모든 사용자가 볼 수 있음
    @GetMapping("/all")
    public void exAll(){
        log.info("exAll...................");
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/member")
    public void exMember(@AuthenticationPrincipal AuthMemberDTO authMemberDTO){

        log.info("exmember........................");
        log.info("________________________________");
        log.info(authMemberDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")   //  ADMIN 권한이 있는 사용자만 볼 수 있는 페이지
    @GetMapping("/admin")
    public void exAdmin(){
        log.info("exAdmin...................");
    }
}
