package code.frontend.panels;

import java.awt.Color;
import java.awt.Font;
import java.time.Instant;

import javax.swing.JLabel;
import javax.swing.JPanel;

import code.backend.Countdown;
import code.frontend.foundation.CustomBox;
import code.frontend.misc.DefaultValues;

/*
 * This class is the JPanel for a single Countdown.
 * The size of the panel should be constant, but the number
 * of panels displayed per row/column or both will change
 * depending on window size.
 */
public class CountdownPanel extends JPanel {
    public static final int PADDING = 5; // total spacing = 10
    private static final int HEIGHT = 180;
    private static final int WIDTH = 110;

    public CountdownPanel(Countdown c, Instant now) {
        assert c != null;
        this.setLayout(null);
        this.setBackground(DefaultValues.BACKGROUND_COLOUR);
        this.setSize(WIDTH, HEIGHT);

        addBorder(this);
        addNumberOfDays(c, now, this);
    }

    private static void addBorder(CountdownPanel cdnPanel) {
        CustomBox border = new CustomBox(3);
        cdnPanel.add(border);
        border.setBounds(0, 0, WIDTH, HEIGHT - 30);
        border.setVisible(true);
    }

    private static void addNumberOfDays(Countdown c, Instant now, CountdownPanel cdnPanel) {
        int daysTillDue = Math.abs(c.daysUntilDue(now));
        String daysString = Integer.toString(daysTillDue);
        JLabel daysRemain = new JLabel(daysString, JLabel.CENTER);
        daysRemain.setFont(new Font(DefaultValues.getFontFamily(), Font.PLAIN, 40));
        cdnPanel.add(daysRemain);
        daysRemain.setLocation(0, -30);
        daysRemain.setSize(WIDTH, HEIGHT - 20);
        daysRemain.setForeground(Color.WHITE);

        String noun = (daysTillDue != 1) ? "DAYS" : "DAY";
        String adverb = (c.isOverdue(now)) ? "AGO" : "LEFT";
        JLabel nounLabel = new JLabel(noun, JLabel.CENTER);
        JLabel adverbLabel = new JLabel(adverb, JLabel.CENTER);
        Font labelFont = new Font(DefaultValues.getFontFamily(), Font.BOLD, 16);
        nounLabel.setFont(labelFont);
        adverbLabel.setFont(labelFont);
        int nounY = HEIGHT - 100; // defines y pos just below daysRemain
        int adverbY = HEIGHT - 79; // defines y pos just below nounLabel
        cdnPanel.add(nounLabel);
        cdnPanel.add(adverbLabel);
        nounLabel.setBounds(0, nounY, WIDTH, 20);
        adverbLabel.setBounds(0, adverbY, WIDTH, 20);
        nounLabel.setForeground(Color.WHITE);
        adverbLabel.setForeground(Color.WHITE);
    }

    /*
     * Adds the end date and title under the border
     */
    private static void addMiscLabels(CountdownPanel cdnPanel) {
    }
}
