package edu.kit.kastel.sdq.scorestats.core.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.kit.kastel.sdq.artemis4j.api.artemis.Course;
import edu.kit.kastel.sdq.artemis4j.api.artemis.Exercise;
import edu.kit.kastel.sdq.artemis4j.grading.config.ExerciseConfig;
import edu.kit.kastel.sdq.scorestats.core.Ratio;
import edu.kit.kastel.sdq.scorestats.core.assessment.Assessment;
import edu.kit.kastel.sdq.scorestats.core.assessment.Assessments;

public class Report<K> {
    private ReportData<K> data;

    public Report(
            Course course,
            Exercise exercise,
            ExerciseConfig config,
            Assessments<K> assessments) {
        this(course, exercise, config, assessments, null);
    }

    public Report(
            Course course,
            Exercise exercise,
            ExerciseConfig config,
            Assessments<K> assessments,
            Set<String> students) {
        this.data = new ReportData<>(
                course,
                exercise,
                config,
                assessments,
                getSelectedAssessments(assessments, students),
                students);
    }

    public <T> T accept(ReportVisitor<K, T> visitor) {
        return visitor.visit(this.data);
    }

    public <T, U> List<U> list(ReportListVisitor<K, T, U> visitor) {
        List<U> result = new ArrayList<>();
        for (T value : visitor.iterable(this.data)) {
            result.addAll(visitor.list(value));
        }
        return result;
    }

    public <T> Ratio count(ReportCountVisitor<K, T> visitor) {
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

    public <T> Ratio average(ReportAverageVisitor<K, T> visitor) {
        int i = 0;
        double sum = 0;
        for (T value : visitor.iterable(this.data)) {
            sum += visitor.summand(value, this.data);
            i++;
        }
        return new Ratio(sum / i, visitor.n(this.data));
    }

    public <T, U> FrequencyResult<U> frequency(ReportFrequencyVisitor<K, T, U> visitor) {
        Map<U, Integer> frequency = new HashMap<>();

        for (T value : visitor.iterable(this.data)) {
            Collection<U> counted = visitor.count(value);
            for (U countedValue : counted) {
                Integer count = frequency.get(countedValue);
                if (count == null) {
                    frequency.put(countedValue, 1);
                } else {
                    frequency.put(countedValue, count + 1);
                }
            }
        }

        return new FrequencyResult<>(frequency, visitor.n(this.data));
    }

    private List<Assessment<K>> getSelectedAssessments(Assessments<K> assessments, Set<String> students) {
        if (students == null || students.isEmpty()) {
            return new ArrayList<>(assessments.assessments().values());
        }

        Map<String, Assessment<K>> assessmentsMap = assessments.assessments();

        List<Assessment<K>> selectedAssessments = new ArrayList<>(students.size());
        for (String studentId : students) {
            Assessment<K> assessment = assessmentsMap.get(studentId);
            if (assessment != null) {
                selectedAssessments.add(assessment);
            }
        }
        return selectedAssessments;
    }

    public record ReportData<K>(
            Course course,
            Exercise exercise,
            ExerciseConfig config,
            Assessments<K> assessments,
            List<Assessment<K>> selectedAssessments,
            Set<String> students) {
    }

    public record FrequencyResult<U>(
            Map<U, Integer> values,
            int n) {
    }
}
