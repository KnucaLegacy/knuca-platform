package com.theopus.parser.obj.sheets;

import com.google.common.collect.Lists;
import com.theopus.entity.schedule.Curriculum;
import com.theopus.entity.schedule.Group;
import com.theopus.parser.obj.Patterns;
import com.theopus.parser.obj.line.LessonLines;
import com.theopus.parser.obj.roomdate.RoomDateBrackets;
import com.theopus.parser.obj.table.SimpleTable;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class FileSheetTest {

    private String string = new String(Files.readAllBytes(Paths.get("src/test/resources/pdfs/full_file_test.txt")), "UTF-8");

    public FileSheetTest() throws IOException {
    }

    private FileSheet sheet = FileSheet.create()
            .delimiter(Patterns.Sheet.SHEET_DELIMITER)
            .build();

    @Ignore
    @Test
    public void splitToSheets() throws Exception {
        sheet.prepare(string);

        List<String> expected = Lists.newArrayList(
                new String(Files.readAllBytes(Paths.get("src/test/resources/pdfs/pt1_file_test.txt")), "UTF-8"),
                new String(Files.readAllBytes(Paths.get("src/test/resources/pdfs/pt2_file_test.txt")), "UTF-8"),
                new String(Files.readAllBytes(Paths.get("src/test/resources/pdfs/pt3_file_test.txt")), "UTF-8")
        );
        List<String> actual = sheet.splitToSheets();

        assertEquals(expected, actual);
    }

    @Test
    public void buildTest() throws Exception {
        String text = null;
        try (PDDocument document = PDDocument.load(new File("src/test/resources/pdfs/Arch.pdf"))) {

            document.getClass();

            if (!document.isEncrypted()) {

                PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                stripper.setSortByPosition(true);
                PDFTextStripper tStripper = new PDFTextStripper();

                text = tStripper.getText(document);

            }
            document.close();
        }

        FileSheet<Group> sheet = FileSheet.<Group>create()
                .delimiter(Patterns.Sheet.SHEET_DELIMITER)
                .build()
                .child(GroupSheet.<Group>createGroupSheet()
                        .deafultTableBound()
                        .defaultPatterns()
                        .anchorPattern(Patterns.Sheet.EXACT_GROUP_PATTERN)
                        .table(new SimpleTable().defaultPatternsMap())
                        .build()
                        .child(DaySheet.<Group>create()
                                .defaultPatterns()
                                .build()
                                .child(LessonLines.createGroupLine()
                                        .defaultPatterns()
                                        .defaultOrderPatterns()
                                        .build()
                                        .child(RoomDateBrackets.create()
                                                .defaultPatterns()
                                                .build()
                                        )
                                )
                        )
                );
        sheet.prepare(text).parseAll();
    }
}