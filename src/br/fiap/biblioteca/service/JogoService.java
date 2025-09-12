package br.fiap.biblioteca.service;

import br.fiap.biblioteca.DAO.JogoDAO;
import br.fiap.biblioteca.model.Jogo;

import java.io.ObjectInputFilter.Status;
import java.sql.SQLException;
import java.time.Year;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JogoService {

  private JogoDAO dao = new JogoDAO();
  private static final Set<String> STATUS_VALIDOS = new HashSet<>(Arrays.asList("Jogando", "Concluido", "Wishlist"));
  private static final Set<String> GENEROS_VALIDOS = new HashSet<>(Arrays.asList("Ação", "RPG", "Estratégia"));
  private static final Set<String> PLATAFORMAS_VALIDAS = new HashSet<>(
      Arrays.asList("PC", "Xbox", "PlayStation", "Switch"));

  /**
   * Validação de regras de negócio
   *
   * @throws SQLException
   */
  public void validarJogo(Jogo j) throws SQLException {
    List<Jogo> search = dao.buscarPorNomeLike(j.getTitulo());
    if (search.size() > 0 && search.get(0).getPlataforma() == j.getPlataforma())
      throw new IllegalArgumentException("O jogo deve conter um titulo e ou plataforma unica");

    if (j.getTitulo() == null || j.getTitulo().trim().length() < 2)
      throw new IllegalArgumentException("O titulo deve ter ao menos 2 caracteres.");

    if (j.getNota() < 0 || j.getNota() > 10)
      throw new IllegalArgumentException("A nota deve ser um numero entre 0 e 10");

    if (j.getAno_lancamento() == 0 || j.getAno_lancamento() > Year.now().getValue())
      throw new IllegalArgumentException("O ano de lançamento deve ser maior que 0 e no maximo no ano atual");

    if (j.getStatus() == null || !STATUS_VALIDOS.contains(j.getStatus()))
      throw new IllegalArgumentException("Status inválido. Válidos: " + STATUS_VALIDOS);

    if (j.getGenero() == null || !GENEROS_VALIDOS.contains(j.getGenero()))
      throw new IllegalArgumentException("Gênero inválido. Válidos: " + GENEROS_VALIDOS);

    if (j.getPlataforma() == null || !PLATAFORMAS_VALIDAS.contains(j.getPlataforma()))
      throw new IllegalArgumentException("Plataforma inválido. Válidos: " + PLATAFORMAS_VALIDAS);
  }

  /**
   * 2) Atualizacao de status do Jogo
   * 
   * @throws SQLException
   */
  public String atualizarStatusDoJogo(Jogo j, String status) throws SQLException {
    j.setStatus(status);
    dao.atualizar(j);
    return j.toString();
  }
}
