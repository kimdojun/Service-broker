package org.openpaas.servicebroker.mysql.config;

import java.net.UnknownHostException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
//import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@PropertySource("classpath:datasource.properties")
//@PropertySources(value = {@PropertySource("classpath:/datasource.properties")})
public class MysqlConfig {

//	@Value("${jdbc.driver}")
//	private String jdbcDriver;
	
	@Autowired
	private Environment env;
//	@Bean
//	public PropertyPlaceholderConfigurer properties() {
//		PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
//		Resource[] resources = new ClassPathResource[] { 
//				new ClassPathResource("mysql.properties")
//		};
//		propertyPlaceholderConfigurer.setLocations(resources);
//		propertyPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders(true);
//		return propertyPlaceholderConfigurer;
//	}
	@Bean
	public JdbcTemplate jdbcTemplate() throws UnknownHostException {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());
		return jdbcTemplate;
	}
	
	//@Override
	@Bean
    public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		//dataSource.setDriverClassName(env.getRequiredProperty("jdbc.driver"));
		dataSource.setUrl("jdbc:mysql://192.168.4.41:3306");
		dataSource.setUsername("borker");
		dataSource.setPassword("qwer1234");
//
		return dataSource;
        // instantiate, configure and return DataSource
    }
	
}