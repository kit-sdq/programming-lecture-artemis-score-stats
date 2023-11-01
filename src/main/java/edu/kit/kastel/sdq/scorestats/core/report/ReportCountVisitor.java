package edu.kit.kastel.sdq.scorestats.core.report;

public interface ReportCountVisitor<K, T> {
    Iterable<T> iterable(Report.ReportData<K> data);

    boolean count(T value);
}
