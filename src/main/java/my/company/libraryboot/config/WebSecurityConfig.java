package my.company.libraryboot.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.company.libraryboot.repository.UserRepository;
import my.company.libraryboot.web.auth.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@Slf4j
@AllArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final PasswordEncoder PASSWORD_ENCODER = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    private final UserRepository userRepository;

//    @Bean
//    public UserDetailsService userDetailsService() {
//        return email -> {
//            log.debug("Authenticating '{}'", email);
//            Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(email);
//            return new AuthUser(optionalUser.orElseThrow(
//                    () -> new UsernameNotFoundException("User '" + email + "' was not found")));
//        };
//    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(PASSWORD_ENCODER);
    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/api/account/register").anonymous()
//                .antMatchers("/api/account").hasRole(Role.USER.name())
//                .antMatchers("/api/**").hasRole(Role.ADMIN.name())
////                .antMatchers("/api/**").hasRole(Role.USER.name())
//                .and().httpBasic()
//                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and().headers().frameOptions().disable() // to display h2 console correctly http://localhost:8080/h2-console
//                .and().csrf().disable();
//
//            // .antMatchers("/admin/**").hasRole("ADMIN")
//            //                .antMatchers("/anonymous*").anonymous()
//            //                .antMatchers("/login*").permitAll()
//            //                .anyRequest().authenticated()
//
//            // CONFIGURE Login Form:        https://www.baeldung.com/spring-security-login
//
//    }


//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//                .authorizeRequests()
//                //Доступ только для не зарегистрированных пользователей
//                .antMatchers("/register").not().authenticated()
//                .antMatchers("/login").not().authenticated()
//                //Доступ только для пользователей с ролью Администратор
//                .antMatchers("/api/authors/**").hasRole("ADMIN")
//                .antMatchers("/api/books/**").hasRole("USER")
//                //Доступ разрешен всем пользователей
////                .antMatchers("/", "/api/**").permitAll()
//                //Все остальные страницы требуют аутентификации
//                .anyRequest().authenticated()
//                .and()
//                //Настройка для входа в систему
//                .formLogin()
//                .loginPage("/login")
//                //Перенарпавление на главную страницу после успешного входа
//                .defaultSuccessUrl("/")
//                .permitAll()
//                .and()
//                .logout()
//                .permitAll()
//                .logoutSuccessUrl("/api");
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/resources/**", "/registration").permitAll()
                .antMatchers("/api/users/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }
}