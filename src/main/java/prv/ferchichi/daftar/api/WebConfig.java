package prv.ferchichi.daftar.api;

import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

public class WebConfig {

	@Bean
	CorsWebFilter corsFilter() {

	    CorsConfiguration config = new CorsConfiguration();

	    // Possibly...
	    // config.applyPermitDefaultValues()

	    config.setAllowCredentials(true);
	    config.addAllowedOrigin("http://localhost:8080");
	    config.addAllowedHeader("*");
	    config.addAllowedMethod("*");

	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", config);

	    return new CorsWebFilter(source);
	}
}
