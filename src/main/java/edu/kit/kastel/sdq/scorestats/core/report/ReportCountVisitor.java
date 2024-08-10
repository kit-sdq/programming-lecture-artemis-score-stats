/* Licensed under EPL-2.0 2023-2024. */
package edu.kit.kastel.sdq.scorestats.core.report;

import edu.kit.kastel.sdq.scorestats.core.assessment.Assessment;

/**
 * A report visitor describing a count.
 *
 * @param <K> see {@link Assessment}
 * @param <T> the element {@link #count} will be called with
 *
 * @author Moritz Hertler
 * @version 1.0
 */
public interface ReportCountVisitor<K, T> {
    Iterable<T> iterable(Report.ReportData<K> data);

    /**
     * Returns {@code true} if the current {@code value} should be counted.
     *
     * @return {@code true} if the current {@code value} should be counted
     */
    boolean count(T value);
}
