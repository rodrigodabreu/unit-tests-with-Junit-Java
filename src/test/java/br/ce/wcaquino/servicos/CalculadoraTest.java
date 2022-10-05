package br.ce.wcaquino.servicos;

import exceptions.NaoPodeDividirPorZeroException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CalculadoraTest {

  private Calculadora calculadora;

  @Before
  public void setup() {
    calculadora = new Calculadora();
  }

  @Test
  public void deveSomarDoisValores() {
    //cenario
    int a = 5;
    int b = 3;

    //acao
    int result = calculadora.somar(a, b);

    //verificacao
    Assert.assertEquals(8, result);
  }

  @Test
  public void deveMultiplicarDoisValores() {
    //cenario
    int a = 5;
    int b = 3;

    //acao
    int result = calculadora.multiplicar(5, 3);

    //verificacao
    Assert.assertEquals(24, result);
  }

  @Test
  public void deveSubtrairDoisValores() {
    //cenario
    int a = 5;
    int b = 3;

    //acao
    int result = calculadora.subtrair(a, b);

    //verificacao
    Assert.assertEquals(2, result);
  }

  @Test
  public void deveDividirDoisValoresValidos() throws NaoPodeDividirPorZeroException {
    //cenario
    int a = 15;
    int b = 3;
    double expected = 5.0;

    //acao
    double result = calculadora.dividir(a, b);

    //verificacao
    Assert.assertEquals(expected, result);
  }

  @Test(expected = NaoPodeDividirPorZeroException.class)
  public void deveLancarExcecaoAoDividirPorZero() throws NaoPodeDividirPorZeroException {
    //cenario
    int a = 15;
    int b = 0;

    //acao
    double result = calculadora.dividir(a, b);
  }

}
