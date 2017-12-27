package com.theopus.parser.obj.table;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Map;

public class SimpleTableTest {

    SimpleTable simpleTable = new SimpleTable(null);
    String file =  new String(Files.readAllBytes(Paths.get("src/test/resources/pdfs/test_file.txt")), "UTF-8");

    public SimpleTableTest() throws IOException {
    }

    @Test
    public void test() throws Exception {
        simpleTable.prepare(file);
        System.out.println(simpleTable.parseTable(file));
        Map<LocalDate, String> localDateStringMap = simpleTable.parseToDays(simpleTable.parseTable(file));

        localDateStringMap.forEach((s, localDate) -> {
            System.out.println(s + " " + localDate);
        });

        System.out.println("===");
        LocalDate toBound = simpleTable.getToBound(DayOfWeek.FRIDAY);
        LocalDate fromBound = simpleTable.getFromBound(DayOfWeek.FRIDAY);
        System.out.println(toBound);
        System.out.println(fromBound);
    }
}