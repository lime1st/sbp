package com.lime1st.sbp.security.filter;

import com.lime1st.sbp.security.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Log4j2
public class ApiCheckFilter extends OncePerRequestFilter {

    private AntPathMatcher antPathMatcher;
    private String pattern;
    private JWTUtil jwtUtil;    //  Authorization 헤더 메시지를 통해 JWT를 확인하도록 수정하기 위해 추가(생성자도 변경)

    public ApiCheckFilter(String pattern, JWTUtil jwtUtil){
        this.antPathMatcher = new AntPathMatcher();
        this.pattern = pattern;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{

        log.info("=====doFilterInternal=======REQUESTURI: " + request.getRequestURI());
        log.info(antPathMatcher.match(pattern, request.getRequestURI()));

        if(antPathMatcher.match(pattern, request.getRequestURI())) {    // "/notes/" 로 시작하는 경로에만 로그가 출력

            log.info("ApiCheckFilter...............................");
            log.info("ApiCheckFilter...............................");
            log.info("ApiCheckFilter...............................");

            boolean checkHeader = checkAuthHeader(request);

            if(checkHeader) {
                filterChain.doFilter(request, response);
                return;
            }else{  // checkAuthHeader가 false를 반환하면
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                //  json 리턴 및 한글깨짐 수정
                response.setContentType("application/json; charset=utf-8");
                JSONObject json = new JSONObject();
                String message = "FAIL! CHECK API TOKEN";
                json.put("code", "403");
                json.put("message", message);

                PrintWriter out = response.getWriter();
                out.print(json);
                return;
            }
        }

        filterChain.doFilter(request, response);

    }

    private boolean checkAuthHeader(HttpServletRequest request){
        //  내부에서 Authorization 헤더를 추출해서 검증하는 역할을 수행
        //  Authorization 헤더 메시지의 경우 앞에는 인증 타입을 이용하는 데 일반적인 경우에는 Basic을 사용하고 JWT를 사용할 때는 'Bearer'를 사용한다.
        //  Authorization 이라는 헤더의 값을 확인하고 boolean 타입의 결과를 반환
        //  이를 이용해서 위의 함수(doFilterInternal)에서 다음 필터로 진행할 것인지를 결정함

        boolean checkResult = false;

        String authHeader = request.getHeader("Authorization");

        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")){
            log.info("Authorization exist: " + authHeader);

            try{
                String email = jwtUtil.validateAndExtract(authHeader.substring(7));
                log.info("ApiCheckFilter.checkAuthHeader=================");
                log.info("validate result: " + email);
                checkResult = email.length() > 0;
            }catch (Exception e){
                e.printStackTrace();
            }

//            if(authHeader.equals("12345678")){
//                checkResult = true;
//            }
        }

        return checkResult;
    }
}
