package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static br.ce.wcaquino.utils.DataUtils.verificarDiaSemana;
import static builder.LocacaoBuilder.umaLocacao;
import static matchers.MatcherProprios.ehHoje;
import static matchers.MatcherProprios.ehHojeComDiferencaDeDia;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import br.ce.wcaquino.daos.LocacaoDao;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import builder.FilmeBuilder;
import builder.UsuarioBuilder;
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
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(LocacaoService.class)
public class LocacaoServicePowerMockTest {

  private static final Double VALOR_ESPERADO = 4.0;
  //definição do contador
  private static int contador = 0;
  @Rule
  public ErrorCollector error = new ErrorCollector();
  @Rule
  public ExpectedException exception = ExpectedException.none();
  private LocacaoService service;

  private LocacaoDao dao;

  private SPCService spcService;

  private EmailService emailService;

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
    dao = mock(LocacaoDao.class);
//    dao = new LocacaoDaoFake();
    service.setLocacaoDAO(dao);
    spcService = mock(SPCService.class);
    service.setSpcService(spcService);
    emailService = Mockito.mock(EmailService.class);
    service.setEmailService(emailService);
  }

  @After
  public void tearDown() {
    System.out.println("after");
  }

  @Test
  public void test() throws Exception {
    //cenario
    Usuario usuario = UsuarioBuilder.umUsuario().agora();
    List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

    // acao
    Locacao locacao = service.alugarFilme(usuario, filmes);

    // verificacao expected = ao valor esperado, actual = valor que será gerado pelos médtodos criados
    Assert.assertEquals(VALOR_ESPERADO, locacao.getValor(), 0.01);
    Assert.assertTrue(isMesmaData(locacao.getDataLocacao(), new Date()));
    Assert.assertTrue(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)));

    Assert.assertThat(
        locacao.getValor(),
        is(equalTo(VALOR_ESPERADO))
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
    error.checkThat(locacao.getValor(), is(equalTo(VALOR_ESPERADO)));
    error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
    error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
    error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDeDia(1));
    error.checkThat(locacao.getDataRetorno(), ehHoje());
  }

  @Test
  public void testeUtilizandoTratamentoDeExcecoes() throws Exception {
    //cenario
    Usuario usuario = new Usuario("Usuario1");
    List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

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
    List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().semEstoque().agora());

    // acao
    Locacao locacao = service.alugarFilme(usuario, filmes);
  }

  //Forma nova
  @Test
  public void testeLocacao_filmeSemEstoqueFormaNova() throws Exception {
    //cenario
    Usuario usuario = new Usuario("Usuario1");
    List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().semEstoque().agora());

    //Dessa forma é necessário que seja informada nesse ponto do código
    exception.expect(Exception.class);

    // acao
    service.alugarFilme(usuario, filmes);

  }

  //Forma robusta
  @Test
  public void testeLocacao_usuarioVazio() throws FilmeSemEstoqueException {
    //cenario
    List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

    //acao
    try {
      service.alugarFilme(null, filmes);
      Assert.fail();
    } catch (LocadoraException e) {
      Assert.assertThat(e.getMessage(), is("Usuário vazio"));
    }

  }

  @Test
  public void deveAlugarFilme() throws FilmeSemEstoqueException, LocadoraException {
    Assume.assumeFalse(verificarDiaSemana(new Date(), Calendar.SATURDAY));

    //cenario
    Usuario usuario = UsuarioBuilder.umUsuario().agora();
    List<Filme> filmes = Arrays.asList((FilmeBuilder.umFilme().comValor(5.0).agora()));

    //acao
    Locacao locacao = service.alugarFilme(usuario, filmes);

    //verificacao
    error.checkThat(locacao.getValor(), is(equalTo(5.0)));
    error.checkThat(locacao.getDataLocacao(), ehHoje());
    error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDeDia(1));
  }


  //Forma nova
  @Test
  public void testeLocacao_FilmeVazio() throws FilmeSemEstoqueException, LocadoraException {
    //cenario
    Usuario usuario = new Usuario("Usuario1");
    List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().semEstoque().agora());

    exception.expect(LocadoraException.class);

    //acao
    service.alugarFilme(usuario, filmes);
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
    boolean ehSegunda = verificarDiaSemana(result.getDataRetorno(), Calendar.MONDAY);
    Assert.assertEquals(false, ehSegunda);
  }


  @Test
  public void deveDevolverFilmeNaSegundaAoAlugarNoSabadoUtilizandoOAssumptions()
      throws FilmeSemEstoqueException, LocadoraException {
//    Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

    //cenario
    Usuario usuario = new Usuario("Usuario1");
    List<Filme> filmes = new ArrayList<Filme>(Collections.<Filme>emptyList());
    filmes.add(FilmeBuilder.umFilme().agora());

    //acao
    Locacao result = service.alugarFilme(usuario, filmes);

    //verificacao
    boolean ehSegunda = verificarDiaSemana(result.getDataRetorno(), Calendar.MONDAY);
    Assert.assertEquals(false, ehSegunda);

    //3 outras formas de criar uma verificação com Matchers Próprios
//    assertThat(result.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
//    assertThat(result.getDataRetorno(), caiEm(Calendar.MONDAY));
//    assertThat(result.getDataRetorno(), caiNumaSegunda(Calendar.MONDAY));
  }

  // Testes desnecessários tendo em vista que os mesmos foram replicados de forma parametrizada na classe CalculoValorLocacaoTest
