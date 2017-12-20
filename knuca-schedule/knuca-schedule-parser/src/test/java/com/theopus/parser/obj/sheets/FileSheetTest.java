package com.theopus.parser.obj.sheets;

import com.google.common.collect.Lists;
import com.theopus.parser.obj.Patterns;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.*;


public class FileSheetTest {

    private String string = new String(Files.readAllBytes(Paths.get("src/test/resources/pdfs/full_file_test.txt")), "UTF-8");

    public FileSheetTest() throws IOException {
    }

    private FileSheet sheet = FileSheet.create()
            .delimiter(Patterns.Sheet.SHEET_DELIMITER)
            .build();

    @Test
    public void splitToSheets() throws Exception {
        sheet.prepare(string);

        List<String> expected = Lists.newArrayList(
                new String(Files.readAllBytes(Paths.get("src/test/resources/pdfs/pt1_file_test.txt")), "UTF-8"),
                new String(Files.readAllBytes(Paths.get("src/test/resources/pdfs/pt2_file_test.txt")), "UTF-8"),
                new String(Files.readAllBytes(Paths.get("src/test/resources/pdfs/pt3_file_test.txt")), "UTF-8")
        );
        List<String> actual = sheet.splitToSheets();

        assertEquals(expected,actual);
    }
}