package br.fiap.biblioteca.view;

import br.fiap.biblioteca.DAO.JogoDAO;
import br.fiap.biblioteca.db.DBInit;
import br.fiap.biblioteca.model.Jogo;
import br.fiap.biblioteca.service.JogoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class JogoView extends JFrame {

  private JTextField txtId = new JTextField(5);
  private JTextField txtTitulo = new JTextField(20);
  private JTextField txtNota = new JTextField(5);
  private JComboBox<String> cbGenero = new JComboBox<>(new String[] {
      "Ação", "RPG", "Estratégia"
  });
  private JComboBox<String> cbPlataforma = new JComboBox<>(new String[] {
      "PC", "Xbox", "PlayStation", "Switch"
  });
  private JComboBox<String> cbStatus = new JComboBox<>(new String[] {
      "Jogando", "Concluido", "Wishlist" });
  private JTextField txtAno = new JTextField(5);
  private JTextField txtBuscar = new JTextField(15);

  private DefaultTableModel model = new DefaultTableModel(
      new Object[] { "titulo", " genero", "  plataforma", " status", "  nota", " ano_lancamento" }, 0);
  private JTable tabela = new JTable(model);

  private JogoDAO dao = new JogoDAO();
  private JogoService service = new JogoService();

  public JogoView() {
    super("Busca de jogos - Swing + SQLite + DAO");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(800, 500);
    setLocationRelativeTo(null);

    JPanel form = new JPanel(new GridBagLayout());
    GridBagConstraints g = new GridBagConstraints();
    g.insets = new Insets(5, 5, 5, 5);
    g.fill = GridBagConstraints.HORIZONTAL;

    int y = 0;
    g.gridx = 0;
    g.gridy = y;
    form.add(new JLabel("ID:"), g);
    g.gridx = 1;
    g.gridy = y;
    txtId.setEditable(false);
    form.add(txtId, g);

    y++;
    g.gridx = 0;
    g.gridy = y;
    form.add(new JLabel("Nome:"), g);
    g.gridx = 1;
    g.gridy = y;
    form.add(txtTitulo, g);

    y++;
    g.gridx = 0;
    g.gridy = y;
    form.add(new JLabel("Idade:"), g);
    g.gridx = 1;
    g.gridy = y;
    form.add(txtId, g);

    y++;
    g.gridx = 0;
    g.gridy = y;
    form.add(new JLabel("Curso:"), g);
    g.gridx = 1;
    g.gridy = y;
    form.add(cbGenero, g);

    JPanel botoes = new JPanel();
    JButton btnNovo = new JButton("Novo");
    JButton btnSalvar = new JButton("Salvar");
    JButton btnAtualizar = new JButton("Atualizar");
    JButton btnExcluir = new JButton("Excluir");
    botoes.add(btnNovo);
    botoes.add(btnSalvar);
    botoes.add(btnAtualizar);
    botoes.add(btnExcluir);

    JPanel busca = new JPanel();
    JButton btnBuscar = new JButton("Buscar");
    busca.add(new JLabel("Nome:"));
    busca.add(txtBuscar);
    busca.add(btnBuscar);

    add(form, BorderLayout.NORTH);
    add(new JScrollPane(tabela), BorderLayout.CENTER);
    add(botoes, BorderLayout.SOUTH);
    add(busca, BorderLayout.WEST);

    // Ações
    btnNovo.addActionListener(e -> limpar());
    btnSalvar.addActionListener(e -> salvar());
    btnAtualizar.addActionListener(e -> atualizar());
    btnExcluir.addActionListener(e -> excluir());
    btnBuscar.addActionListener(e -> buscar());

    carregarTabela();
  }

  private void limpar() {
    txtId.setText("");
    txtTitulo.setText("");
    txtNota.setText("");
    txtAno.setText("");
    cbGenero.setSelectedIndex(0);
    cbStatus.setSelectedIndex(0);
    cbPlataforma.setSelectedIndex(0);
    txtTitulo.requestFocus();
  }

  private Jogo buildFromForm() throws SQLException {
    String titulo = txtTitulo.getText();
    String plataforma = (String) cbPlataforma.getSelectedItem();
    String status = (String) cbStatus.getSelectedItem();
    String genero = (String) cbGenero.getSelectedItem();
    int nota = Integer.parseInt(txtNota.getText());
    int ano_lancamento = Integer.parseInt(txtAno.getText());
    Jogo j = new Jogo(titulo, genero, plataforma, status, nota, ano_lancamento);
    try {
      service.validarJogo(j);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return j;
  }

  private void salvar() {
    try {
      Jogo j = buildFromForm();
      dao.criar(j);
      JOptionPane.showMessageDialog(this,
          "Jogo salvo! Titulo: " + j.getTitulo() + " | Plataforma: " + j.getPlataforma() +
              " | Genero: " + j.getGenero());
      carregarTabela();
      limpar();
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void atualizar() {
    try {
      if (txtId.getText().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Selecione um registro da tabela.");
        return;
      }
      Jogo j = buildFromForm();
      j.setId(Integer.parseInt(txtId.getText()));
      dao.atualizar(j);
      carregarTabela();
      limpar();
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void excluir() {
    try {
      if (txtId.getText().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Selecione um registro da tabela.");
        return;
      }
      int id = Integer.parseInt(txtId.getText());
      dao.deletar(id);
      carregarTabela();
      limpar();
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void buscar() {
    try {
      List<Jogo> lista = dao.buscarPorNomeLike(txtBuscar.getText());
      preencherTabela(lista);
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(this, "Erro ao buscar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void carregarTabela() {
    try {
      DBInit.ensureCreated();
      List<Jogo> lista = dao.listarTodos();
      preencherTabela(lista);
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(this, "Erro ao carregar tabela: " + ex.getMessage(), "Erro",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void preencherTabela(List<Jogo> lista) {
    model.setRowCount(0);
    for (Jogo j : lista) {
      model.addRow(new Object[] { j.getId(), j.getTitulo(), j.getGenero(), j.getPlataforma(), j.getStatus(),
          j.getNota(), j.getAno_lancamento() });
    }
    // clique para editar
    tabela.getSelectionModel().addListSelectionListener(e -> {
      int i = tabela.getSelectedRow();
      if (i >= 0) {
        txtId.setText(model.getValueAt(i, 0).toString());
        txtTitulo.setText(model.getValueAt(i, 1).toString());
        cbGenero.setSelectedItem(model.getValueAt(i, 2).toString());
        cbPlataforma.setSelectedItem(model.getValueAt(i, 3).toString());
        cbStatus.setSelectedItem(model.getValueAt(i, 4).toString());
        txtNota.setText(model.getValueAt(i, 5).toString());
        txtAno.setText(model.getValueAt(i, 6).toString());
      }
    });
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      try {
        DBInit.ensureCreated();
      } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Falha ao inicializar banco: " + e.getMessage());
      }
      new JogoView().setVisible(true);
    });
  }
}
