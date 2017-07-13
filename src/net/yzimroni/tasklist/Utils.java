package net.yzimroni.tasklist;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

	private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");

	private Utils() {

	}

	public static String formatDate(long time) {
		return DATE_FORMATTER.format(new Date(time));
	}

}
