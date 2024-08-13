/* Licensed under EPL-2.0 2023-2024. */
package edu.kit.kastel.sdq.scorestats.core.report;

/**
 * A report visitor describing a count.
 *
 * @param <T> the element {@link #count} will be called with
 *
 * @author Moritz Hertler
 * @version 1.0
 */
public interface ReportCountVisitor<T> {
    Iterable<T> iterable(Report.ReportData data);

    /**
     * Returns {@code true} if the current {@code value} should be counted.
     *
     * @return {@code true} if the current {@code value} should be counted
     */
    boolean count(T value);
}
