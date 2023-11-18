/* Licensed under EPL-2.0 2023. */
package edu.kit.kastel.sdq.scorestats.output.report;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import edu.kit.kastel.sdq.artemis4j.api.grading.IAnnotation;
import edu.kit.kastel.sdq.scorestats.output.Output;

/**
 * @author Moritz Hertler
 * @version 1.0
 */
public class CustomPenaltyList implements Output {

    private static final String FORMAT = "%.1fP %s";

    private final List<IAnnotation> annotations;
    private final int itemsCount;
    private final int indentationLevel;

    public CustomPenaltyList(int indentationLevel, List<IAnnotation> annotations) {
        this.annotations = annotations;
        this.itemsCount = annotations.size();
        this.indentationLevel = indentationLevel;
    }

    public CustomPenaltyList(int indentationLevel, List<IAnnotation> annotations, int itemsCount) {
        this.annotations = annotations;
        this.itemsCount = itemsCount;
        this.indentationLevel = indentationLevel;
    }

    @Override
    public String print() {
        Collections.sort(this.annotations,
                Comparator
                        .comparing((IAnnotation a) -> a.getCustomPenalty().get())
                        .thenComparing((IAnnotation a) -> a.getCustomMessage().get()));

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.itemsCount && i < this.annotations.size(); i++) {
            IAnnotation annotation = this.annotations.get(i);
            Output.indent(builder, this.indentationLevel);
            builder.append(String.format(Locale.US, FORMAT, annotation.getCustomPenalty().get(),
                    annotation.getCustomMessage().get()));
            builder.append(System.lineSeparator());
        }
        return builder.toString();
    }

}
