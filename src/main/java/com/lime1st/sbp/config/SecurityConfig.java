package com.lime1st.sbp.config;


import com.lime1st.sbp.security.filter.ApiCheckFilter;
import com.lime1st.sbp.security.filter.ApiLoginFilter;
import com.lime1st.sbp.security.handler.ApiLoginFailHandler;
import com.lime1st.sbp.security.handler.MemberLoginSuccessHandler;
import com.lime1st.sbp.security.service.MemberUserDetailsService;
import com.lime1st.sbp.security.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Log4j2
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)   //  어노테이션 기반의 접근 제한을 설정할 수 있도록 하는 설정
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MemberUserDetailsService userDetailsService;

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{

        //  @PreAuthorize 적용 후 주석처리 함->인증을 하고 싶은 페이지의 콘트롤러 함수 위에
        //  @PreAuthorize(적용권한 예:"hasRole('ADMIN')) 애너테이션을 적용
//        http.authorizeRequests()
//                .antMatchers("/sample/all").permitAll()
//                .antMatchers("/sample/member").hasRole("USER");

        http.formLogin();       //  인가-인증 문제 시 로그인
        http.csrf().disable();  //  csrf토큰 비활성화- CSRF 공격(사이트간 요청 위조) 방지
        http.logout();
        http.oauth2Login().successHandler(successHandler());    // 소셜 로그인, 로그인 후 회원 수정으로
        // 쿠키 사용(rememberMe), 쿠키 유지일 지정(초단위) 7일
        http.rememberMe().tokenValiditySeconds(60 * 60 * 24 * 7).userDetailsService(userDetailsService);

        // ApiCheckFilter를 UsernamePasswordAuthenticationFilter 이전에 동작하도록 지정함
        http.addFilterBefore(apiCheckFilter(), UsernamePasswordAuthenticationFilter.class);

        // ApiLoginFilter를 적용
        http.addFilterBefore(apiLoginFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public ApiCheckFilter apiCheckFilter(){
        //  REST 요청 시 패턴에 맞지 검사하기

//        log.info("apiCheckFilter============================");

        return new ApiCheckFilter("/notes/**/*", jwtUtil());
    }

    @Bean
    public ApiLoginFilter apiLoginFilter() throws Exception{
        // "/api/login" 경로로 접근할 때 필터가 동작하도록 설정

//        log.info("apiLoginFilter============================");

        ApiLoginFilter apiLoginFilter = new ApiLoginFilter("/api/login", jwtUtil());
        apiLoginFilter.setAuthenticationManager(authenticationManager());

        apiLoginFilter.setAuthenticationFailureHandler(new ApiLoginFailHandler());

        return apiLoginFilter;
    }

    @Bean
    public MemberLoginSuccessHandler successHandler(){
        return new MemberLoginSuccessHandler(passwordEncoder());
    }

    @Bean
    public JWTUtil jwtUtil(){
        return new JWTUtil();
    }

//    사용하지 않음
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        //  user1
//        auth.inMemoryAuthentication().withUser("user1")
//                //  1111
//                .password("$2a$10$3IIR1tJ9q6/hG7lGWEVYvOoOTgagEjr7qRelN02F7GednQSeRecii")
//                .roles("USER");
//    }
}
