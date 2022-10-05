package br.ce.wcaquino.servicos;

import static org.junit.Assert.assertEquals;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//Dessa forma estamos informando ao JUnit para rodar os cenários de acordo com a ordem alfabética.
public class OrdemTest {

    private static int contador = 0;

    /*
    Uma maneira de forçar que a ordem de execução seja a que desejamos é criar um método de teste geral
     */

    public void inicia() {
        contador = 1;
    }

    public void verifica() {
        assertEquals(1, contador);
    }

    @Test
    public void testGeral() {
        inicia();
        verifica();
    }
}
