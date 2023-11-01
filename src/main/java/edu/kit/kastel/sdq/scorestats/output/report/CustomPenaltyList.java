package edu.kit.kastel.sdq.scorestats.output.report;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import edu.kit.kastel.sdq.artemis4j.api.grading.IAnnotation;
import edu.kit.kastel.sdq.scorestats.output.Output;

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
        Collections.sort(this.annotations, new Comparator<IAnnotation>() {
            @Override
            public int compare(IAnnotation o1, IAnnotation o2) {
                int compare = Double.compare(o1.getCustomPenalty().get(), o2.getCustomPenalty().get());
                if (compare == 0) {
                    compare = o1.getCustomMessage().get().compareTo(o2.getCustomMessage().get());
                }
                return compare;
            };
        });

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
