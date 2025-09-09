package br.fiap.biblioteca.model;

import java.util.Objects;

public class Jogo {
  private Integer id;
  private String titulo;
  private String genero;
  private String plataforma;
  private String status;
  private int nota;
  private int ano_lancamento;

  public Jogo() {
  }

  public Jogo(Integer id, String titulo, String genero, String plataforma, String status, int nota,
      int ano_lancamento) {
    this.id = id;
    this.titulo = titulo;
    this.genero = genero;
    this.plataforma = plataforma;
    this.status = status;
    this.nota = nota;
    this.ano_lancamento = ano_lancamento;
  }

  public Jogo(String titulo, String genero, String plataforma, String status, int nota, int ano_lancamento) {
    this(null, titulo, genero, plataforma, status, nota, ano_lancamento);
  }

  @Override
  public String toString() {
    return "Jogo{id=" + id + ", titulo='" + titulo + '\'' +
        ", genero=" + genero + ", plataforma='" + plataforma + '\'' +
        ", status=" + status + ", nota='" + nota + '\'' +
        ", ano_lancamento=" + ano_lancamento +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof Jogo))
      return false;
    Jogo jogo = (Jogo) o;
    return Objects.equals(id, jogo.id);
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public void setGenero(String genero) {
    this.genero = genero;
  }

  public void setPlataforma(String plataforma) {
    this.plataforma = plataforma;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setAno_lancamento(int ano_lancamento) {
    this.ano_lancamento = ano_lancamento;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  public Integer getId() {
    return id;
  }

  public String getTitulo() {
    return titulo;
  }

  public String getGenero() {
    return genero;
  }

  public String getPlataforma() {
    return plataforma;
  }

  public String getStatus() {
    return status;
  }

  public int getAno_lancamento() {
    return ano_lancamento;
  }

  public int getNota() {
    return nota;
  }

  public void setNota(int nota) {
    this.nota = nota;
  }

}
