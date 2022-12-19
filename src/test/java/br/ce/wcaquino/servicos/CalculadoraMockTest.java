package br.ce.wcaquino.servicos;

import org.junit.Test;
import org.mockito.Mockito;

public class CalculadoraMockTest {

  @Test
  public void teste() {
    Calculadora calculadora = Mockito.mock(Calculadora.class);
    //Mockito.when(calculadora.somar(1, 2)).thenReturn(5);
    //Mockito.when(calculadora.somar(Mockito.anyInt(), Mockito.anyInt())).thenReturn(5); //qualquer valor inserido retornará 5
    //restringindo o valor de entrada
    Mockito.when(calculadora.somar(Mockito.eq(1), Mockito.anyInt())).thenReturn(5);

    System.out.println(calculadora.somar(1, 2));
  }
}