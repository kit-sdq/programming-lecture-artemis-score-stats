/* Licensed under EPL-2.0 2023-2024. */
package edu.kit.kastel.sdq.scorestats.core.report.visitors;

import java.util.ArrayList;
import java.util.List;

import edu.kit.kastel.sdq.artemis4j.api.grading.IAnnotation;
import edu.kit.kastel.sdq.scorestats.core.assessment.Assessment;
import edu.kit.kastel.sdq.scorestats.core.report.Report.ReportData;
import edu.kit.kastel.sdq.scorestats.core.report.ReportListVisitor;

/**
 * A report visitor collecting a list of all custom penalties.
 *
 * @param <K> see {@link Assessment}
 * @author Moritz Hertler
 * @version 1.0
 */
public class CustomPenaltyAnnotationList<K> implements ReportListVisitor<K, Assessment<K>, IAnnotation> {

    @Override
    public Iterable<Assessment<K>> iterable(ReportData<K> data) {
        return data.selectedAssessments();
    }

    @Override
    public List<IAnnotation> list(Assessment<K> value) {
        List<IAnnotation> annotations = new ArrayList<>();
        for (IAnnotation annotation : value.getAnnotations()) {
            if (annotation.getMistakeType().isCustomPenalty()) {
                annotations.add(annotation);
            }
        }
        return annotations;
    }

}
