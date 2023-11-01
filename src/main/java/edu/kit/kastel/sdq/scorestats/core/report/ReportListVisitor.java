package edu.kit.kastel.sdq.scorestats.core.report;

import java.util.List;

public interface ReportListVisitor<K, T, U> {
    Iterable<T> iterable(Report.ReportData<K> data);

    List<U> list(T value);
}
