package db

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {

  private static final String URL = "jdbc:sqlite:banco.sqlite";

  public static Connect getConnection() throws SQLException {
    try {
      Class.forName("org.sqlite.JDBC");
    } catch (ClassNotFoundException e) {
      throw new SQLException("Driver SQLite não encontrado. Adicione o sqlite-jdbc ao classpath.", e);
    }
    return DriverManager.getConnection(URL);
  }
}
