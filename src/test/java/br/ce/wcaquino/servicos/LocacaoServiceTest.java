package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;
import exceptions.FilmeSemEstoqueException;
import exceptions.LocadoraException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

public class LocacaoServiceTest {

  //definição do contador
  private static int contador = 0;
  @Rule
  public ErrorCollector error = new ErrorCollector();
  @Rule
  public ExpectedException exception = ExpectedException.none();
  private LocacaoService service;

  @BeforeClass
  public static void setupClass() {
    System.out.println("before class");
  }

  @AfterClass
  public static void tearDownClass() {
    System.out.println("after class");
  }

  @Before
  public void setup() {
    System.out.println("before");
    service = new LocacaoService();
    contador++;
    System.out.println(contador);
  }

  @After
  public void tearDown() {
    System.out.println("after");
  }

  @Test
  public void test() throws Exception {
    //cenario
    Usuario usuario = new Usuario("Usuario1");
    List<Filme> filmes = Arrays.asList(new Filme("filme1", 2, 5.0));

    // acao
    Locacao locacao = service.alugarFilme(usuario, filmes);

    // verificacao expected = ao valor esperado, actual = valor que será gerado pelos médtodos criados
    Assert.assertEquals(5.0, locacao.getValor(), 0.01);
    Assert.assertTrue(isMesmaData(locacao.getDataLocacao(), new Date()));
    Assert.assertTrue(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)));

    Assert.assertThat(
        locacao.getValor(),
        is(equalTo(5.0))
    ); //verifique que o valor da locacao é 5.0
    Assert.assertThat(
        locacao.getValor(),
        is(not(6.0))
    ); //verifique que o valor da locacao não é 6.0
    Assert.assertThat(
        isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)),
        is(true)
    );

    //verificacao utilizando o ErrorCollector
    error.checkThat(locacao.getValor(), is(equalTo(5.0)));
    error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
    error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
  }

  @Test
  public void testeUtilizandoTratamentoDeExcecoes() throws Exception {
    //cenario
    Usuario usuario = new Usuario("Usuario1");
    List<Filme> filmes = Arrays.asList(new Filme("filme1", 2, 5.0));

    // acao
    Locacao locacao = service.alugarFilme(usuario, filmes);

    //verificacao utilizando o ErrorCollector
    error.checkThat(locacao.getValor(), is(equalTo(5.0)));
    error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
    error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));

  }

  //Forma elegante
  @Test(expected = FilmeSemEstoqueException.class)
  public void testeLocacao_filmeSemEstoque() throws Exception {
    //cenario
    Usuario usuario = new Usuario("Usuario1");
    List<Filme> filmes = Arrays.asList(new Filme("filme1", 0, 5.0));

    // acao
    Locacao locacao = service.alugarFilme(usuario, filmes);
  }

  //Forma nova
  @Test
  public void testeLocacao_filmeSemEstoqueFormaNova() throws Exception {
    //cenario
    Usuario usuario = new Usuario("Usuario1");
    List<Filme> filmes = Arrays.asList(new Filme("filme1", 0, 5.0));

    //Dessa forma é necessário que seja informada nesse ponto do código
    exception.expect(Exception.class);

    // acao
    service.alugarFilme(usuario, filmes);

  }

  //Forma robusta
  @Test
  public void testeLocacao_usuarioVazio() throws FilmeSemEstoqueException {
    //cenario
    List<Filme> filmes = Arrays.asList(new Filme("filme1", 0, 5.0));

    //acao
    try {
      service.alugarFilme(null, filmes);
      Assert.fail();
    } catch (LocadoraException e) {
      Assert.assertThat(e.getMessage(), is("Usuário vazio"));
    }

  }


  //Forma nova
  @Test
  public void testeLocacao_FilmeVazio() throws FilmeSemEstoqueException, LocadoraException {
    //cenario
    Usuario usuario = new Usuario("Usuario1");
    List<Filme> filmes = new ArrayList<>();

    exception.expect(LocadoraException.class);

    //acao
    service.alugarFilme(usuario, filmes);
  }

  @Test
  public void devePagar75PorcentoNoFilme3() throws FilmeSemEstoqueException, LocadoraException {
    //cenario
    Usuario usuario = new Usuario("Usuario1");
    List<Filme> filmes = new ArrayList<Filme>(Collections.<Filme>emptyList());

    filmes.add(new Filme("filme 1", 10, 1.0));
    filmes.add(new Filme("filme 2", 10, 1.0));
    filmes.add(new Filme("filme 3", 10, 1.0));

    Double expected = 1 + 1 + (1 * 0.75);

    //acao
    Locacao result = service.alugarFilme(usuario, filmes);

    //verificacao
    Assert.assertThat(result.getValor(), is(expected));
  }

  @Test
  public void devePagar50PorcentoNoFilme4() throws FilmeSemEstoqueException, LocadoraException {
    //cenario
    Usuario usuario = new Usuario("Usuario1");
    List<Filme> filmes = new ArrayList<Filme>(Collections.<Filme>emptyList());

    filmes.add(new Filme("filme 1", 10, 1.0));
    filmes.add(new Filme("filme 2", 10, 1.0));
    filmes.add(new Filme("filme 3", 10, 1.0));
    filmes.add(new Filme("filme 4", 10, 1.0));

    Double expected = 1 + 1 + (1 * 0.75) + (1 * 0.5);

    //acao
    Locacao result = service.alugarFilme(usuario, filmes);

    //verificacao
    Assert.assertThat(result.getValor(), is(expected));
  }

  @Test
  public void devePagar25PorcentoNoFilme5() throws FilmeSemEstoqueException, LocadoraException {
    //cenario
    Usuario usuario = new Usuario("Usuario1");
    List<Filme> filmes = new ArrayList<Filme>(Collections.<Filme>emptyList());

    filmes.add(new Filme("filme 1", 10, 1.0));
    filmes.add(new Filme("filme 2", 10, 1.0));
    filmes.add(new Filme("filme 3", 10, 1.0));
    filmes.add(new Filme("filme 4", 10, 1.0));
    filmes.add(new Filme("filme 5", 10, 1.0));

    Double expected = 1 + 1 + (1 * 0.75) + (1 * 0.5) + (1 * 0.25);

    //acao
    Locacao result = service.alugarFilme(usuario, filmes);

    //verificacao
    Assert.assertThat(result.getValor(), is(expected));
  }

  @Test
  public void devePagar100PorcentoNoFilme6() throws FilmeSemEstoqueException, LocadoraException {
    //cenario
    Usuario usuario = new Usuario("Usuario1");
    List<Filme> filmes = new ArrayList<Filme>(Collections.<Filme>emptyList());

    filmes.add(new Filme("filme 1", 10, 1.0));
    filmes.add(new Filme("filme 2", 10, 1.0));
    filmes.add(new Filme("filme 3", 10, 1.0));
    filmes.add(new Filme("filme 4", 10, 1.0));
    filmes.add(new Filme("filme 5", 10, 1.0));
    filmes.add(new Filme("filme 6", 10, 1.0));

    Double expected = 1 + 1 + (1 * 0.75) + (1 * 0.5) + (1 * 0.25) + (1 * 0);

    //acao
    Locacao result = service.alugarFilme(usuario, filmes);

    //verificacao
    Assert.assertThat(result.getValor(), is(expected));
  }


  @Ignore
  @Test
  public void deveDevolverFilmeNaSegundaAoAlugarNoSabado()
      throws FilmeSemEstoqueException, LocadoraException {
    //cenario
    Usuario usuario = new Usuario("Usuario1");
    List<Filme> filmes = new ArrayList<Filme>(Collections.<Filme>emptyList());
    filmes.add(new Filme("filme 1", 10, 1.0));

    //acao
    Locacao result = service.alugarFilme(usuario, filmes);

    //verificacao
    boolean ehSegunda = DataUtils.verificarDiaSemana(result.getDataRetorno(), Calendar.MONDAY);
    Assert.assertEquals(true, ehSegunda);
  }


  @Test
  public void deveDevolverFilmeNaSegundaAoAlugarNoSabadoUtilizandoOAssumptions()
      throws FilmeSemEstoqueException, LocadoraException {
    Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

    //cenario
    Usuario usuario = new Usuario("Usuario1");
    List<Filme> filmes = new ArrayList<Filme>(Collections.<Filme>emptyList());
    filmes.add(new Filme("filme 1", 10, 1.0));

    //acao
    Locacao result = service.alugarFilme(usuario, filmes);

    //verificacao
    boolean ehSegunda = DataUtils.verificarDiaSemana(result.getDataRetorno(), Calendar.MONDAY);
    Assert.assertEquals(true, ehSegunda);
  }
}