package edu.kit.kastel.sdq.scorestats.core.report;

public interface ReportAverageVisitor<K, T> {
    Iterable<T> iterable(Report.ReportData<K> data);

    double summand(T value, Report.ReportData<K> data);

    double n(Report.ReportData<K> data);
}
