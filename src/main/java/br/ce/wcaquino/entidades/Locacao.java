package br.ce.wcaquino.entidades;

import java.util.Date;
import java.util.List;

public class Locacao {

  private Usuario usuario;
  private List<Filme> filmes;
  private Date dataLocacao;
  private Date dataRetorno;
  private Double valor = 0.0;

  public Locacao(
      Usuario usuario,
      List<Filme> filmes,
      Date dataLocacao,
      Date dataRetorno,
      Double valor
  ) {
    this.usuario = usuario;
    this.filmes = filmes;
    this.dataLocacao = dataLocacao;
    this.dataRetorno = dataRetorno;
    this.valor = valor;
  }

  public Locacao() {
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }

  public Date getDataLocacao() {
    return dataLocacao;
  }

  public void setDataLocacao(Date dataLocacao) {
    this.dataLocacao = dataLocacao;
  }

  public Date getDataRetorno() {
    return dataRetorno;
  }

  public void setDataRetorno(Date dataRetorno) {
    this.dataRetorno = dataRetorno;
  }

  public Double getValor() {
    return valor;
  }

  public void setValor(List<Filme> filmes) {
    for (Filme filme : filmes) {
      this.valor += filme.getPrecoLocacao();
    }
  }

  public List<Filme> getFilmes() {
    return filmes;
  }

  public void setFilmes(List<Filme> filmes) {
    this.filmes = filmes;
  }
}