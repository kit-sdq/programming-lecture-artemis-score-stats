package edu.kit.kastel.sdq.scorestats.core.report;

import java.util.Collection;

public interface ReportFrequencyVisitor<K, T, U> {
    Iterable<T> iterable(Report.ReportData<K> data);

    Collection<U> count(T value);

    int n(Report.ReportData<K> data);
}
