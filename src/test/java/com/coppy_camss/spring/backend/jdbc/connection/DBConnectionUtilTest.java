package com.coppy_camss.spring.backend.jdbc.connection;
import com.coppy_camss.spring.backend.jdbc.connection.DBConnectionUtil;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.coppy_camss.spring.backend.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.*;

@Slf4j
class DBConnectionUtilTest {

	@Test
	void connection() throws SQLException {
		Connection connection = DBConnectionUtil.getConnection();
		assertThat(connection).isNotNull();
	}
	@Test
	void dataSourceDriverManager() throws SQLException{
		DriverManagerDataSource dataSource = new DriverManagerDataSource(URL,USERNAME,PASSWORD);
		useDataSource(dataSource);
	}
	private void useDataSource(DataSource dataSource) throws SQLException{
		Connection con1 = dataSource.getConnection();
		Connection con2 = dataSource.getConnection();
		log.info("con1 ={}", con1);
		log.info("con2 ={}", con2);
		con1.close();
		con2.close();
	}

	@Test void dataSourceConnectionPool() throws SQLException, InterruptedException {
		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setJdbcUrl(URL);
		dataSource.setUsername(USERNAME);
		dataSource.setPassword(PASSWORD);
		dataSource.setMaximumPoolSize(10);
		dataSource.setPoolName("pool");

		useDataSource(dataSource);
		Thread.sleep(1000);
	}
}
