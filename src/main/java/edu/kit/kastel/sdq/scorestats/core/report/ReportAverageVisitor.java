/* Licensed under EPL-2.0 2023-2024. */
package edu.kit.kastel.sdq.scorestats.core.report;

import edu.kit.kastel.sdq.scorestats.core.assessment.Assessment;

/**
 * A report visitor describing an arithmetic mean.
 *
 * @param <K> see {@link Assessment}
 * @param <T> the element {@link #summand} will be called with
 *
 * @author Moritz Hertler
 * @version 1.0
 */
public interface ReportAverageVisitor<K, T> {
    Iterable<T> iterable(Report.ReportData<K> data);

    /**
     * The summand added to the current average based on the current {@code value}.
     *
     * @return the summand
     */
    double summand(T value, Report.ReportData<K> data);

    /**
     * The maximum value the average could be.
     *
     * @param data the data
     * @return the maximum value
     */
    double max(Report.ReportData<K> data);
}
