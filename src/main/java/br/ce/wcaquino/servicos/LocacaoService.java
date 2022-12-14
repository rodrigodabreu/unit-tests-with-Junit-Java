package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import br.ce.wcaquino.daos.LocacaoDao;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;
import exceptions.FilmeSemEstoqueException;
import exceptions.LocadoraException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class LocacaoService {

  private LocacaoDao dao;
  private SPCService spcService;
  private EmailService emailService;

  public Locacao alugarFilme(Usuario usuario, List<Filme> filmes)
      throws FilmeSemEstoqueException, LocadoraException {

    if (usuario == null) {
      throw new LocadoraException("Usuário vazio");
    }

    if (filmes.isEmpty()) {
      throw new LocadoraException("Lista de filmes vazia");
    }

    for (Filme filme : filmes) {
      if (filme.getEstoque() == 0) {
        throw new FilmeSemEstoqueException();
      }
    }

    if (spcService.possuiNegativacao(usuario)) {
      throw new LocadoraException("Usuário negativado");
    }

    Locacao locacao = new Locacao();
    locacao.setFilmes(filmes);
    locacao.setUsuario(usuario);
    locacao.setDataLocacao(new Date());
    locacao.setValor(calcularValorTotalLocacao(filmes));

    //Entrega no dia seguinte
    Date dataEntrega = new Date();
    dataEntrega = adicionarDias(dataEntrega, 1);
    if (DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)) {
      dataEntrega = adicionarDias(dataEntrega, 1);
    }
    locacao.setDataRetorno(dataEntrega);

    //Salvando a locacao...
    //TODO adicionar método para salvar
    dao.salvar(locacao);

    return locacao;
  }

  public void setLocacaoDAO(LocacaoDao dao) {
    this.dao = dao;
  }

  public void notificarAtrasos() {
    List<Locacao> locacoes = dao.obterLocacoesPendentes();
    for (Locacao lococao : locacoes) {
      if (lococao.getDataRetorno().before(
          new Date())) { //envia apenas para aquelas que tiver a data retorno depois da data atual
        emailService.notificarAtraso(lococao.getUsuario());
      }
    }
  }

  private Double calcularValorTotalLocacao(List<Filme> filmes) {
    Double valorTotalLocacao = 0d;
    for (int i = 0; i < filmes.size(); i++) {
      Filme filme = filmes.get(i);
      Double valorFilme = filme.getPrecoLocacao();
      //i = 2 por conta de se tratar de um array que inicializa na posição 0 (ZERO)
      switch (i) {
        case (2):
          valorFilme = valorFilme * 0.75;
          break;
        case (3):
          valorFilme = valorFilme * 0.5;
          break;
        case (4):
          valorFilme = valorFilme * 0.25;
          break;

        case (5):
          valorFilme = valorFilme * 0.0;
          break;
      }
      valorTotalLocacao += valorFilme;
    }
    return valorTotalLocacao;
  }

  @Test
  public void test() throws Exception {
    //cenario
    LocacaoService service = new LocacaoService();
    Usuario usuario = new Usuario("Usuario1");
    List<Filme> filmes = Arrays.asList();
    Filme filme = new Filme("filme1", 2, 5.0);
    filmes.add(filme);

    // acao
    Locacao locacao = service.alugarFilme(usuario, filmes);

    // verificacao
    Assert.assertFalse(locacao.getValor() == 4.0);
    Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
    Assert.assertTrue(
        DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1))
    );
  }

  public void setSpcService(SPCService spcService) {
    this.spcService = spcService;
  }

  public void setEmailService(EmailService emailService) {
    this.emailService = emailService;
  }
}