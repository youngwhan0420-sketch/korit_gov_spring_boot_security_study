package com_korit.security_study.security.filter;

import com_korit.security_study.entity.User;
import com_korit.security_study.repository.UserRepository;
import com_korit.security_study.security.jwt.JwtUtils;
import com_korit.security_study.security.model.Principal;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter implements Filter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request =(HttpServletRequest) servletRequest;

        //해당 메소드가 아니면 그냥 다음 필터로 넘기겠다.
        List<String> methods = List.of("POST", "PUT", "GET", "PATCH", "DELETE");
        if (!methods.contains(request.getMethod())) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        String authorization = request.getHeader("Authorization"); //토큰이 들어있을 것이다.
        System.out.println("Bearer 토큰: " + authorization);

        if (jwtUtils.isBearer(authorization)) { //Bearer형식인지 확인 부터 한다.
            String accessToken = jwtUtils.removeBearer(authorization);
            //여기까지만들고 security에 모델을 만든다.
            try {
                Claims claims = jwtUtils.getClaims(accessToken);
                String id = claims.getId();
                Integer userId = Integer.parseInt(id);
                //userId로 회원 조회 => Principal,회원조회할려면 회원조회 로직이 있어야한다.
                Optional<User> foundUser = userRepository.getUserByUserId(userId);
                foundUser.ifPresentOrElse((user -> {Principal principal = Principal.builder()
                        .userId(user.getUserId())
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .email(user.getEmail())
                        .userRoles(user.getUserRoles())
                        .build();
                    //UsernamePasswordAuthenticationToken객체 생성
                    Authentication authentication = new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
                    //인증객체에 저장한다.spring security의 인증 컨텍스트에 인증 객체 저장 => 이후의 요청은 인증된 사용자로 간주됨
                    SecurityContextHolder.getContext().setAuthentication(authentication); //인증 완료
                }), () -> {
                    throw new AuthenticationServiceException("인증 실패");
                });
                //앞에는 유저가 있으면 행하고 뒤에는 유저가 없으면 행한다.
                //존재하면 principal
            } catch (RuntimeException e) { 
                e.printStackTrace();
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
