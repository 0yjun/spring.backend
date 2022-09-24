package com.coppy_camss.spring.backend.jdbc.connection;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.coppy_camss.spring.backend.jdbc.connection.ConnectionConst.*;

@Slf4j
public class DBConnectionUtil {
    public static Connection getConnection() {

        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            log.info("get connection ={}", connection);
            return connection;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

    }
}
