package br.fiap.biblioteca.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnect {

  // SQLite não precisa de user!!!
  // private static final String USER = "admin";
  // private static final String PASS = "admin";
  private static final String URL = "jdbc:sqlite:db.sqlite";

  public static Connection getConnection() throws SQLException {
    try {
      Class.forName("org.sqlite.JDBC");
    } catch (ClassNotFoundException e) {
      throw new SQLException("Driver SQLite não encontrado. Adicione o sqlite-jdbc ao classpath.", e);
    }
    return DriverManager.getConnection(URL);
  }
}
