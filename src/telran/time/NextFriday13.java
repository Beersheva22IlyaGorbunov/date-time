package telran.time;

import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;

public class NextFriday13 implements TemporalAdjuster {

	@Override
	public Temporal adjustInto(Temporal temporal) {
		Temporal checkedDate = initialDate(temporal);
		while(!(checkedDate.get(ChronoField.DAY_OF_MONTH) == 13 && checkedDate.get(ChronoField.DAY_OF_WEEK) == 5)) {
			checkedDate = checkedDate.plus(1, ChronoUnit.MONTHS);
		}
		return checkedDate;
	}

	private Temporal initialDate(Temporal temporal) {
		Temporal checkedDate;
		int temporalDay = temporal.get(ChronoField.DAY_OF_MONTH);
		if (temporalDay > 12) { 
			checkedDate = temporal.minus(temporalDay - 13, ChronoUnit.DAYS).plus(1, ChronoUnit.MONTHS);
		} else {
			checkedDate = temporal.plus(13 - temporalDay, ChronoUnit.DAYS);
		}
		return checkedDate;
	}

}
