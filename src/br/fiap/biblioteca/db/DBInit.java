package br.fiap.biblioteca.db;

import br.fiap.biblioteca.DAO.DbConnect;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBInit {
  /**
   * @throws SQLException
   */
  public static void ensureCreated() throws SQLException {
    String sql = "CREATE TABLE IF NOT EXISTS jogo (" +
        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
        "titulo TEXT NOT NULL," +
        "genero TEXT NOT NULL," +
        "plataforma TEXT NOT NULL," +
        "status TEXT NOT NULL," + // “Jogando”, “Concluído”, “Wishlist”
        "nota INTEGER NOT NULL" +
        "ano_lancamento INTEGER NOT NULL" +
        ")";
    try (Connection c = DbConnect.getConnection();
        Statement st = c.createStatement()) {
      st.execute(sql);
    }
  }
}
