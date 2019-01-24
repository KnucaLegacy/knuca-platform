package com.theopus.parser.obj;

import com.google.common.collect.Sets;
import com.theopus.entity.schedule.Curriculum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Validator {

    private static final Logger LOG = LoggerFactory.getLogger(Validator.class);
    private int errorCount;
    private Sheet parent;

    public Validator() {

    }

    public ValidatorReport validate(List<Curriculum> curriculums) {
        Table table = parent.getTable();
        ValidatorReport validatorReport = new ValidatorReport(table.getParent().getContent(), table.getDaysMap());

        for (Map.Entry<LocalDate, Set<Table.TableEntry>> localDateSetEntry : table.getScheduleMap().entrySet()) {
            Set<Table.TableEntry> tableEntries = localDateSetEntry.getValue();
            LocalDate localDate = localDateSetEntry.getKey();

            Set<Table.TableEntry> result = this.tableEntryAtDate(curriculums, localDate);
            Sets.SetView<Table.TableEntry> tableEntries1 = Sets.symmetricDifference(result, tableEntries);

            if (!tableEntries1.isEmpty()) {
                LOG.debug(
                        "{} \n " +
                                "{} \n " +
                                "{} \n " +
                                "{} \n " +
                                "{} \n " +
                                "{} \n",
                        localDate,
                        tableEntries,
                        result,
                        curriculums,
                        table.getDaysMap(),
                        table.getParent().getContent()
                );

                validatorReport.trackError(
                        localDate,
                        tableEntries,
                        result,
                        curriculumAtDate(curriculums, localDate)
                );
                errorCount++;
            }
        }

        return validatorReport;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public <T> Validator parent(Sheet sheet) {
        this.parent = sheet;
        this.errorCount = 0;
        return this;
    }

    public float hitCount() {
        return 100 - getErrorCount() / ((parent.getParent().getPosition() + 1) / 100f);
    }

    public Set<Table.TableEntry> tableEntryAtDate(List<Curriculum> curriculumList, LocalDate localDate) {
        return curriculumList.stream().flatMap(curic ->
                curic.getCircumstances().stream()
                        .filter(circ -> circ.getDates().contains(localDate))
                        .map(circumstance1 -> new Table.TableEntry(circumstance1.getLessonOrder(), curic.getCourse().getType()))
        ).collect(Collectors.toSet());
    }

    private List<Curriculum> curriculumAtDate(List<Curriculum> curriculumList, LocalDate localDate) {
        return curriculumList.stream().filter(atThisDayPredicate(localDate)).collect(Collectors.toList());
    }

    private Predicate<Curriculum> atThisDayPredicate(LocalDate localDate) {
        return curriculum -> curriculum.getCircumstances().stream().filter(c -> c.getDates().contains(localDate)).count() != 0;
    }


}
