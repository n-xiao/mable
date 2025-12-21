package code.frontend.misc;

import java.time.Instant;

import javax.swing.JFrame;

import code.backend.Countdown;
import code.frontend.panels.CountdownPanel;

public class AppWindow extends JFrame { // singleton class

    private static AppWindow window = null;

    private AppWindow() {
        // mostly boilerplate
        this.setLayout(null);
        this.setTitle("Mable");
        this.setResizable(false); // prevents window from being resized
        this.getContentPane().setBackground(DefaultValues.BACKGROUND_COLOUR);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1120,730); // sets window size
        this.setLocationRelativeTo(null);
        this.addUIelements(); // inits frontend elements
        this.setVisible(true);
    }

    public static AppWindow getInstance() {
        if (AppWindow.window == null)
            AppWindow.window = new AppWindow();
        return AppWindow.window;
    }

    private void addUIelements() {
        Countdown c = Countdown.create("test", 20, 12, 2025);
        CountdownPanel panel = new CountdownPanel(c,Instant.now());
        this.add(panel);
        panel.setLocation(10, 10);
        panel.setVisible(true);
    }

}
