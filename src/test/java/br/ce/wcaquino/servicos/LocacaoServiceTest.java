package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.Date;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.*;

public class LocacaoServiceTest {
    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void test() throws Exception {
        //cenario
        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario("Usuario1");
        Filme filme = new Filme("filme1", 2, 5.0);

        // acao
        Locacao locacao = service.alugarFilme(usuario, filme);

        // verificacao expected = ao valor esperado, actual = valor que será gerado pelos médtodos criados
        Assert.assertEquals(5.0, locacao.getValor(), 0.01);
        Assert.assertTrue(isMesmaData(locacao.getDataLocacao(), new Date()));
        Assert.assertTrue(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)));

        Assert.assertThat(locacao.getValor(), is(equalTo(5.0))); //verifique que o valor da locacao é 5.0
        Assert.assertThat(locacao.getValor(), is(not(6.0))); //verifique que o valor da locacao não é 6.0
        Assert.assertThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));


        //verificacao utilizando o ErrorCollector
        error.checkThat(locacao.getValor(), is(equalTo(6.0)));
        error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(false));
    }

    @Test
    public void testeUtilizandoTratamentoDeExcecoes() throws Exception {
        //cenario
        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario("Usuario1");
        Filme filme = new Filme("filme1", 2, 5.0);

        // acao
        Locacao locacao = service.alugarFilme(usuario, filme);

        //verificacao utilizando o ErrorCollector
        error.checkThat(locacao.getValor(), is(equalTo(6.0)));
        error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(false));

    }

    //Forma elegante
    @Test(expected = Exception.class)
    public void testeLocacao_filmeSemEstoque() throws Exception {
        //cenario
        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario("Usuario1");
        Filme filme = new Filme("filme1", 0, 5.0);

        // acao
        Locacao locacao = service.alugarFilme(usuario, filme);
    }

    //Forma nova
    @Test
    public void testeLocacao_filmeSemEstoqueFormaNova() throws Exception {
        //cenario
        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario("Usuario1");
        Filme filme = new Filme("filme1", 0, 5.0);

        //Dessa forma é necessário que seja informada nesse ponto do código
        exception.expect(Exception.class);
        exception.expectMessage("Filme sem estoque");

        // acao
        service.alugarFilme(usuario, filme);

    }
}