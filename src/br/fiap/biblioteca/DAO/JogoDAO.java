package br.fiap.biblioteca.DAO;

import br.fiap.biblioteca.model.Jogo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JogoDAO {

  /**
   * @param j
   * @throws SQLException
   */
  public void criar(Jogo j) throws SQLException {
    String sql = "INSERT INTO jogo (titulo, genero, plataforma, status, nota, ano_lancamento) VALUES (?, ?, ?, ?, ?, ?)";
    try (Connection c = DbConnect.getConnection();
        PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      ps.setString(1, j.getTitulo());
      ps.setString(2, j.getGenero());
      ps.setString(3, j.getPlataforma());
      ps.setString(4, j.getStatus());
      ps.setInt(5, j.getNota());
      ps.setInt(6, j.getAno_lancamento());
      ps.executeUpdate();

      try (ResultSet rs = ps.getGeneratedKeys()) {
        if (rs.next())
          j.setId(rs.getInt(1));
      }
    }
  }

  /**
   * @param id
   * @return
   * @throws SQLException
   */
  public Jogo buscarPorId(int id) throws SQLException {
    String sql = "SELECT id, titulo, genero, plataforma, status, nota, ano_lancamento FROM jogo WHERE id = ?";
    try (Connection c = DbConnect.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next())
          return map(rs);
      }
    }
    return null;
  }

  /**
   * @param titulo
   * @param plataforma
   * @return
   * @throws SQLException
   */
  public Jogo validarExistencia(String titulo, String plataforma) throws SQLException {
    String sql = "SELECT id, titulo, genero, plataforma, status, nota, ano_lancamento " +
        "FROM jogo WHERE titulo = ? AND plataforma = ? LIMIT 1";
    try (Connection c = DbConnect.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setString(1, titulo);
      ps.setString(2, plataforma);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next())
          return map(rs);
      }
    }
    return null;
  }

  /**
   * @return
   * @throws SQLException
   */
  public List<Jogo> listarTodos() throws SQLException {
    String sql = "SELECT id, titulo, genero, plataforma, status, nota, ano_lancamento FROM jogo ORDER BY id";
    List<Jogo> lista = new ArrayList<>();
    try (Connection c = DbConnect.getConnection();
        PreparedStatement ps = c.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {
      while (rs.next())
        lista.add(map(rs));
    }
    return lista;
  }

  /**
   * @param genero
   * @return
   * @throws SQLException
   */
  public List<Jogo> listarTodosComGenero(String genero) throws SQLException {
    String sql = "SELECT id, titulo, genero, plataforma, status, nota, ano_lancamento FROM jogo WHERE genero = ? ORDER BY id";
    List<Jogo> lista = new ArrayList<>();
    try (Connection c = DbConnect.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setString(1, genero);
      ps.executeUpdate();

      ResultSet rs = ps.executeQuery();
      while (rs.next())
        lista.add(map(rs));
    }
    return lista;
  }

  /**
   * @param plataforma
   * @return
   * @throws SQLException
   */
  public List<Jogo> listarTodosComPlataforma(String plataforma) throws SQLException {
    String sql = "SELECT id, titulo, genero, plataforma, status, nota, ano_lancamento FROM jogo WHERE plataforma = ? ORDER BY id";
    List<Jogo> lista = new ArrayList<>();
    try (Connection c = DbConnect.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setString(1, plataforma);
      ps.executeUpdate();

      ResultSet rs = ps.executeQuery();
      while (rs.next())
        lista.add(map(rs));
    }
    return lista;
  }

  /**
   * @param status
   * @return
   * @throws SQLException
   */
  public List<Jogo> listarTodosComStatus(String status) throws SQLException {
    String sql = "SELECT id, titulo, genero, plataforma, status, nota, ano_lancamento FROM jogo WHERE status = ? ORDER BY id";
    List<Jogo> lista = new ArrayList<>();
    try (Connection c = DbConnect.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setString(1, status);
      ps.executeUpdate();

      ResultSet rs = ps.executeQuery();
      while (rs.next())
        lista.add(map(rs));
    }
    return lista;
  }

  /**
   * @param j
   * @throws SQLException
   */
  public void atualizar(Jogo j) throws SQLException {
    String sql = "UPDATE jogo SET titulo = ?, genero = ?, plataforma = ?, status = ?, nota = ?, ano_lancamento = ? WHERE id=?";
    try (Connection c = DbConnect.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setString(1, j.getTitulo());
      ps.setString(2, j.getGenero());
      ps.setString(3, j.getPlataforma());
      ps.setString(4, j.getStatus());
      ps.setInt(5, j.getNota());
      ps.setInt(6, j.getAno_lancamento());
      ps.setInt(7, j.getId());
      ps.executeUpdate();
    }
  }

  /**
   * @param id
   * @throws SQLException
   */
  public void deletar(int id) throws SQLException {
    String sql = "DELETE FROM jogo WHERE id=?";
    try (Connection c = DbConnect.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setInt(1, id);
      ps.executeUpdate();
    }
  }

  // --- Busca por titulo que contenha ---
  /**
   * @param termo
   * @return
   * @throws SQLException
   */
  public List<Jogo> buscarPorTituloLike(String termo) throws SQLException {
    String sql = "SELECT id, titulo, genero, plataforma, status, nota, ano_lancamento FROM jogo WHERE titulo LIKE ? ORDER BY titulo";
    List<Jogo> lista = new ArrayList<>();
    try (Connection c = DbConnect.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setString(1, "%" + termo + "%");
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next())
          lista.add(map(rs));
      }
    }
    return lista;
  }

  /**
   * @param genero
   * @return
   * @throws SQLException
   */
  public long contarConcluidosPorGenero(String genero) throws SQLException {
    String sql = "SELECT COUNT(*) FROM jogo WHERE genero = ? AND WHERE status = 'Concluído'";
    try (Connection c = DbConnect.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setString(1, genero);
      try (ResultSet rs = ps.executeQuery()) {
        return rs.next() ? rs.getLong(1) : 0L;
      }
    }
  }

  /**
   * @param plataforma
   * @return
   * @throws SQLException
   */
  public long contarConcluidosPorPlataforma(String plataforma) throws SQLException {
    String sql = "SELECT COUNT(*) FROM jogo WHERE plataforma = ? AND WHERE status = 'Concluído'";
    try (Connection c = DbConnect.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setString(1, plataforma);
      try (ResultSet rs = ps.executeQuery()) {
        return rs.next() ? rs.getLong(1) : 0L;
      }
    }
  }

  /**
   * @return
   * @throws SQLException
   */
  public Integer jogoMaisRecente() throws SQLException {
    String sql = "SELECT MAX(ano_lancamento) FROM jogo";
    try (Connection c = DbConnect.getConnection();
        PreparedStatement ps = c.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {
      return rs.next() ? (Integer) rs.getInt(1) : null;
    }
  }

  /**
   * @param genero
   * @return
   * @throws SQLException
   */
  public Double mediaNotaPorGenero(String genero) throws SQLException {
    String sql = "SELECT AVG(nota) FROM jogo WHERE genero = ?";
    try (Connection c = DbConnect.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setString(1, genero);
      try (ResultSet rs = ps.executeQuery()) {
        return rs.next() ? rs.getDouble(1) : null;
      }
    }
  }

  /**
   * @param rs
   * @return
   * @throws SQLException
   */
  private Jogo map(ResultSet rs) throws SQLException {
    return new Jogo(
        rs.getInt("id"),
        rs.getString("titulo"),
        rs.getString("genero"),
        rs.getString("plataforma"),
        rs.getString("status"),
        rs.getInt("nota"),
        rs.getInt("ano_lancamento"));
  }
}
