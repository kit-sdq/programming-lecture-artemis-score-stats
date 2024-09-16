/* Licensed under EPL-2.0 2023-2024. */
package edu.kit.kastel.sdq.scorestats.core.report.visitors;

import java.util.Collection;

import edu.kit.kastel.sdq.artemis4j.grading.Annotation;
import edu.kit.kastel.sdq.artemis4j.grading.Assessment;
import edu.kit.kastel.sdq.artemis4j.grading.penalty.MistakeType;
import edu.kit.kastel.sdq.scorestats.core.report.Report.ReportData;
import edu.kit.kastel.sdq.scorestats.core.report.ReportFrequencyVisitor;

/**
 * A report visitor calculating the frequency of {@link MistakeType mistake
 * types} over all annotations where each annotation is counted.
 *
 * @author Moritz Hertler
 * @version 1.0
 */
public class MistakeTypeFrequencyPerAnnotation implements ReportFrequencyVisitor<Assessment, MistakeType> {

    @Override
    public Iterable<Assessment> iterable(ReportData data) {
        return data.selectedAssessments();
    }

    @Override
    public Collection<MistakeType> count(Assessment value) {
        return value.getAnnotations().stream().map(Annotation::getMistakeType).toList();
    }

    @Override
    public int max(ReportData data) {
        int sum = 0;
        for (Assessment assessment : data.selectedAssessments()) {
            sum += assessment.getAnnotations().size();
        }
        return sum;
    }

}
