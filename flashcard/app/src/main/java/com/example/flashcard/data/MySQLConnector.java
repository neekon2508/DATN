package com.example.flashcard.data;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
public class MySQLConnector implements AutoCloseable {

    private static String host ="mysql-26a06367-neekon250803.j.aivencloud.com";
    private static String port = "12350";
    private static String databaseName="flashcard";
    private static String userName="avnadmin";
    private static String password="";
    private static Connection connection;
    private static PreparedStatement preparedStatement;
    private static ResultSet resultset;

    public MySQLConnector() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + databaseName + "?sslmode=require", userName, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void close() throws SQLException {
     if(resultset!=null && !resultset.isClosed()) resultset.close();
     if(preparedStatement!=null && !preparedStatement.isClosed())preparedStatement.close();
     if (connection!=null && !connection.isClosed()) connection.close();
    }
    public void signUp(String name, String password) throws SQLException {
        preparedStatement = connection.prepareStatement("INSERT INTO ACCOUNT(name, password) VALUES(?, ?) ");
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, password);
        preparedStatement.executeUpdate();
    }

}
