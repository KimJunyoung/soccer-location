package com.soccerlocation.config.argumentresolver;

import com.soccerlocation.config.AppConfig;
import com.soccerlocation.config.data.UserSession;
import com.soccerlocation.exception.Unauthorized;
import com.soccerlocation.repository.SessionRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import javax.crypto.SecretKey;


@Slf4j
@RequiredArgsConstructor
public class AuthResolver  implements HandlerMethodArgumentResolver {
    /**
     * 사용 된 곳에서 적합한가 ?
     */
    private final SessionRepository sessionRepository;
    private final AppConfig appConfig;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return UserSession.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        log.info(" >>>>>>>> {}", appConfig.toString());

        String jws = webRequest.getHeader("Authorization");

        if(jws == null || jws.equals("")){
            throw new Unauthorized();
        }

        // 복호화
        SecretKey secretKey = Keys.hmacShaKeyFor(appConfig.getJwtKey());

        try {
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(jws);

            String userId = claims.getPayload().getSubject();
            return new UserSession(Long.parseLong(userId));

        } catch (JwtException e) {

            throw new Unauthorized();
        }

    }
}
