package matchers;

import java.util.Calendar;

public class MatcherProprios {

  public static DiaSemanaMatcher caiEm(Integer diaSemana) {
    return new DiaSemanaMatcher(diaSemana);
  }

  public static DiaSemanaMatcher caiNumaSegunda(Integer diaSemana) {
    return new DiaSemanaMatcher(Calendar.MONDAY);
  }

  public static DataDiferenteDiasMatcher ehHojeComDiferencaDeDia(final Integer diasDeDiferenca) {
    return new DataDiferenteDiasMatcher(diasDeDiferenca);
  }

  public static DataDiferenteDiasMatcher ehHoje() {
    return new DataDiferenteDiasMatcher(0);
  }
}