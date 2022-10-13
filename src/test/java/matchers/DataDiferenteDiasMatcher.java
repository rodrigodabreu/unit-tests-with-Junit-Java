package matchers;

import br.ce.wcaquino.utils.DataUtils;
import java.util.Date;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class DataDiferenteDiasMatcher extends TypeSafeMatcher<Date> {

  private Integer quantidadeDias;

  public DataDiferenteDiasMatcher(final Integer quantidadeDias) {
    this.quantidadeDias = quantidadeDias;
  }

  public void describeTo(Description arq0) {
  }

  @Override
  protected boolean matchesSafely(Date date) {
    return DataUtils.isMesmaData(date, DataUtils.obterDataComDiferencaDias(quantidadeDias));
  }
}