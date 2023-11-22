/* Licensed under EPL-2.0 2023. */
package edu.kit.kastel.sdq.scorestats.core.report;

import java.util.List;

import edu.kit.kastel.sdq.scorestats.core.assessment.Assessment;

/**
 * A report visitor describing a list.
 * 
 * @param <K> see {@link Assessment}
 * @param <T> the element {@link #list} will be called with
 * @param <U> the elements {@link #list} returns
 * 
 * @author Moritz Hertler
 * @version 1.0
 */
public interface ReportListVisitor<K, T, U> {
	Iterable<T> iterable(Report.ReportData<K> data);

	/**
	 * Returns the elements based on the current {@code value} to add to the list.
	 *
	 * @return the elements to add to the list
	 */
	List<U> list(T value);
}
