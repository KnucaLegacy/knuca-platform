package com.theopus.upload.handling;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.theopus.parser.obj.Table;
import com.theopus.parser.obj.ValidatorReport;
import com.theopus.upload.entity.Report;
import com.theopus.upload.entity.UploadFile;
import com.theopus.upload.entity.Violation;
import com.theopus.upload.entity.ViolationEntry;
import com.theopus.upload.repository.ReportRepo;

public class ReportService {

    private final ReportRepo reportRepo;

    public ReportService(ReportRepo reportRepo) {
        this.reportRepo = reportRepo;
    }

    public Report reportSheet(ValidatorReport reportOfCurrentSheet, UploadFile file, int currentSheet, String anchor) {
        Report report = new Report();
        report.setSheetNumber(currentSheet);
        report.setAnchor(anchor);
        report.setFile(file);
        report.setContent(reportOfCurrentSheet.getContent());
        report.setDaysMap(reportOfCurrentSheet.getDaysMap());
        report.setReportDate(LocalDate.now());
        Set<Violation> violations = convertViolation(reportOfCurrentSheet.getErrors(), report);
        report.setErrors(violations);
        return reportRepo.save(report);
    }

    private Set<Violation> convertViolation(Set<ValidatorReport.Violation> errors, Report report) {
        return errors.stream().map(violation -> {
            Violation v = new Violation();
            v.setActual(toViolationEntry(violation.getActual()));
            v.setExpected(toViolationEntry(violation.getExpected()));
            v.setViolationDate(violation.getViolationDate());
            v.setReport(report);
            return v;
        }).collect(Collectors.toSet());
    }

    private List<ViolationEntry> toViolationEntry(Set<Table.TableEntry> expected) {
        return expected.stream().map(tableEntry -> {
            ViolationEntry violationEntry = new ViolationEntry();
            violationEntry.setLessonOrder(tableEntry.getLessonOrder());
            violationEntry.setLessonType(tableEntry.getLessonType());
            return violationEntry;
        }).collect(Collectors.toList());
    }

    public List<Report> getAll() {
        List<Report> all = reportRepo.findAll();
        all.forEach(report -> report.getErrors().stream()
                .peek(violation -> violation.getExpected().sort(ViolationEntry::compareTo))
                .forEach(violation -> violation.getActual().sort(ViolationEntry::compareTo)));
        return all;
    }

    public Report get(Long reportId) {
        return reportRepo.getOne(reportId);
    }
}
