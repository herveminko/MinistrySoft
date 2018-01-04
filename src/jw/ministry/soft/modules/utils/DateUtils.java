package jw.ministry.soft.modules.utils;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

import jw.ministry.soft.application.Main;

import org.joda.time.Months;
import org.joda.time.YearMonth;

public class DateUtils {

	public static Date asDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay()
				.atZone(ZoneId.systemDefault()).toInstant());
	}

	public static Date asDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault())
				.toInstant());
	}

	public static LocalDate asLocalDate(Date date) {
		return Instant.ofEpochMilli(date.getTime())
				.atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static LocalDateTime asLocalDateTime(Date date) {
		return Instant.ofEpochMilli(date.getTime())
				.atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	public static String getUiDateFormat() {

		ResourceBundle resources;
		try {
			resources = Main.getMainBundle();
			return resources.getString("ui_date_format");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public static YearMonth toYearMonth(org.joda.time.LocalDate localDate) {

		// org.joda.time.LocalDate
		return new YearMonth(localDate.getYear(), localDate.getMonthOfYear());
	}

	public static int monthSwitches(org.joda.time.LocalDate date1,
			org.joda.time.LocalDate date2) {
		return Months.monthsBetween(toYearMonth(date1), toYearMonth(date2))
				.getMonths();
	}

	public static org.joda.time.LocalDate converToJodaDate(Date jDate) {
		return org.joda.time.LocalDate.fromDateFields(jDate);
	}

	/**
	 * Calculate the difference in months between two dates.
	 *
	 * @param date1
	 *            is the first comparison date.
	 * @param date2
	 *            is the second comparison date
	 * @return the amount of months between those 2 dates.
	 */
	public static int monthDifference(Date date1, Date date2) {

		if (date1 == null || date2 == null) {
			return Integer.MAX_VALUE;
		} else {
			org.joda.time.LocalDate d1 = converToJodaDate(date1);
			org.joda.time.LocalDate d2 = converToJodaDate(date2);
			return monthSwitches(d1, d2);
		}
	}

}
