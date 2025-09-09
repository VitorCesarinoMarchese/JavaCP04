package br.fiap.biblioteca.DAO;

import br.fiap.biblioteca.model.Jogo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JogoDAO {

  public void criar(Jogo a) throws SQLException {
    String sql = "INSERT INTO jogo (titulo, genero, plataforma, status, nota, ano_lancamento) VALUES (?, ?, ?, ?, ?, ?)";
    try (Connection c = DbConnect.getConnection();
        PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      ps.setString(1, a.getTitulo());
      ps.setString(2, a.getGenero());
      ps.setString(3, a.getPlataforma());
      ps.setString(4, a.getStatus());
      ps.setInt(5, a.getNota());
      ps.setInt(6, a.getAno_lancamento());
      ps.executeUpdate();

      try (ResultSet rs = ps.getGeneratedKeys()) {
        if (rs.next())
          a.setId(rs.getInt(1));
      }
    }
  }

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

  public void atualizar(Jogo a) throws SQLException {
    String sql = "UPDATE jogo SET titulo = ?, genero = ?, plataforma = ?, status = ?, nota = ?, ano_lancamento = ? WHERE id=?";
    try (Connection c = DbConnect.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setString(1, a.getTitulo());
      ps.setString(2, a.getGenero());
      ps.setString(3, a.getPlataforma());
      ps.setString(4, a.getStatus());
      ps.setInt(5, a.getNota());
      ps.setInt(6, a.getAno_lancamento());
      ps.setInt(7, a.getId());
      ps.executeUpdate();
    }
  }

  public void deletar(int id) throws SQLException {
    String sql = "DELETE FROM jogo WHERE id=?";
    try (Connection c = DbConnect.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setInt(1, id);
      ps.executeUpdate();
    }
  }

  // --- Métodos "de relevância" (consultas de negócio) ---
  public List<Jogo> buscarPorNomeLike(String termo) throws SQLException {
    String sql = "SELECT id, titulo, genero, plataforma, status, nota, ano_lancamento FROM jogo WHERE nome LIKE ? ORDER BY titulo";
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

  public long contarPorGenero(String genero) throws SQLException {
    String sql = "SELECT COUNT(*) FROM jogo WHERE genero = ?";
    try (Connection c = DbConnect.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setString(1, genero);
      try (ResultSet rs = ps.executeQuery()) {
        return rs.next() ? rs.getLong(1) : 0L;
      }
    }
  }

  public Integer jogoMaisRecente() throws SQLException {
    String sql = "SELECT MAX(ano_lancamento) FROM jogo";
    try (Connection c = DbConnect.getConnection();
        PreparedStatement ps = c.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {
      return rs.next() ? (Integer) rs.getInt(1) : null;
    }
  }

  public Double mediaNotaPorGenero(String curso) throws SQLException {
    String sql = "SELECT AVG(nota) FROM jogo WHERE genero = ?";
    try (Connection c = DbConnect.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setString(1, curso);
      try (ResultSet rs = ps.executeQuery()) {
        return rs.next() ? rs.getDouble(1) : null;
      }
    }
  }

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
