/* Licensed under EPL-2.0 2023-2024. */
package edu.kit.kastel.sdq.scorestats.core.report.visitors;

import java.util.ArrayList;
import java.util.List;

import edu.kit.kastel.sdq.artemis4j.grading.Annotation;
import edu.kit.kastel.sdq.artemis4j.grading.Assessment;
import edu.kit.kastel.sdq.scorestats.core.report.Report.ReportData;
import edu.kit.kastel.sdq.scorestats.core.report.ReportListVisitor;

/**
 * A report visitor collecting a list of all custom penalties.
 *
 * @author Moritz Hertler
 * @version 1.0
 */
public class CustomPenaltyAnnotationList implements ReportListVisitor<Assessment, Annotation> {

    @Override
    public Iterable<Assessment> iterable(ReportData data) {
        return data.selectedAssessments();
    }

    @Override
    public List<Annotation> list(Assessment value) {
        List<Annotation> annotations = new ArrayList<>();
        for (Annotation annotation : value.getAnnotations()) {
            if (annotation.getMistakeType().isCustomAnnotation()) {
                annotations.add(annotation);
            }
        }
        return annotations;
    }
}
