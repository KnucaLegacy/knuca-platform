package com.theopus.parser.obj;

import com.google.common.collect.Lists;
import com.theopus.entity.schedule.Curriculum;
import com.theopus.entity.schedule.Group;
import com.theopus.parser.ParserUtils;
import com.theopus.parser.exceptions.IllegalParserFileException;
import com.theopus.parser.facade.Parser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class FileSheetTest {

    private String string = new String(Files.readAllBytes(Paths.get("src/test/resources/pdfs/full_file_test.txt")), "UTF-8");

    public FileSheetTest() throws IOException {
    }

    private FileSheet sheet = FileSheet.create()
            .delimiter(Patterns.Sheet.SHEET_DELIMITER)
            .build();

    @Test(expected = IllegalParserFileException.class)
    public void splitToShhersEmptySheet() throws Exception {
        Parser.onlyFileSheetParser().prepare("");
    }
    @Test
    public void splitToSheets() throws Exception {
        sheet.prepare(ParserUtils.replaceEngToUkr(string));
        List<String> expected = Lists.newArrayList(
                ParserUtils.replaceEngToUkr(new String(Files.readAllBytes(Paths.get("src/test/resources/pdfs/pt1_file_test.txt")), "UTF-8")),
                ParserUtils.replaceEngToUkr(new String(Files.readAllBytes(Paths.get("src/test/resources/pdfs/pt2_file_test.txt")), "UTF-8")),
                ParserUtils.replaceEngToUkr(new String(Files.readAllBytes(Paths.get("src/test/resources/pdfs/pt3_file_test.txt")), "UTF-8"))
        );
        List<String> actual = sheet.splitToSheets();

        assertEquals(expected, actual);
    }

    @Test
    public void fullFileParseTest() throws Exception {
        StringBuilder text = null;

        try (Stream<Path> paths = Files.walk(Paths.get("src/test/resources/pdfs/full/small"))) {
            Set<Path> collect = paths
                    .filter(path -> Files.isRegularFile(path)).collect(Collectors.toSet());
            for (Path path : collect) {
                try (PDDocument document = PDDocument.load(new File(path.toString()))) {

                    document.getClass();

                    if (!document.isEncrypted()) {

                        PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                        stripper.setSortByPosition(true);
                        PDFTextStripper tStripper = new PDFTextStripper();

                        text = new StringBuilder(text + tStripper.getText(document) + "\n");

                    }
                    document.close();
                }
            }
        }

        Validator validator = new Validator();
        FileSheet<Group> sheet = FileSheet.<Group>create()
                .delimiter(Patterns.Sheet.SHEET_DELIMITER)
                .build()
                .child(GroupSheet.<Group>createGroupSheet()
                        .deafultTableBound()
                        .defaultPatterns()
                        .anchorPattern(Patterns.Sheet.EXACT_GROUP_PATTERN)
                        .table(new SimpleTable().defaultPatternsMap())
                        .build()
                        .validator(validator)
                        .child(DaySheet.<Group>create()
                                .defaultPatterns()
                                .build()
                                .child(LessonLines.createGroupLine()
                                        .defaultPatterns()
                                        .orderPatterns("src/main/resources/parser-lessonorder.properties")
                                        .build()
                                        .child(RoomDateBrackets.create()
                                                .defaultPatterns()
                                                .build()
                                        )
                                )
                        )
                );
        sheet.prepare(text.toString()).parseAll();
        System.out.println(validator.hitCount());
        assertTrue(validator.hitCount() >= 96d);
    }


    @Test
    public void fullFileParseTest_big_file() throws Exception {
        StringBuilder text = new StringBuilder();

        try (Stream<Path> paths = Files.walk(Paths.get("src/test/resources/pdfs/full/big"))) {
            Set<Path> collect = paths
                    .filter(path -> Files.isRegularFile(path)).collect(Collectors.toSet());
            for (Path path : collect) {
                PDDocument document = PDDocument.load(new File(path.toString()));
                text.append(new PDFTextStripper().getText(document));
            }
        }

        Validator validator = new Validator();
        FileSheet<Group> sheet = FileSheet.<Group>create()
                .delimiter(Patterns.Sheet.SHEET_DELIMITER)
                .build()
                .child(GroupSheet.<Group>createGroupSheet()
                        .deafultTableBound()
                        .defaultPatterns()
                        .anchorPattern(Patterns.Sheet.EXACT_GROUP_PATTERN)
                        .table(new SimpleTable().defaultPatternsMap())
                        .build()
                        .validator(validator)
                        .child(DaySheet.<Group>create()
                                .defaultPatterns()
                                .build()
                                .child(LessonLines.createGroupLine()
                                        .defaultPatterns()
                                        .orderPatterns("src/main/resources/parser-lessonorder.properties")
                                        .build()
                                        .child(RoomDateBrackets.create()
                                                .defaultPatterns()
                                                .build()
                                        )
                                )
                        )
                );
        sheet.prepare(text.toString(), 2018).parseAll();
        System.out.println(validator.hitCount());
        assertTrue(validator.hitCount() >= 96d);
    }

    @Test
    public void fullFileParseTest_file_msdosdoc() throws Exception {
        String text = ParserUtils.msdosFileToString(new File("src/test/resources/doc/KEK-msdos.doc"));

        Validator validator = new Validator();
        FileSheet<Group> sheet = FileSheet.<Group>create()
                .delimiter(Patterns.Sheet.SHEET_DELIMITER)
                .build()
                .child(GroupSheet.<Group>createGroupSheet()
                        .deafultTableBound()
                        .defaultPatterns()
                        .anchorPattern(Patterns.Sheet.EXACT_GROUP_PATTERN)
                        .table(new SimpleTable().defaultPatternsMap())
                        .build()
                        .validator(validator)
                        .child(DaySheet.<Group>create()
                                .defaultPatterns()
                                .build()
                                .child(LessonLines.createGroupLine()
                                        .defaultPatterns()
                                        .orderPatterns("src/main/resources/parser-lessonorder.properties")
                                        .build()
                                        .child(RoomDateBrackets.create()
                                                .defaultPatterns()
                                                .build()
                                        )
                                )
                        )
                );
        List<Curriculum> curricula = sheet.prepare(text, 2018).parseAll();
        assertTrue(validator.hitCount() >= 96d);
    }

    @Test
    public void fullFileParseTest_file_doc() throws Exception {
        String text = ParserUtils.docFileToString(new File("src/test/resources/doc/kek.doc"));

        Validator validator = new Validator();
        FileSheet<Group> sheet = FileSheet.<Group>create()
                .delimiter(Patterns.Sheet.SHEET_DELIMITER)
                .build()
                .child(GroupSheet.<Group>createGroupSheet()
                        .deafultTableBound()
                        .defaultPatterns()
                        .anchorPattern(Patterns.Sheet.EXACT_GROUP_PATTERN)
                        .table(new SimpleTable().defaultPatternsMap())
                        .build()
                        .validator(validator)
                        .child(DaySheet.<Group>create()
                                .defaultPatterns()
                                .build()
                                .child(LessonLines.createGroupLine()
                                        .defaultPatterns()
                                        .orderPatterns("src/main/resources/parser-lessonorder.properties")
                                        .build()
                                        .child(RoomDateBrackets.create()
                                                .defaultPatterns()
                                                .build()
                                        )
                                )
                        )
                );
        List<Curriculum> curricula = sheet.prepare(text, 2018).parseAll();
        assertTrue(validator.hitCount() >= 96d);
    }

    @Test
    public void fullFileParseTest_file_docx() throws Exception {
        String text = ParserUtils.docxFileToString(new File("src/test/resources/doc/kek.docx"));

        Validator validator = new Validator();
        FileSheet<Group> sheet = FileSheet.<Group>create()
                .delimiter(Patterns.Sheet.SHEET_DELIMITER)
                .build()
                .child(GroupSheet.<Group>createGroupSheet()
                        .deafultTableBound()
                        .defaultPatterns()
                        .anchorPattern(Patterns.Sheet.EXACT_GROUP_PATTERN)
                        .table(new SimpleTable().defaultPatternsMap())
                        .build()
                        .validator(validator)
                        .child(DaySheet.<Group>create()
                                .defaultPatterns()
                                .build()
                                .child(LessonLines.createGroupLine()
                                        .defaultPatterns()
                                        .orderPatterns("src/main/resources/parser-lessonorder.properties")
                                        .build()
                                        .child(RoomDateBrackets.create()
                                                .defaultPatterns()
                                                .build()
                                        )
                                )
                        )
                );
        List<Curriculum> curricula = sheet.prepare(text, 2018).parseAll();
        assertTrue(validator.hitCount() >= 96d);
    }


    @Test
    public void fullFileParseTest_small_files() throws Exception {
        StringBuilder text = null;

        try (Stream<Path> paths = Files.walk(Paths.get("src/test/resources/pdfs/full/xsmall"))) {
            Set<Path> collect = paths
                    .filter(path -> Files.isRegularFile(path)).collect(Collectors.toSet());
            for (Path path : collect) {
                try (PDDocument document = PDDocument.load(new File(path.toString()))) {

                    document.getClass();

                    if (!document.isEncrypted()) {

                        PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                        stripper.setSortByPosition(true);
                        PDFTextStripper tStripper = new PDFTextStripper();

                        text = new StringBuilder(text + tStripper.getText(document) + "\n");

                    }
                    document.close();
                }
            }
        }

        Validator validator = new Validator();
        FileSheet<Group> sheet = FileSheet.<Group>create()
                .delimiter(Patterns.Sheet.SHEET_DELIMITER)
                .build()
                .child(GroupSheet.<Group>createGroupSheet()
                        .deafultTableBound()
                        .defaultPatterns()
                        .anchorPattern(Patterns.Sheet.EXACT_GROUP_PATTERN)
                        .table(new SimpleTable().defaultPatternsMap())
                        .build()
                        .validator(validator)
                        .child(DaySheet.<Group>create()
                                .defaultPatterns()
                                .build()
                                .child(LessonLines.createGroupLine()
                                        .defaultPatterns()
                                        .orderPatterns("src/main/resources/parser-lessonorder.properties")
                                        .build()
                                        .child(RoomDateBrackets.create()
                                                .defaultPatterns()
                                                .build()
                                        )
                                )
                        )
                );
        sheet.prepare(text.toString()).parseAll();
        System.out.println(validator.hitCount());
        assertTrue(validator.hitCount() >= 96d);
    }
}