package edu.kit.kastel.sdq.scorestats.core.report.visitors;

import edu.kit.kastel.sdq.scorestats.core.assessment.Assessment;
import edu.kit.kastel.sdq.scorestats.core.report.Report.ReportData;
import edu.kit.kastel.sdq.scorestats.core.report.ReportAverageVisitor;

public class ScoreAverage<K> implements ReportAverageVisitor<K, Assessment<K>> {

    @Override
    public Iterable<Assessment<K>> iterable(ReportData<K> data) {
        return data.selectedAssessments();
    }

    @Override
    public double summand(Assessment<K> value, ReportData<K> data) {
        return (value.getScore() / 100) * data.exercise().getMaxPoints();
    }

    @Override
    public double n(ReportData<K> data) {
        return data.exercise().getMaxPoints();
    }

}
