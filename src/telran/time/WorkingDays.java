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
	DayOfWeek[] dayWorks;
	int workingDays;
	
	@Override
	public Temporal adjustInto(Temporal temporal) {
		if (!temporal.isSupported(ChronoField.DAY_OF_WEEK)) {
			throw new UnsupportedOperationException();
		}
		temporal = setNearestWorkingDay(temporal);
		temporal = temporal.plus(workingDays / dayWorks.length, ChronoUnit.WEEKS);
		int restOfDays = workingDays % dayWorks.length;
		temporal = addRestOfWeek(temporal, restOfDays);
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
		while (i < dayWorks.length && dayOfWeek != dayWorks[i].getValue()) {
			i++;
		}
		return i == dayWorks.length ? false : true;
	}
	
	public WorkingDays(int workingDays, DayOfWeek[] dayOffs) {
		dayWorks = getWorkingDays(dayOffs);
		this.workingDays = workingDays;
	}

	private DayOfWeek[] getWorkingDays(DayOfWeek[] dayOffs) {
		Set<DayOfWeek> dayOffsSet = Set.of(dayOffs);
		DayOfWeek[] res = new DayOfWeek[7 - dayOffsSet.size()];
		int index = 0;
		for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
			if (!dayOffsSet.contains(dayOfWeek)) {
				res[index++] = dayOfWeek;
			}
		}
		return res;
	}
}
