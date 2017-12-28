package com.theopus.parser.obj.validator;

import com.google.common.collect.Sets;
import com.theopus.entity.schedule.Circumstance;
import com.theopus.entity.schedule.Curriculum;
import com.theopus.entity.schedule.enums.LessonType;
import com.theopus.parser.exceptions.IllegalPdfException;
import com.theopus.parser.obj.table.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Validator {

    private static final Logger LOG = LoggerFactory.getLogger(Validator.class);
    private Table table;

    public Validator(Table table) {
        this.table = table;
    }

    public List<Curriculum> validate(List<Curriculum> curriculums) {
        table.getScheduleMap().forEach((localDate, tableEntries) -> {
            Set<Table.TableEntry> result = curriculumAtDate(curriculums, localDate);
            if (!Sets.symmetricDifference(result, tableEntries).isEmpty()) {
                LOG.error("{} \n {} \n {} \n {} \n {} \n", localDate, tableEntries, result, table, curriculums);
                table.getDaysMap().forEach((localDate1, s) -> System.out.println(localDate1 + "=" + s));
                LOG.error("{}");
                throw new IllegalPdfException("Parse result not equals to table representation");
            }

        });
        return curriculums;
    }

    public Set<Table.TableEntry> curriculumAtDate(List<Curriculum> curriculumList, LocalDate localDate) {
        return curriculumList.stream().flatMap(curic ->
                curic.getCircumstances().stream()
                        .filter(circ -> circ.getDates().contains(localDate))
                        .map(circumstance1 -> new Table.TableEntry(circumstance1.getLessonOrder(), curic.getCourse().getType()))
        ).collect(Collectors.toSet());
    }
}
