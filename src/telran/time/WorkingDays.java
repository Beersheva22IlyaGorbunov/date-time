package telran.time;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

import javax.naming.OperationNotSupportedException;

public class WorkingDays implements TemporalAdjuster {
	private int workingDays;
	private DayOfWeek[] dayOffs;
	
	@Override
	public Temporal adjustInto(Temporal temporal) {
		if (!temporal.isSupported(ChronoField.DAY_OF_WEEK)) {
			throw new UnsupportedOperationException();
		}
		int workingWeekLength = DayOfWeek.values().length - dayOffs.length;
		if (workingWeekLength > 0) {
			temporal = setNearestWorkingDay(temporal);
			temporal = temporal.plus(workingDays / workingWeekLength, ChronoUnit.WEEKS);
			int restOfDays = workingDays % workingWeekLength;
			temporal = addRestOfWeek(temporal, restOfDays);
		}
		return temporal;
	}

	private Temporal setNearestWorkingDay(Temporal temporal) {
		while (!isWorkingDay(temporal.get(ChronoField.DAY_OF_WEEK))) {
			temporal = temporal.plus(1, ChronoUnit.DAYS);
		}
		return temporal;
	}

	private Temporal addRestOfWeek(Temporal temporal, int restOfDays) {
		while (restOfDays > 0) {
			temporal = temporal.plus(1, ChronoUnit.DAYS);
			if (isWorkingDay(temporal.get(ChronoField.DAY_OF_WEEK))) {
				restOfDays--;
			}
		}
		return temporal;
	}
	
	private boolean isWorkingDay(int dayOfWeek) {
		int i = 0;
		while (i < dayOffs.length && dayOfWeek != dayOffs[i].getValue()) {
			i++;
		}
		return i == dayOffs.length ? true : false;
	}
	
	public WorkingDays(int workingDays, DayOfWeek[] dayOffs) {
		this.dayOffs = dayOffs;
		this.workingDays = workingDays;
	}

}
