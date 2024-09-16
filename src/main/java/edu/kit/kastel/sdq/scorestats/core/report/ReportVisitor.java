/* Licensed under EPL-2.0 2023-2024. */
package edu.kit.kastel.sdq.scorestats.core.report;

/**
 * A generic report visitor.
 *
 * @param <T> the element {@link #visit} will be called with
 *
 * @author Moritz Hertler
 * @version 1.0
 */
@FunctionalInterface
public interface ReportVisitor<T> {
    T visit(Report.ReportData data);
}
