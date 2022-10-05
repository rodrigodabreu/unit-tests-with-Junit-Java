package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import java.util.Date;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;
import org.junit.Assert;
import org.junit.Test;

public class LocacaoService {

    public Locacao alugarFilme(Usuario usuario, Filme filme) {
        Locacao locacao = new Locacao();
        locacao.setFilme(filme);
        locacao.setUsuario(usuario);
        locacao.setDataLocacao(new Date());
        locacao.setValor(filme.getPrecoLocacao());

        //Entrega no dia seguinte
        Date dataEntrega = new Date();
        dataEntrega = adicionarDias(dataEntrega, 1);
        locacao.setDataRetorno(dataEntrega);

        //Salvando a locacao...
        //TODO adicionar método para salvar

        return locacao;
    }

    public static void main(String[] args) {
        //cenario
        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario("Usuario1");
        Filme filme = new Filme("filme1", 2, 5.0);

        // acao
        Locacao locacao = service.alugarFilme(usuario, filme);

        // verificacao
        System.out.println(locacao.getValor());
        System.out.println(locacao.getDataLocacao());
        System.out.println(locacao.getDataRetorno());
    }
    @Test
    public void test() {
        //cenario
        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario("Usuario1");
        Filme filme = new Filme("filme1", 2, 5.0);

        // acao
        Locacao locacao = service.alugarFilme(usuario, filme);

        // verificacao
        Assert.assertFalse(locacao.getValor() == 4.0);
        Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(),new Date()));
        Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(),DataUtils.obterDataComDiferencaDias(1)));
    }
}