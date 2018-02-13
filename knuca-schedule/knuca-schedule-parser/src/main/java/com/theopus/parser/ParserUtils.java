package com.theopus.parser;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            if (s.matches("п.ятниця")) {
                return DayOfWeek.FRIDAY;
            }
            dayOfWeek = (Date) dayOfWeekFormat.parse(s);
        } catch (ParseException e) {
            throw new RuntimeException(e);
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

    public static String readPdfsFromFolder(String string) throws IOException {
        StringBuilder text = new StringBuilder("");
        try (Stream<Path> paths = Files.walk(Paths.get(string))) {
            Set<Path> collect = paths
                    .filter(path -> Files.isRegularFile(path)).collect(Collectors.toSet());
            for (Path path : collect) {
                PDDocument document = PDDocument.load(new File(path.toString()));
                text.append(new PDFTextStripper().getText(document));
                document.close();
            }
        }
        return text.toString();
    }

    public static String pdfFileToString(File file) throws IOException {
        StringBuilder text = new StringBuilder();
        try (PDDocument document = PDDocument.load(file)) {
            document.getClass();

            if (!document.isEncrypted()) {

                PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                stripper.setSortByPosition(true);
                PDFTextStripper pdfTextStripper = new PDFTextStripper();

                text = new StringBuilder(text + pdfTextStripper.getText(document) + "\n");

            }
            document.close();
        }

        return text.toString();
    }
}