//  @Test
//  public void devePagar75PorcentoNoFilme3() throws FilmeSemEstoqueException, LocadoraException {
//    //cenario
//    Usuario usuario = new Usuario("Usuario1");
//    List<Filme> filmes = new ArrayList<Filme>(Collections.<Filme>emptyList());
//
//    filmes.add(new Filme("filme 1", 10, 1.0));
//    filmes.add(new Filme("filme 2", 10, 1.0));
//    filmes.add(new Filme("filme 3", 10, 1.0));
//
//    //esperado = 2.75
//    Double expected = 1 + 1 + (1 * 0.75);
//
//    //acao
//    Locacao result = service.alugarFilme(usuario, filmes);
//
//    //verificacao
//    Assert.assertThat(result.getValor(), is(expected));
//  }
//
//  @Test
//  public void devePagar50PorcentoNoFilme4() throws FilmeSemEstoqueException, LocadoraException {
//    //cenario
//    Usuario usuario = new Usuario("Usuario1");
//    List<Filme> filmes = new ArrayList<Filme>(Collections.<Filme>emptyList());
//
//    filmes.add(new Filme("filme 1", 10, 1.0));
//    filmes.add(new Filme("filme 2", 10, 1.0));
//    filmes.add(new Filme("filme 3", 10, 1.0));
//    filmes.add(new Filme("filme 4", 10, 1.0));
//
//    //esperado = 3.25
//    Double expected = 1 + 1 + (1 * 0.75) + (1 * 0.5);
//
//    //acao
//    Locacao result = service.alugarFilme(usuario, filmes);
//
//    //verificacao
//    Assert.assertThat(result.getValor(), is(expected));
//  }
//
//  @Test
//  public void devePagar25PorcentoNoFilme5() throws FilmeSemEstoqueException, LocadoraException {
//    //cenario
//    Usuario usuario = new Usuario("Usuario1");
//    List<Filme> filmes = new ArrayList<Filme>(Collections.<Filme>emptyList());
//
//    filmes.add(new Filme("filme 1", 10, 1.0));
//    filmes.add(new Filme("filme 2", 10, 1.0));
//    filmes.add(new Filme("filme 3", 10, 1.0));
//    filmes.add(new Filme("filme 4", 10, 1.0));
//    filmes.add(new Filme("filme 5", 10, 1.0));
//
//    //esperado = 3,25
//    Double expected = 1 + 1 + (1 * 0.75) + (1 * 0.5) + (1 * 0.25);
//
//    //acao
//    Locacao result = service.alugarFilme(usuario, filmes);
//
//    //verificacao
//    Assert.assertThat(result.getValor(), is(expected));
//  }
//
//  @Test
//  public void devePagar100PorcentoNoFilme6() throws FilmeSemEstoqueException, LocadoraException {
//    //cenario
//    Usuario usuario = new Usuario("Usuario1");
//    List<Filme> filmes = new ArrayList<Filme>(Collections.<Filme>emptyList());
//
//    filmes.add(new Filme("filme 1", 10, 1.0));
//    filmes.add(new Filme("filme 2", 10, 1.0));
//    filmes.add(new Filme("filme 3", 10, 1.0));
//    filmes.add(new Filme("filme 4", 10, 1.0));
//    filmes.add(new Filme("filme 5", 10, 1.0));
//    filmes.add(new Filme("filme 6", 10, 1.0));
//
//    //esperado = 3,50
//    Double expected = 1 + 1 + (1 * 0.75) + (1 * 0.5) + (1 * 0.25) + (1 * 0);
//
//    //acao
//    Locacao result = service.alugarFilme(usuario, filmes);
//
//    //verificacao
//    Assert.assertThat(result.getValor(), is(expected));
//  }

  @Test
  public void naoDeveAlugarFilmePAraNagativadoSPC()
      throws FilmeSemEstoqueException, LocadoraException {
    //cenario
    Usuario usuario = UsuarioBuilder.umUsuario().agora();
    List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

    //Definindo o comportamento esperado da classe com o Mockito
    when(spcService.possuiNegativacao(usuario)).thenReturn(Boolean.TRUE);

    exception.expect(LocadoraException.class);
    exception.expectMessage("Usuário negativado");

    //acao
    service.alugarFilme(usuario, filmes);
  }

  //Nesse cenário aqui o mockito não saberá o comportamento que deve retornar para o usuário 2
  //com isso vai retornar false e não irá lançar a exceção esperada.
  @Test
  public void naoDeveAlugarFilmePAraNagativadoSPC_OutroUsuario()
      throws FilmeSemEstoqueException, LocadoraException {
    //cenario
    Usuario usuario = UsuarioBuilder.umUsuario().agora();
    Usuario usuario2 = UsuarioBuilder.umUsuario().comNome("Usuário 2").agora();
    List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

    //Definindo o comportamento esperado da classe com o Mockito
    //when(spcService.possuiNegativacao(usuario)).thenReturn(Boolean.TRUE);
    when(spcService.possuiNegativacao(Mockito.any(Usuario.class))) //Utilizando Matchers ao invés de ter que passar como parâmetro uma instância do que é requerido
        .thenReturn(Boolean.TRUE);

    /*
    OBS: Caso um método que está sendo verificado utilizar matcher em um dos parâmetros, os demais parâmetros tb devemos utilizar o matchers.
     */

    exception.expect(LocadoraException.class);
    exception.expectMessage("Usuário negativado");

    //acao
    service.alugarFilme(usuario2, filmes);

    //verificacao
    Mockito.verify(spcService).possuiNegativacao(usuario);
  }

  @Test
  public void deveEnviarEmailParaLocacoesAtrasadas() {
    //cenario
    Usuario usuario1 = UsuarioBuilder.umUsuario().agora();
    Usuario usuarioEmDia = UsuarioBuilder.umUsuario().comNome("Usuario em dia").agora();

    List<Locacao> locacoesPendentes = Arrays.asList(
        umaLocacao().atrasada().comUsuario(usuario1).agora(),
        umaLocacao().comUsuario(usuarioEmDia).agora()
    );

    Mockito.when(dao.obterLocacoesPendentes()).thenReturn(locacoesPendentes);

    //acao
    service.notificarAtrasos();

    //verificacao
    Mockito.verify(emailService).notificarAtraso(usuario1);
    Mockito.verify(emailService, never()).notificarAtraso(
        usuarioEmDia); //dessa forma conseguimos verificar que um usuário em dia não será notificado
    verifyNoMoreInteractions(
        emailService); //Outra forma de realizar uma verificação que não ocorre iteração com o método
    verifyZeroInteractions(spcService); //Mais uma nova forma de verificar que não houve iterações

    //outras formas de verificaçãoes existentes
    verify(emailService, Mockito.times(1)).notificarAtraso(usuario1);
    verify(emailService, Mockito.atLeast(1)).notificarAtraso(usuario1);
    verify(emailService, Mockito.atLeastOnce()).notificarAtraso(usuario1);

    //verificação genérica, não importando para quem foi enviado, mas apenas a quantidade - Utilizando o Matcher.Any()
    verify(emailService, Mockito.times(1)).notificarAtraso(Mockito.any(Usuario.class));


    /*
    Verificações demais num cenário de teste não são de fato relevantes e precisamos verificar o que realmente faz sentido para o cenário de testes em questão
     */
  }
}