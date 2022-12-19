package br.ce.wcaquino.servicos;

import exceptions.NaoPodeDividirPorZeroException;

public class Calculadora {

  public int somar(final int a, final int b) {
    return a + b;
  }

  public int multiplicar(final int a, final int b) {
    return a * b;
  }

  public int subtrair(final int a, final int b) {
    return a - b;
  }

  public double dividir(final int a, final int b) throws NaoPodeDividirPorZeroException {
    if (b == 0) {
      throw new NaoPodeDividirPorZeroException();
    }
    return a / b;
  }

  public int divide(String a, String b) {
    return Integer.valueOf(a) / Integer.valueOf(b);
  }
}