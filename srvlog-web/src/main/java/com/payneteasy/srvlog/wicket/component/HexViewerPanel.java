package com.payneteasy.srvlog.wicket.component;

import java.io.Serializable;
import static java.lang.Integer.min;
import static java.lang.String.format;
import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * This panel displays given data in hex and ascii forms.
 *
 * @author imenem
 */
public class HexViewerPanel extends Panel {

    /**
     * Line width measured in chars.
     */
    private static final int LINE_WIDTH = 32;

    /**
     * Set of char codes for conversation from ascii to hex.
     */
    private static final char[] HEX_CODES = "0123456789ABCDEF".toCharArray();

    public HexViewerPanel(String id, String data) {
        super(id, new Model<>(data));

        add(new ListView<HexViewerLine>("hex-viewer-lines", getLines(data)) {

            @Override
            protected void populateItem(ListItem<HexViewerLine> item) {
                HexViewerLine line = item.getModelObject();

                item.add(new Label("line-offset", line.lineOffset));
                item.add(new Label("line-in-hex", line.lineInHex));
                item.add(new Label("line-in-ascii", line.lineInAscii));
            }
        });

    }

    /**
     * Converts input data to list of lines for displaying in viewer.
     *
     * @param       data        Input data.
     *
     * @return      List of lines.
     */
    private List<HexViewerLine> getLines(String data) {
        List<HexViewerLine> lines = new ArrayList<>();

        for (int lineStart = 0, lineEnd = LINE_WIDTH;
             lineStart < data.length();
             lineStart += LINE_WIDTH, lineEnd += LINE_WIDTH
        ) {
            String line = data.substring(lineStart, min(lineEnd, data.length()));

            String lineOffset = formatLineOffset(lineStart);
            String lineInHex = formatLineInHex(line);
            String lineInAscii = formatLineInAscii(line);

            lines.add(new HexViewerLine(lineOffset, lineInHex, lineInAscii));
        }

        return lines;
    }

    /**
     * Formats line offset.
     *
     * @param       lineOffset      Line offset.
     *
     * @return      Formatted line offset.
     */
    private String formatLineOffset(int lineOffset) {
        return format("0x%07X", lineOffset);
    }

    /**
     * Converts line from ascii to hex and formats it.
     *
     * @param       line        Line to convert and format.
     *
     * @return      Converted and formatted line.
     */
    private String formatLineInHex(String line) {
        char[] chars = line.toCharArray();
        int length = (LINE_WIDTH * 3) + 4;
        StringBuilder hex = new StringBuilder(length);

        for (int i = 0; i < chars.length; i++)
        {
            if (i > 0 && i < chars.length && i % 8 == 0) {
                hex.append("  ");
            }

            hex.append(HEX_CODES[(chars[i] >> 4) & 0xF]);
            hex.append(HEX_CODES[(chars[i] & 0xF)]);
            hex.append(" ");
        }

        return hex.toString().toUpperCase();
    }

    /**
     * Removes non-ASCII characters from line.
     *
     * @param       line        Line to clean.
     *
     * @return      Line that cleaned from non-ASCII characters.
     */
    private String formatLineInAscii(String line) {
        return line.replaceAll("[\000-\040\177-\377]", ".");
    }

    /**
     * Data transfer object for parts of formatted hex viewer string.
     */
    private static class HexViewerLine implements Serializable {

        /**
         * Line offset.
         */
        public final String lineOffset;

        /**
         * Line in hex.
         */
        public final String lineInHex;

        /**
         * Line in ASCII.
         */
        public final String lineInAscii;

        HexViewerLine(String lineOffset, String lineInHex, String lineInAscii) {
            this.lineOffset = lineOffset;
            this.lineInHex = lineInHex;
            this.lineInAscii = lineInAscii;
        }
    }
}
