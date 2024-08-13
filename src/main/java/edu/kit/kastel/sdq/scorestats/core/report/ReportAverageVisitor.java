/* Licensed under EPL-2.0 2023-2024. */
package edu.kit.kastel.sdq.scorestats.core.report;

/**
 * A report visitor describing an arithmetic mean.
 *
 * @param <T> the element {@link #summand} will be called with
 *
 * @author Moritz Hertler
 * @version 1.0
 */
public interface ReportAverageVisitor<T> {
    Iterable<T> iterable(Report.ReportData data);

    /**
     * The summand added to the current average based on the current {@code value}.
     *
     * @return the summand
     */
    double summand(T value, Report.ReportData data);

    /**
     * The maximum value the average could be.
     *
     * @param data the data
     * @return the maximum value
     */
    double max(Report.ReportData data);
}
