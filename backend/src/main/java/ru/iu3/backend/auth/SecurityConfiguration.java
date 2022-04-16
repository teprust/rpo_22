package ru.iu3.backend.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;


/*
В настройках указывается шаблон для определения локального пути, для которого действует фильтр. Это все
что находится внутри /api/. По умолчанию все локальные пути защищены, поэтому надо сделать исключение
для API логина /auth/. Здесь же указывается класс провайдера, который мы сделали только что.
Здесь надо обратить внимание на то, что отключены стандартные средства spring boot для авторизации и
выхода, а также отключено управление сессией (NEVER), так как технология JWT, которую мы планируем
применить не использует свойства сессии. К этому еще вернемся.
 */


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final RequestMatcher PROTECTED_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/api/**")
    );
    // Провайдер для аутентификации
    AuthenticationProvider provider;

    //Конструктор
    public SecurityConfiguration(final AuthenticationProvider authenticationProvider){
        super();
        this.provider = authenticationProvider;
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(provider);
    }

    // Указываем адресный путь, откуда будет вызываться авторизация

    @Override
    public void configure(final WebSecurity webSecurity) {
        webSecurity.ignoring().antMatchers("/auth/login");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // Устанавливаем определённую политику доступа
    http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.NEVER)
            .and()
            .exceptionHandling()
            .and()
            .authenticationProvider(provider)
            .addFilterBefore(authenticationFilter(), AnonymousAuthenticationFilter.class)
                    .authorizeRequests()
                    .requestMatchers(PROTECTED_URLS)
                    .authenticated()
                    .and()
                    .csrf().disable()
                    .formLogin().disable()
                    .httpBasic().disable()
                    .logout().disable()
                    .cors();
    }


    /*
    JavaBeans — классы в языке Java, написанные по определённым правилам. Они используются для объединения нескольких объектов в один для удобной передачи данных.
     */

    // Метод - дополнительная фильтрация. Извлекать можно только из определённого диапазона адресов
    @Bean
    AuthenticationFilter authenticationFilter() throws Exception {
        final AuthenticationFilter filter = new AuthenticationFilter(PROTECTED_URLS);
                filter.setAuthenticationManager(authenticationManager());
                //filter.setAuthenticationSuccessHandler(successHandler());
        return filter;
    }

    // Метод, возвращающий статус ошибки - 403
    @Bean
    AuthenticationEntryPoint forbiddenEntryPoint() {
        return new HttpStatusEntryPoint(HttpStatus.FORBIDDEN);
    }
}
