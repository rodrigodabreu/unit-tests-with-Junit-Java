package br.ce.wcaquino.servicos;

import static org.hamcrest.CoreMatchers.is;

import br.ce.wcaquino.daos.LocacaoDao;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import exceptions.FilmeSemEstoqueException;
import exceptions.LocadoraException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mockito;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {

  private static Filme filme1 = new Filme("filme 1", 10, 1.0);
  private static Filme filme2 = new Filme("filme 2", 10, 1.0);
  private static Filme filme3 = new Filme("filme 3", 10, 1.0);
  private static Filme filme4 = new Filme("filme 4", 10, 1.0);
  private static Filme filme5 = new Filme("filme 5", 10, 1.0);
  private static Filme filme6 = new Filme("filme 6", 10, 1.0);
  private static Filme filme7 = new Filme("filme 6", 10, 1.0);
  @Parameter
  public List<Filme> filmes;
  @Parameter(value = 1)
  public Double valorLocacao;
  @Parameter(value = 2)
  public String descricaoCenario;
  private LocacaoService service;
  private SPCService spcService;
  private LocacaoDao dao;

  //sinaliza ao JUnit que esse método será a fonte de dados
  //esse método deve ser estático
  @Parameters(name = "{2}")
  public static Collection<Object[]> getParametros()
      throws FilmeSemEstoqueException, LocadoraException {

    //Esse método será a fonte de dados
    return Arrays.asList(new Object[][]{
        {Arrays.asList(filme1, filme2), 2.0, "2 filmes: sem desconto"},
        {Arrays.asList(filme1, filme2, filme3), 2.75, "3 filmes: 25%"},
        {Arrays.asList(filme1, filme2, filme3, filme4), 3.25, "4 filmes: 50%"},
        {Arrays.asList(filme1, filme2, filme3, filme4, filme5), 3.5, "5 filmes: 75%"},
        {Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6), 3.5, "6 filmes: 100%"},
        {Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6, filme7), 4.5,
            "7 filmes: sem desconto no%"}
    });
  }

  @Before
  public void setup() {
    service = new LocacaoService();
    dao = Mockito.mock(LocacaoDao.class);
    spcService = Mockito.mock(SPCService.class);
    service.setLocacaoDAO(dao);
    service.setSpcService(spcService);
  }

  @Test
  public void deveCalcularValorLocacaoConsiderandoDescontos()
      throws FilmeSemEstoqueException, LocadoraException {
    //cenario
    Usuario usuario = new Usuario("Usuario1");

    //acao
    Locacao result = service.alugarFilme(usuario, filmes);

    //verificacao
    Assert.assertThat(result.getValor(), is(valorLocacao));
  }

  @Test
  public void print() {
    System.out.println(valorLocacao);
  }
}