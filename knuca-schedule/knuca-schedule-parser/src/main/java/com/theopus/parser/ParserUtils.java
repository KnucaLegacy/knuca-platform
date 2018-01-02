package com.theopus.parser;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

public class ParserUtils {

    private static SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("EEEE", Locale.forLanguageTag("uk-UA"));

    public static String trimWhitespaces(String string) {
        return string
                .replaceFirst("\\s++$", "")
                .replaceFirst("^\\s++", "");
    }

    public static String normalize(String string) {
        string = string.replaceAll("\n", " ");
        string = string.replaceAll("\r", " ");
        string = string.replaceAll("\\s++", " ");
        return string;
    }

    public static DayOfWeek converUkrDayOfWeek(String dow) {
        Date dayOfWeek = null;
        try {
            String s = dow.toLowerCase().replaceAll("i", "і");
            dayOfWeek = (Date) dayOfWeekFormat.parseObject(s);
        } catch (ParseException e) {
            //TODO hadle.
            e.printStackTrace();
        }
        return dayOfWeek
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .getDayOfWeek();
    }

    public static String replaceEngToUkr(String string) {
        String eng = "AaBCcEeHIiKkMOoPpTXx?";
        String ukr = "АаВСсЕеНІіКкМОоРрТХхі";
        return StringUtils.replaceChars(string, eng, ukr);
    }
}
