package ru.iu3.backend.auth;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter{
    // Конструктор
    AuthenticationFilter(final RequestMatcher requiresAuth) {
        super(requiresAuth);
    }
    /*
    Метод attemptAuthentication вызывается каждый раз, когда сервис получает HTTP запрос. Здесь из заголовка
    запроса извлекается токен и передается для аутентификации. Если аутентификация прошла успешно, запрос
    передается обработчику, иначе возвращается ответ с кодом 401 – ошибка авторизации. HTTP заголовок,
    который в тексте задается константой AUTHORIZATION. Выглядит примерно так
    Authorization: Bearer 77f2d300-e3d7-47f5-9d2f-577bf91dbc95
    После слова Bearer через пробел указывается токен.
     */

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse response)
        throws AuthenticationException, IOException, ServletException {
        //Извлекаем заголовки
        Enumeration  headerNames = httpServletRequest.getHeaderNames();
        String token= httpServletRequest.getHeader(AUTHORIZATION);
        if (token != null) {
        token = StringUtils.removeStart(token, "Bearer").trim();
        }
        Authentication requestAuthentication = new UsernamePasswordAuthenticationToken(httpServletRequest, token);
        return getAuthenticationManager().authenticate(requestAuthentication);
    }
    @Override
    protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response, FilterChain chain, Authentication authResult)
        throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);chain.doFilter(request, response);
        }
}
