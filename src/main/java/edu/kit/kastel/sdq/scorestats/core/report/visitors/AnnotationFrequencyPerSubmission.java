package edu.kit.kastel.sdq.scorestats.core.report.visitors;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import edu.kit.kastel.sdq.artemis4j.api.grading.IAnnotation;
import edu.kit.kastel.sdq.artemis4j.api.grading.IMistakeType;
import edu.kit.kastel.sdq.scorestats.core.report.Report.ReportData;
import edu.kit.kastel.sdq.scorestats.core.assessment.Assessment;
import edu.kit.kastel.sdq.scorestats.core.report.ReportFrequencyVisitor;

public class AnnotationFrequencyPerSubmission<K> implements ReportFrequencyVisitor<K, Assessment<K>, IMistakeType> {

    @Override
    public Iterable<Assessment<K>> iterable(ReportData<K> data) {
        return data.selectedAssessments();
    }

    @Override
    public Collection<IMistakeType> count(Assessment<K> value) {
        Set<IMistakeType> mistakeTypes = new HashSet<>();
        for (IAnnotation annotation : value.getAnnotations()) {
            mistakeTypes.add(annotation.getMistakeType());
        }
        return mistakeTypes;
    }

    @Override
    public int n(ReportData<K> data) {
        return data.selectedAssessments().size();
    }
}
