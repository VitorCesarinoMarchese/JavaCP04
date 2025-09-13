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
      new Object[] { "ID", "Título", "Gênero", "Plataforma", "Status", "Nota", "Ano lançamento" }, 0);
  private JTable tabela = new JTable(model);

  private JogoDAO dao = new JogoDAO();
  private JogoService service = new JogoService();

  public JogoView() {
    super("Biblioteca de jogos");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(1000, 700);
    setLocationRelativeTo(null);

    JPanel form = new JPanel(new GridBagLayout());
    GridBagConstraints g = new GridBagConstraints();
    g.insets = new Insets(5, 5, 5, 5);
    g.fill = GridBagConstraints.HORIZONTAL;

    int y = 0;

    // Row 1: ID + Titulo
    g.gridx = 0;
    g.gridy = y;
    form.add(new JLabel("ID:"), g);
    g.gridx = 1;
    txtId.setEditable(false);
    form.add(txtId, g);

    g.gridx = 2;
    form.add(new JLabel("Título:"), g);
    g.gridx = 3;
    form.add(txtTitulo, g);

    y++;

    // Row 2: Genero + Plataforma
    g.gridx = 0;
    g.gridy = y;
    form.add(new JLabel("Gênero:"), g);
    g.gridx = 1;
    form.add(cbGenero, g);

    g.gridx = 2;
    form.add(new JLabel("Plataforma:"), g);
    g.gridx = 3;
    form.add(cbPlataforma, g);

    y++;

    // Row 3: Status + Nota
    g.gridx = 0;
    g.gridy = y;
    form.add(new JLabel("Status:"), g);
    g.gridx = 1;
    form.add(cbStatus, g);

    g.gridx = 2;
    form.add(new JLabel("Nota:"), g);
    g.gridx = 3;
    form.add(txtNota, g);

    y++;

    // Row 4: Ano lançamento
    g.gridx = 0;
    g.gridy = y;
    form.add(new JLabel("Ano lançamento:"), g);
    g.gridx = 1;
    form.add(txtAno, g);

    JPanel botoes = new JPanel();
    JButton btnLimpar = new JButton("Limpar");
    JButton btnNovo = new JButton("Novo");
    JButton btnAtualizar = new JButton("Atualizar");
    JButton btnExcluir = new JButton("Excluir");
    botoes.add(btnLimpar);
    botoes.add(btnNovo);
    botoes.add(btnAtualizar);
    botoes.add(btnExcluir);

    JPanel busca = new JPanel();
    JButton btnBuscar = new JButton("Buscar");
    busca.add(new JLabel("Titulo:"));
    busca.add(txtBuscar);
    busca.add(btnBuscar);

    add(form, BorderLayout.NORTH);
    add(new JScrollPane(tabela), BorderLayout.CENTER);
    add(botoes, BorderLayout.SOUTH);
    add(busca, BorderLayout.WEST);

    // Ações
    btnLimpar.addActionListener(e -> limpar());
    btnNovo.addActionListener(e -> salvar());
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
    service.validarJogo(j);
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
      List<Jogo> lista = dao.buscarPorTituloLike(txtBuscar.getText());
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
