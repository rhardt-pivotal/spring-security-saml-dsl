package com.example;

import static com.example.SAMLConfigurer.saml;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.saml.SAMLBootstrap;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	//@Value("${security.saml2.metadata-url}")
	//String metadataUrl;
	@Value("${app.hostname}")
	String appHostname;


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/saml/**").permitAll()
				.anyRequest().authenticated()
				.and()
			.apply(saml())
				.serviceProvider()
					.keyStore()
						.storeFilePath("saml/keystore.jks")
						.password("secret")
						.keyname("spring")
						.keyPassword("secret")
						.and()
					.protocol("https")
					.hostname(appHostname)
					.basePath("/")
					.and()
				.identityProvider()
					.metadataFilePath("saml/metadata.xml")
					.and()
				.init(http);

	}


}
