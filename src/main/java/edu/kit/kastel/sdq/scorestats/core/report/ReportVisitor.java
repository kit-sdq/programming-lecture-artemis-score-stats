/* Licensed under EPL-2.0 2023-2024. */
package edu.kit.kastel.sdq.scorestats.core.report;

import edu.kit.kastel.sdq.scorestats.core.assessment.Assessment;

/**
 * A generic report visitor.
 *
 * @param <K> see {@link Assessment}
 * @param <T> the element {@link #visit} will be called with
 *
 * @author Moritz Hertler
 * @version 1.0
 */
public interface ReportVisitor<K, T> {
    T visit(Report.ReportData<K> data);
}
