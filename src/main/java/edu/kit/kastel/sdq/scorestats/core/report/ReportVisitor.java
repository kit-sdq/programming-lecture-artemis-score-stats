package edu.kit.kastel.sdq.scorestats.core.report;

public interface ReportVisitor<K, T> {
    T visit(Report.ReportData<K> data);
}
