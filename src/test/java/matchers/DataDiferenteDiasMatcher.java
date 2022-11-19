package matchers;

import br.ce.wcaquino.utils.DataUtils;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class DataDiferenteDiasMatcher extends TypeSafeMatcher<Date> {

  private Integer quantidadeDias;

  public DataDiferenteDiasMatcher(final Integer quantidadeDias) {
    this.quantidadeDias = quantidadeDias;
  }

  @Override
  public void describeTo(Description description) {
    Calendar data = Calendar.getInstance();
    data.set(Calendar.DAY_OF_WEEK, quantidadeDias);
    String dataPorExtenso = data.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG,
        new Locale("pt", "BR"));
    description.appendText(dataPorExtenso);
  }

  @Override
  protected boolean matchesSafely(Date date) {
    return DataUtils.isMesmaData(date, DataUtils.obterDataComDiferencaDias(quantidadeDias));
  }
}