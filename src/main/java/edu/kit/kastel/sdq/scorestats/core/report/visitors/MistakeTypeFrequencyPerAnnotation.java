/* Licensed under EPL-2.0 2023-2024. */
package edu.kit.kastel.sdq.scorestats.core.report.visitors;

import java.util.Collection;

import edu.kit.kastel.sdq.artemis4j.api.grading.IAnnotation;
import edu.kit.kastel.sdq.artemis4j.api.grading.IMistakeType;
import edu.kit.kastel.sdq.scorestats.core.assessment.Assessment;
import edu.kit.kastel.sdq.scorestats.core.report.Report.ReportData;
import edu.kit.kastel.sdq.scorestats.core.report.ReportFrequencyVisitor;

/**
 * A report visitor calculating the frequency of {@link IMistakeType mistake
 * types} over all annotations where each annotation is counted.
 *
 * @param <K> see {@link Assessment}
 *
 * @author Moritz Hertler
 * @version 1.0
 */
public class MistakeTypeFrequencyPerAnnotation<K> implements ReportFrequencyVisitor<K, Assessment<K>, IMistakeType> {

    @Override
    public Iterable<Assessment<K>> iterable(ReportData<K> data) {
        return data.selectedAssessments();
    }

    @Override
    public Collection<IMistakeType> count(Assessment<K> value) {
        return value.getAnnotations().stream().map(IAnnotation::getMistakeType).toList();
    }

    @Override
    public int max(ReportData<K> data) {
        int sum = 0;
        for (Assessment<K> assessment : data.selectedAssessments()) {
            sum += assessment.getAnnotations().size();
        }
        return sum;
    }

}
