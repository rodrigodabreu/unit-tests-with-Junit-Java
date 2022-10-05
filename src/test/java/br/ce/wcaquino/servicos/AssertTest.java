package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Usuario;
import org.junit.Assert;
import org.junit.Test;
import sun.java2d.pipe.AAShapePipe;

public class AssertTest {

    @Test
    public void test() {
        Assert.assertTrue(true);
        Assert.assertFalse(false);

        Assert.assertEquals(1, 1);
        Assert.assertEquals(0.51, 0.51, 0.01); //delta = margem de erro de comparação
        Assert.assertEquals(Math.PI, 3.14, 0.01);

        int i = 5;
        Integer i2 = 5;

        //Assert.assertEquals(i, i2); //o Java não deixa executar o teste entre tipo diferentes (Não faz o unboxing do wrapper)
        //A forma que pode ser realizado é da seguinte forma
        Assert.assertEquals(Integer.valueOf(i), i2);
        //ou
        Assert.assertEquals(i, i2.intValue());

        Assert.assertEquals("bola", "bola");
        Assert.assertTrue("bola".equalsIgnoreCase("Bola"));

        Usuario u1 = new Usuario("Usuario 1");
        Usuario u2 = new Usuario("Usuario 1");
        Usuario u3 = null;

        Assert.assertEquals(u1, u2);
        //Realizando essa comparação temos que não se tratam da mesma instância de Usuário (pois não temos o método equals implementado), logo teremos resultado falso.
        //Após temos implementado o método equals na classe, essa condicã́o será atendida.

        //Sendo necessário validar se o objeto é da mesma instancia, devemos utilizar o assertSame
        Assert.assertSame(u1, u2); //false
        Assert.assertSame(u2, u2); //true

        Assert.assertNull(u3);
        Assert.assertNotNull(u2);
    }
}
