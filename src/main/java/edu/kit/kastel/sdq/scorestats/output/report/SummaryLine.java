package edu.kit.kastel.sdq.scorestats.output.report;

import edu.kit.kastel.sdq.scorestats.cli.ReportOutput;
import edu.kit.kastel.sdq.scorestats.output.Output;
import edu.kit.kastel.sdq.scorestats.output.layout.RightPad;
import edu.kit.kastel.sdq.scorestats.output.layout.Text;
import edu.kit.kastel.sdq.scorestats.output.ratio.RatioRow;

public class SummaryLine implements Output {

    private final String label;
    private final RatioRow ratioRow;
    private final int indentationLevel;

    public SummaryLine(String label, int indentationLevel, RatioRow ratioRow) {
        this.label = label;
        this.ratioRow = ratioRow;
        this.indentationLevel = indentationLevel;
    }

    @Override
    public String print() {
        RightPad padding = new RightPad(
                this.indentationLevel,
                ReportOutput.COLUMN_WIDTH,
                new Text(this.label));

        return padding.print() + this.ratioRow.print() + System.lineSeparator();
    }
}
