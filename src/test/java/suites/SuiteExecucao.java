//package suites;
//
//import br.ce.wcaquino.servicos.CalculadoraTest;
//import br.ce.wcaquino.servicos.CalculoValorLocacaoTest;
//import br.ce.wcaquino.servicos.LocacaoServiceTest;
//import org.junit.AfterClass;
//import org.junit.BeforeClass;
//import org.junit.runner.RunWith;
//import org.junit.runners.Suite;
//import org.junit.runners.Suite.SuiteClasses;
//
///**
// * Contras da utilização de Suites de testes
// *
// * Necessidade de adicionar cada nova classe de testes na lista de SuiteClasses
// * Em caso de testes automatizados, temos a duplicidade de execução tendo em vista que as classes de
// * testes serão executadas na suite e também na automação.
// */
//
//
//@RunWith(Suite.class)
//@SuiteClasses( {
//    CalculadoraTest.class,
//    CalculoValorLocacaoTest.class,
//    LocacaoServiceTest.class
//})
//public class SuiteExecucao {
//  //Remova se puder!
//
//  @BeforeClass
//  public static void before(){
//    System.out.println("before");
//  }
//
//  @AfterClass
//  public static void after(){
//    System.out.println("after");
//  }
//}