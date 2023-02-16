package telran.time;

import static org.junit.jupiter.api.Assertions.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

class DateTimeTests {

	@Test
	void localDateTest() {
		LocalDate birthDateASDate = LocalDate.of(1799, 6, 6);
		LocalDate barMizvaAS = birthDateASDate.plusYears(13);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM, YYYY, d", Locale.forLanguageTag("ru"));
		System.out.println(barMizvaAS.format(dtf));
		
		ChronoUnit unit = ChronoUnit.WEEKS;
		System.out.printf("Number of %s between %s and %s is %s\n", unit, birthDateASDate, barMizvaAS, unit.between(birthDateASDate, barMizvaAS));
	}
	
	@Test
	void barMizvaTest() {
		LocalDate current = LocalDate.now();
		assertEquals(current.plusYears(13), current.with(new BarMizvaAdjuster()));
	}
	
	@Test
	void canadaTimeZonesTest() {
		ZonedDateTime current = ZonedDateTime.now();
		Set<String> zones = ZoneId.getAvailableZoneIds();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("kk:mm:ss dd-MM-YY");
		zones.stream()
				.filter(zone -> zone.contains("Canada"))
				.forEach(zone -> {
					ZonedDateTime thisZoneTime = current.withZoneSameInstant(ZoneId.of(zone));
					System.out.println(zone + " ".repeat(25 - zone.length()) + thisZoneTime.format(dtf));
				});
	}
	
	@Test
	void nextFriday13Test() {
		LocalDate firstJanuary2023 = LocalDate.of(2023, 1, 1);
		LocalDate firstFriday13in2023 = LocalDate.of(2023, 1, 13);
		LocalDate secondFriday13in2023 = LocalDate.of(2023, 10, 13);
		assertEquals(firstFriday13in2023, firstJanuary2023.with(new NextFriday13()));
		assertEquals(secondFriday13in2023, firstFriday13in2023.with(new NextFriday13()));
	}
	
	@Test
	void workingDaysTest() {
		LocalDate secondFriday13in2023 = LocalDate.of(2023, 10, 13);
		LocalDate expected = LocalDate.of(2023, 10, 29);
		assertEquals(expected, secondFriday13in2023.with(new WorkingDays(10, new DayOfWeek[]{DayOfWeek.FRIDAY, DayOfWeek.SATURDAY})));
		LocalDate expected2 = LocalDate.of(2023, 11, 1);
		assertEquals(expected2, secondFriday13in2023.with(new WorkingDays(13, new DayOfWeek[]{DayOfWeek.FRIDAY, DayOfWeek.SATURDAY})));
	}
}
