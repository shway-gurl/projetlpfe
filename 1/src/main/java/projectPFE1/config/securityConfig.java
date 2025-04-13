package projectPFE1.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
public class securityConfig {
    @Value("${spring.security.oauth2.client.provider.keycloak.jwk-set-uri}")
    private String jwkSetUri;

    @Bean
    public keycloakConverter customKeycloakConverter() {
        return new keycloakConverter();
    }

    @Bean
    public JwtDecoder decode() {
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider() {
        return new JwtAuthenticationProvider(decode());
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder =
                new MvcRequestMatcher.Builder(introspector);

        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        httpSecurity.cors(AbstractHttpConfigurer::disable);
        httpSecurity

                .authorizeHttpRequests(authorize -> authorize
                        //.requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.OPTIONS, "/**")).permitAll()
                        // .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.GET, "/user/getAllusers")).permitAll()
                        // .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.GET, "/user/getAllusers")).hasAuthority("ROLE_ADMIN")
                        //.requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.GET, "/user/getAllusers")).hasAuthority("GROUP_KANTRA")
                        .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.OPTIONS, "/**")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/**")).permitAll()
                )  // Configuration de l'authentification via OAuth2 et des tokens JWT
                .oauth2ResourceServer(oauth2 -> oauth2
                        // Décode les tokens JWT pour authentifier les utilisateurs
                        .jwt(jwt -> jwt.decoder(decode()).jwtAuthenticationConverter(customKeycloakConverter()))
                )
                // Définition de la politique de gestion des sessions comme "stateless"
                // "Stateless" signifie qu'aucune session n'est conservée entre les requêtes. Chaque requête est authentifiée par un token JWT.
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        // Retourne la chaîne de filtres de sécurité configurée
        return httpSecurity.build();
    }

    @Bean
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new NullAuthenticatedSessionStrategy();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**");
            }
        };
    }


}
