package com.batch.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class ApplicationConfig {
	
	/**Configuración de la base de datos. El ejecicio es con H2 por eso esta parte esta comentada, ya que Spring hará la conexión automaticamente*/
//	@Bean
//    DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("org.h2.Driver");
//        dataSource.setUrl("jdbc:h2:mem:testdb");
//        dataSource.setUsername("sa");
//        dataSource.setPassword("sa");
//        return dataSource;
//    }
	
}
