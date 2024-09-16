/* Licensed under EPL-2.0 2023-2024. */
package edu.kit.kastel.sdq.scorestats.core.report.visitors;

import java.util.Collection;

import edu.kit.kastel.sdq.artemis4j.grading.Course;
import edu.kit.kastel.sdq.scorestats.core.Ratio;
import edu.kit.kastel.sdq.scorestats.core.assessment.Assessments;
import edu.kit.kastel.sdq.scorestats.core.report.Report.ReportData;
import edu.kit.kastel.sdq.scorestats.core.report.ReportVisitor;

/**
 * A report visitor calculating the participation.
 *
 * @author Moritz Hertler
 * @version 1.0
 */
public class ParticipationReport implements ReportVisitor<Ratio> {

    @Override
    public Ratio visit(ReportData data) {
        Assessments assessments = data.assessments();
        Course course = data.course();

        if (data.students() == null || data.students().isEmpty()) {
            return new Ratio(assessments.assessments().size() + assessments.skippedStudents().size(), course.getNumberOfStudents());
        } else {
            return new Ratio(data.selectedAssessments().size() + getSkippedStudentCount(assessments.skippedStudents(), data.students()),
                    data.students().size());
        }
    }

    private int getSkippedStudentCount(Iterable<String> skippedStudents, Collection<String> students) {
        int count = 0;
        for (String skippedStudent : skippedStudents) {
            if (students.contains(skippedStudent)) {
                count++;
            }
        }
        return count;
    }
}
