package com.example.auth.service.jwt;


import com.example.auth.config.JwtCookieProperties;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CookieService {

    private final JwtCookieProperties jwtCookieProperties;



    public void setRefreshTokenCookie(HttpServletResponse servletResponse,String refreshToken){
          ResponseCookie responseCookie = buildRefreshTokenCookie(refreshToken);
          servletResponse.addHeader(HttpHeaders.SET_COOKIE,responseCookie.toString());
    }

    public ResponseCookie buildRefreshTokenCookie(String refreshToken){
        return ResponseCookie.from(jwtCookieProperties.getName(),refreshToken)
                .httpOnly(jwtCookieProperties.isHttpOnly())
                .path(jwtCookieProperties.getPath())
                .maxAge(jwtCookieProperties.getMaxAge())
                .sameSite(jwtCookieProperties.getSameSite())
                .secure(jwtCookieProperties.isSecure())
                .domain(jwtCookieProperties.getDomain())
                .build();
    }


    public void deleteRefreshTokenOnCookie(HttpServletResponse servletResponse) {
      ResponseCookie cookie =  ResponseCookie.from(jwtCookieProperties.getName(),"")
                .httpOnly(jwtCookieProperties.isHttpOnly())
                .path(jwtCookieProperties.getPath())
                .maxAge(0)
                .sameSite(jwtCookieProperties.getSameSite())
                .secure(jwtCookieProperties.isSecure())
                .domain(jwtCookieProperties.getDomain())
                .build();
        servletResponse.addHeader("Set-Cookie",cookie.toString());

    }
}
