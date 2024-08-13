/* Licensed under EPL-2.0 2023-2024. */
package edu.kit.kastel.sdq.scorestats.core.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.kit.kastel.sdq.artemis4j.grading.Assessment;
import edu.kit.kastel.sdq.artemis4j.grading.Course;
import edu.kit.kastel.sdq.artemis4j.grading.ProgrammingExercise;
import edu.kit.kastel.sdq.artemis4j.grading.penalty.GradingConfig;
import edu.kit.kastel.sdq.scorestats.core.Ratio;
import edu.kit.kastel.sdq.scorestats.core.assessment.Assessments;

/**
 * A group of {@link Assessment assessments} that can be statistically queried.
 *
 * @author Moritz Hertler
 * @version 1.0
 */
public class Report {
    private final ReportData data;

    /**
     * Creates a new report of all the given assessments.
     *
     * @param course
     * @param exercise
     * @param config
     * @param assessments
     */
    public Report(Course course, ProgrammingExercise exercise, GradingConfig config, Assessments assessments) {
        this(course, exercise, config, assessments, null);
    }

    /**
     * Creates a new report of the assessments of the given students.
     *
     * @param course
     * @param exercise
     * @param config
     * @param assessments
     * @param students
     */
    public Report(Course course, ProgrammingExercise exercise, GradingConfig config, Assessments assessments, Set<String> students) {
        this.data = new ReportData(course, exercise, config, assessments, getSelectedAssessments(assessments, students), students);
    }

    private List<Assessment> getSelectedAssessments(Assessments assessments, Set<String> students) {
        if (students == null || students.isEmpty()) {
            return new ArrayList<>(assessments.assessments().values());
        }

        Map<String, Assessment> assessmentsMap = assessments.assessments();

        List<Assessment> selectedAssessments = new ArrayList<>(students.size());
        for (String studentId : students) {
            Assessment assessment = assessmentsMap.get(studentId);
            if (assessment != null) {
                selectedAssessments.add(assessment);
            }
        }
        return selectedAssessments;
    }

    /**
     * Accepts a {@link ReportVisitor} and returns the visitors result based on this
     * report.
     *
     * @param <T>     the type of the return value of the {@code visitor}
     * @param visitor the visitor
     * @return the result based on this report and the {@code visitor}
     */
    public <T> T accept(ReportVisitor<T> visitor) {
        return visitor.visit(this.data);
    }

    /**
     * Generates and returns a list based on this report.
     *
     * @param <T>     see {@link ReportListVisitor}
     * @param <U>     see {@link ReportListVisitor}
     * @param visitor the visitor
     * @return the list based on this report and the {@code visitor}
     */
    public <T, U> List<U> list(ReportListVisitor<T, ? extends U> visitor) {
        List<U> result = new ArrayList<>();
        for (T value : visitor.iterable(this.data)) {
            result.addAll(visitor.list(value));
        }
        return result;
    }

    /**
     * Create and returns a count result based on this report.
     *
     * @param <T>     see {@link ReportCountVisitor}
     * @param visitor the visitor
     * @return the count based on this report and the {@code visitor}
     */
    public <T> Ratio count(ReportCountVisitor<T> visitor) {
        int count = 0;
        int size = 0;
        for (T value : visitor.iterable(this.data)) {
            size++;
            if (visitor.count(value)) {
                count++;
            }
        }
        return new Ratio(count, size);
    }

    /**
     * Calculates and returns an arithmetic mean based on this report.
     *
     * @param <T>     see {@link ReportAverageVisitor}
     * @param visitor the visitor
     * @return the average based on this report and the {@code visitor}
     */
    public <T> Ratio average(ReportAverageVisitor<T> visitor) {
        int i = 0;
        double sum = 0;
        for (T value : visitor.iterable(this.data)) {
            sum += visitor.summand(value, this.data);
            i++;
        }
        return new Ratio(sum / i, visitor.max(this.data));
    }

    /**
     * Calculates and returns a frequency of occurrence based on this report.
     *
     * @param <T>     see {@link ReportFrequencyVisitor}
     * @param <U>     see {@link ReportFrequencyVisitor}
     * @param visitor the visitor
     * @return the frequency based on this report and the {@code visitor}
     */
    public <T, U> FrequencyResult<U> frequency(ReportFrequencyVisitor<T, U> visitor) {
        Map<U, Integer> frequency = new HashMap<>();

        for (T value : visitor.iterable(this.data)) {
            Collection<U> counted = visitor.count(value);
            for (U countedValue : counted) {
                frequency.merge(countedValue, 1, Integer::sum);
            }
        }

        return new FrequencyResult<>(frequency, visitor.max(this.data));
    }

    public record ReportData(Course course, ProgrammingExercise exercise, GradingConfig config, Assessments assessments, List<Assessment> selectedAssessments,
            Set<String> students) {
    }

    public record FrequencyResult<U>(Map<U, Integer> values, int n) {
    }
}
