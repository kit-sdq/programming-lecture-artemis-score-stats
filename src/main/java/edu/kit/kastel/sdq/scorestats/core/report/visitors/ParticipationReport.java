package edu.kit.kastel.sdq.scorestats.core.report.visitors;

import java.util.List;
import java.util.Set;

import edu.kit.kastel.sdq.artemis4j.api.artemis.Course;
import edu.kit.kastel.sdq.scorestats.core.Ratio;
import edu.kit.kastel.sdq.scorestats.core.assessment.Assessments;
import edu.kit.kastel.sdq.scorestats.core.report.Report.ReportData;
import edu.kit.kastel.sdq.scorestats.core.report.ReportVisitor;

public class ParticipationReport<K> implements ReportVisitor<K, Ratio> {

    @Override
    public Ratio visit(ReportData<K> data) {
        Assessments<K> assessments = data.assessments();
        Course course = data.course();

        if (data.students() == null || data.students().isEmpty()) {
            return new Ratio(
                    assessments.assessments().size() + assessments.skippedStudents().size(),
                    // ! teaching assistants are also in the student group
                    course.getNumberOfStudents() - course.getNumberOfTeachingAssistants());
        } else {
            return new Ratio(
                    data.selectedAssessments().size()
                            + getSkippedStudentCount(assessments.skippedStudents(), data.students()),
                    data.students().size());
        }
    }

    private int getSkippedStudentCount(List<String> skippedStudents, Set<String> students) {
        int count = 0;
        for (String skippedStudent : skippedStudents) {
            if (students.contains(skippedStudent)) {
                count++;
            }
        }
        return count;
    }
}
