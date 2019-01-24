package com.theopus.parser.obj;

import com.theopus.entity.schedule.Curriculum;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDate;
import java.util.*;

public class ValidatorReport {

    private final String content;
    private final Set<Violation> errors;
    private final Map<LocalDate, String> daysMap;

    public ValidatorReport(String content, Map<LocalDate, String> daysMap) {
        this.content = content;
        this.daysMap = daysMap;
        this.errors = new HashSet<>();
    }

    public void trackError(LocalDate localDate,
                           Set<Table.TableEntry> tableEntries,
                           Set<Table.TableEntry> result,
                           List<Curriculum> curriculums) {
        errors.add(new Violation(localDate, tableEntries, result, curriculums));
    }

    public String getContent() {
        return content;
    }

    public Set<Violation> getErrors() {
        return errors;
    }

    public Map<LocalDate, String> getDaysMap() {
        return daysMap;
    }

    public int getViolationsCount() {
        return errors.size();
    }

    public int getCriticalViolationsCount() {
        return (int) errors.stream().filter(Violation::isMissingDifference).count();
    }

    public class Violation {

        private LocalDate violationDate;
        private Set<Table.TableEntry> expected;
        private Set<Table.TableEntry> actual;
        private Collection<Table.TableEntry> difference;
        private boolean missingDifference;
        private List<Curriculum> parsedData;

        private Violation(LocalDate violationDate,
                          Set<Table.TableEntry> expected,
                          Set<Table.TableEntry> actual,
                          List<Curriculum> parsedData) {
            this.violationDate = violationDate;
            this.expected = expected;
            this.actual = actual;
            this.difference = getDifference();
            this.parsedData = parsedData;
        }

        private Collection<Table.TableEntry> getDifference() {
            Collection<Table.TableEntry> subtract = CollectionUtils.subtract(expected, actual);

            if (subtract.isEmpty()) {
                subtract = CollectionUtils.subtract(actual, expected);
                if (subtract.isEmpty()) {
                    throw new RuntimeException("No difference but tracked as violation \n" + expected + "\n" + actual);
                }
                missingDifference = false;
                return subtract;
            }
            missingDifference = true;
            return subtract;
        }


        public LocalDate getViolationDate() {
            return violationDate;
        }

        public void setViolationDate(LocalDate violationDate) {
            this.violationDate = violationDate;
        }

        public Set<Table.TableEntry> getExpected() {
            return expected;
        }

        public void setExpected(Set<Table.TableEntry> expected) {
            this.expected = expected;
        }

        public Set<Table.TableEntry> getActual() {
            return actual;
        }

        public void setActual(Set<Table.TableEntry> actual) {
            this.actual = actual;
        }

        public List<Curriculum> getParsedData() {
            return parsedData;
        }

        public void setParsedData(List<Curriculum> parsedData) {
            this.parsedData = parsedData;
        }

        public void setDifference(Collection<Table.TableEntry> difference) {
            this.difference = difference;
        }

        public boolean isMissingDifference() {
            return missingDifference;
        }

        public void setMissingDifference(boolean missingDifference) {
            this.missingDifference = missingDifference;
        }


        @Override
        public String toString() {
            return "\nViolation{" +
                    "\nviolationDate=" + violationDate +
                    ", \nexpected=" + expected +
                    ", \nactual=" + actual +
                    ", \ndifference=" + difference +
                    ", \nmissingDifference=" + missingDifference +
                    ", \nparsedData=" + parsedData +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ValidatorReport{" +
                "\ncontent='" + content + '\'' +
                ", \nerrors=" + errors +
                ", \ndaysMap=" + daysMap +
                '}';
    }
}
