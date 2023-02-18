package telran.time.application;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.Locale;

public class PrintCalendar {

	private static final String LANGUAGE_TAG = "en";
	private static final int YEAR_OFFSET = 10;
	private static final int CALENDAR_OFFSET = 2;
	private static final int CELL_WIDTH = 4;

	public static void main(String[] args) {
		try {
			int monthYear[] = getMonthYear(args);
			int firstDayOffset = getFirstDayOffset(args);
			printCalendar(monthYear[0], monthYear[1], firstDayOffset);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private static int getFirstDayOffset(String[] args) throws Exception {
		int res = 0;
		if (args.length > 2) {
			try {
				res = DayOfWeek.valueOf(args[2].toUpperCase()).getValue() - 1;
			} catch (Throwable e) {
				throw new Exception("Name of first week day is incorrect");
			}
		}
		return res;
	}

	private static void printCalendar(int month, int year, int firstDayOffset) {
		printTitle(month, year);
		printWeekDays(firstDayOffset);
		printDates(month, year, firstDayOffset);
	}

	private static void printDates(int month, int year, int firstDayOffset) {
		
		int weekDayNumber = getFirstDay(month, year, firstDayOffset);
		int offset = getOffset(weekDayNumber);
		
		int nDays = YearMonth.of(year, month).lengthOfMonth();
		
		System.out.printf("%s", " ".repeat(offset + CALENDAR_OFFSET));
		for (int date = 1; date <= nDays; date++) {
			System.out.printf("%4d", date);
			if (++weekDayNumber > 7) {
				System.out.printf("\n%s", " ".repeat(CALENDAR_OFFSET));
				weekDayNumber = 1;
			}
		}
	}

	private static int getOffset(int weekDayNumber) {
		return (weekDayNumber - 1) * CELL_WIDTH;
	}

	private static int getFirstDay(int month, int year, int firstDayOffset) {
		int res = LocalDate.of(year, month, 1).getDayOfWeek().getValue() - firstDayOffset;
		if (res < 1) {
			res += 7;
		}
		return res;
	}

	private static void printWeekDays(int firstDayOffset) {
		System.out.print(" ".repeat(CALENDAR_OFFSET));
		DayOfWeek[] weekArr2 = getWeekArr(firstDayOffset);
		Arrays.stream(weekArr2)
			.forEach(dw -> System.out.printf(" %s", dw.getDisplayName(TextStyle.SHORT, Locale.forLanguageTag(LANGUAGE_TAG))));
		System.out.println();
	}

	private static DayOfWeek[] getWeekArr(int firstDayOffset) {
		DayOfWeek[] res = new DayOfWeek[7];
		for (int i = 0; i < 7; i++) {
			int shiftedIndex = i + firstDayOffset;
			if (shiftedIndex > 6) {
				shiftedIndex -= 7;
			}
			res[i] = DayOfWeek.values()[shiftedIndex];
		}
		return res;
	}

	private static void printTitle(int month, int year) {
		String monthName = Month.of(month)
				.getDisplayName(
						TextStyle.FULL, 
						Locale.forLanguageTag(LANGUAGE_TAG));
		int yearOffset = ((7 * CELL_WIDTH) - (6 + monthName.length())) / 2;
		System.out.printf("%s%d, %s\n", " ".repeat(yearOffset + CALENDAR_OFFSET), year, monthName);
	}

	private static int[] getMonthYear(String[] args) throws Exception {
		return args.length == 0 ? getCurrentMonthYear() : getMonthYearArgs(args);
	}

	private static int[] getMonthYearArgs(String[] args) throws Exception {
		return new int[]{getMonth(args), getYear(args)};
	}

	private static int getYear(String[] args) throws Exception {
		int res = LocalDate.now().getYear();
		if (args.length > 1) {
			try {
				res = Integer.parseInt(args[1]);
				if (res < 0) {
					throw new Exception("Year must be a positive number");
				}
			} catch (NumberFormatException e) {
				throw new Exception("Year must be a number");
			}
		}
		return res;
	}

	private static int getMonth(String[] args) throws Exception {
		try {
			int res = Integer.parseInt(args[0]);
			if (res < 0 || res > 12) {
				throw new Exception("Month should be in range 1-12");
			}
			return res;
		} catch (NumberFormatException e) {
			throw new Exception("Month must be a number");
		}
	}

	private static int[] getCurrentMonthYear() {
		LocalDate current = LocalDate.now();
		return new int[]{current.getMonth().getValue(), current.getYear()};
	}
}
