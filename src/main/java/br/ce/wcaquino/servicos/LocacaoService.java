package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;
import exceptions.FilmeSemEstoqueException;
import exceptions.LocadoraException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class LocacaoService {

  public Locacao alugarFilme(Usuario usuario, List<Filme> filmes)
      throws FilmeSemEstoqueException, LocadoraException {

    if (usuario == null) {
      throw new LocadoraException("Usuário vazio");
    }

    if (filmes.isEmpty() || filmes == null) {
      throw new LocadoraException("Lista de filmes vazia");
    }

    for (Filme filme : filmes) {
      if (filme.getEstoque() == 0) {
        throw new FilmeSemEstoqueException();
      }
    }

    Locacao locacao = new Locacao();
    locacao.setFilmes(filmes);
    locacao.setUsuario(usuario);
    locacao.setDataLocacao(new Date());
    locacao.setValor(filmes);

    //Entrega no dia seguinte
    Date dataEntrega = new Date();
    dataEntrega = adicionarDias(dataEntrega, 1);
    locacao.setDataRetorno(dataEntrega);

    //Salvando a locacao...
    //TODO adicionar método para salvar

    return locacao;
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
}