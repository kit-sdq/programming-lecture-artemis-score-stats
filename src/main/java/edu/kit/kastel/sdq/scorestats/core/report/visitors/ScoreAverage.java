/* Licensed under EPL-2.0 2023. */
package edu.kit.kastel.sdq.scorestats.core.report.visitors;

import edu.kit.kastel.sdq.scorestats.core.assessment.Assessment;
import edu.kit.kastel.sdq.scorestats.core.report.Report.ReportData;
import edu.kit.kastel.sdq.scorestats.core.report.ReportAverageVisitor;

/**
 * A report visitor calculating the average score.
 * 
 * @param <K> see {@link Assessment}
 * @author Moritz Hertler
 * @version 1.0
 */
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
    public double max(ReportData<K> data) {
        return data.exercise().getMaxPoints();
    }

}
