package code.frontend.panels;

import code.backend.Countdown;
import code.backend.StorageHandler;
import code.frontend.foundation.CustomBox;
import code.frontend.foundation.CustomLine;
import code.frontend.gui.RightClickMenu;
import code.frontend.misc.Vals;
import code.frontend.misc.Vals.Colour;
import code.frontend.misc.Vals.GraphicalUI;
import code.frontend.windows.EditWindow;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

/**
 * This is the scrollable Pane that is responsible for displaying {@link CountdownPane}.
 * An instance of this class should be added to a {@link Content} instance.
 */
public class CountdownPaneView extends ScrollPane {
    public enum ButtonMode { NO_SELECT, SINGLE_SELECT, MULTI_SELECT }
    public enum DisplayOrder { ASCENDING, DESCENDING }

    private final double HGAP_BETWEEN = 20;
    private final double VGAP_BETWEEN = -5;
    private FlowPane fp;
    private LinkedHashSet<CountdownPane> cdPanes;
    private DisplayOrder displayOrder;
    private ArrayList<Region> paddingsInUse;

    private ButtonMode mode;

    private static CountdownPaneView cpv = null;

    private CountdownPaneView() {
        this.mode = ButtonMode.NO_SELECT;
        this.paddingsInUse = new ArrayList<Region>();
        this.displayOrder = DisplayOrder.ASCENDING;
        this.cdPanes = new LinkedHashSet<>();
        this.fp = new FlowPane();
        this.fp.prefWrapLengthProperty().bind(this.widthProperty());
        // the -2 below is needed to correct a small offset when at minHeight
        this.fp.minHeightProperty().bind(this.heightProperty().add(-2));
        this.fp.setMaxHeight(Double.MAX_VALUE);
        this.fp.setBackground(Colour.createBG(Colour.BACKGROUND, 0, 0));
        // this.fp.setBackground(Colour.createBG(Color.BLUE, 0, 0));
        this.fp.setAlignment(Pos.TOP_CENTER);
        this.fp.setHgap(HGAP_BETWEEN);
        this.fp.setVgap(VGAP_BETWEEN);
        this.setBackground(null);
        this.setFitToWidth(true);
        this.setHbarPolicy(ScrollBarPolicy.NEVER);
        this.setVbarPolicy(ScrollBarPolicy.NEVER);
        this.fp.setOnMousePressed(event -> { RightClickMenu.close(); });
        this.setContent(this.fp);
    }

    public static CountdownPaneView getInstance() {
        if (cpv == null) {
            cpv = new CountdownPaneView();
            cpv.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                cpv.addPaddingForAlignment();
            }
            });
        }
        return cpv;
    }

    private void addPaddingForAlignment() {
        this.paddingsInUse.forEach(
            padding -> CountdownPaneView.getInstance().getChildren().remove(padding));
        if (this.cdPanes.isEmpty())
            return;
        double cdWidth = CountdownPane.WIDTH;
        double cdHeight = CountdownPane.HEIGHT;
        double width = this.fp.getWidth();
        int columns = (int) Math.floor(width / cdWidth);
        int panesOnLast = this.cdPanes.size() % columns;
        int remainder = (panesOnLast > 0) ? columns - panesOnLast : 0;
        for (int i = 0; i < remainder; i++) {
            Region padding = new Region();
            padding.setMinSize(cdWidth, cdHeight);
            padding.setMaxSize(cdWidth, cdHeight);
            padding.setVisible(false);
            this.fp.getChildren().add(padding);
            this.paddingsInUse.add(padding);
        }
    }

    public void repopulate(LocalDate now) {
        Countdown[] countdowns;
        // this if-else is ok for now since there's only two DisplayOrders rn
        if (displayOrder.equals(DisplayOrder.ASCENDING))
            countdowns = StorageHandler.getAscendingCountdowns();
        else
            countdowns = StorageHandler.getDescendingCountdowns();

        fp.getChildren().clear();
        this.cdPanes.clear();
        for (Countdown c : countdowns) {
            CountdownPane countdownPane = new CountdownPane(c, now);
            fp.getChildren().add(countdownPane);
            this.cdPanes.add(countdownPane);
        }
        addPaddingForAlignment();
        updateMode();
    }

    public int getNumOfSelections() {
        int selections = 0;
        for (CountdownPane countdownPane : cdPanes) {
            if (countdownPane.isSelected())
                selections++;
        }
        return selections;
    }

    public void markSelectedAsComplete() {
        cdPanes.forEach(pane -> {
            if (pane.isSelected()) {
                pane.getCountdown().setDone(true);
            }
        });
        deselectAll();
        repopulate(LocalDate.now());
    }

    public void editSelected() {
        // gets first selected
        for (CountdownPane countdownPane : cdPanes) {
            if (countdownPane.isSelected()) {
                EditWindow.getInstance(countdownPane.getCountdown());
                return;
            }
        }
    }

    public void addSelectedToFolder() {
        // TODO
    }

    public void deleteSelected() {
        for (CountdownPane countdownPane : cdPanes) {
            if (countdownPane.isSelected()) {
                StorageHandler.deleteCountdown(countdownPane.getCountdown());
            }
        }
        StorageHandler.save();
        repopulate(LocalDate.now());
    }

    public void deselectAll() {
        for (CountdownPane countdownPane : cdPanes) {
            countdownPane.setSelected(false);
            countdownPane.applyDeselectStyle();
        }
        updateMode();
    }

    public DisplayOrder getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(DisplayOrder displayOrder) {
        this.displayOrder = displayOrder;
    }

    public ButtonMode getMode() {
        return mode;
    }

    private void updateMode() {
        int selections = getNumOfSelections();
        if (selections == 0)
            this.mode = ButtonMode.NO_SELECT;
        else if (selections == 1)
            this.mode = ButtonMode.SINGLE_SELECT;
        else
            this.mode = ButtonMode.MULTI_SELECT;
        CountdownPaneControls.getInstance().setMode();
    }

    private class CountdownPane extends VBox {
        public static final double WIDTH = 280;
        public static final double HEIGHT = 140;
        public static final double NAME_WIDTH = 150;
        public static final double DIV_WIDTH = 10;
        public static final double CONTENT_HEIGHT = 100;

        private HBox hoverHBox; // container
        private HBox contentHBox; // container
        private Label statusLabel; // for displaying the status on mouse hover
        private Label endDateLabel; // for displaying the due date on mouse hover
        private FadeTransition ft; // for ui hover animation
        private Countdown countdown; // points to the backend object
        private CustomBox border; // for selection ui indication
        private boolean selected; // for selection detection
        // TODO private CountdownPane prev; // for multi-select functionality

        public CountdownPane(Countdown cd, LocalDate now) {
            this.countdown = cd;
            this.selected = false;
            this.setAlignment(Pos.CENTER);
            initContentHBox(now);
            initHoverHBox();
            initSelectable(this);
            this.getChildren().addAll(this.hoverHBox, this.contentHBox);
        }

        private void initHoverHBox() {
            int leftRightPadding = 16;
            double height = HEIGHT - CONTENT_HEIGHT;
            this.hoverHBox = new HBox();
            this.hoverHBox.setPrefSize(WIDTH, height);
            Font font =
                Font.font(Vals.FontTools.FONT_FAM, FontWeight.MEDIUM, FontPosture.ITALIC, 14);
            this.statusLabel = new Label();
            this.statusLabel.setAlignment(Pos.BOTTOM_LEFT);
            this.statusLabel.setFont(font);
            this.statusLabel.setTextFill(Vals.Colour.TXT_GHOST);
            this.statusLabel.setMaxSize(WIDTH / 2, height);
            HBox.setMargin(this.statusLabel, new Insets(0, 0, 0, leftRightPadding));
            Pane spacer = new Pane();
            spacer.setMaxSize(WIDTH, height);
            HBox.setHgrow(spacer, Priority.ALWAYS);
            this.endDateLabel = new Label();
            this.endDateLabel.setAlignment(Pos.BOTTOM_RIGHT);
            this.endDateLabel.setFont(font);
            this.endDateLabel.setTextFill(Vals.Colour.TXT_GHOST);
            this.endDateLabel.setMaxSize(WIDTH / 2, height);
            HBox.setMargin(this.endDateLabel, new Insets(0, leftRightPadding, 0, 0));
            this.hoverHBox.setFillHeight(true);
            this.hoverHBox.getChildren().addAll(this.statusLabel, spacer, this.endDateLabel);

            ft = new FadeTransition(Duration.millis(300), hoverHBox);

            contentHBox.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    ft.stop();
                    if (selected)
                        return; // do nothing if selected
                    setMouseEnterAnim(ft);
                    // TODO: copy this in its own update method later, to be called by a watchdog
                    LocalDate now = LocalDate.now();
                    String status = countdown.getStatusString(now);
                    String end = countdown.getStringDueDate(now);
                    statusLabel.setText(status);
                    endDateLabel.setText("Due: " + end);
                    ft.playFromStart();
                }
            });

            contentHBox.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (selected)
                        return; // do nothing if selected
                    ft.stop();
                    setMouseExitAnim(ft);
                    ft.playFromStart();
                }
            });
        }

        private void setMouseEnterAnim(FadeTransition anim) {
            anim.setFromValue(0);
            anim.setToValue(1);
        }

        private void setMouseExitAnim(FadeTransition anim) {
            anim.setFromValue(1);
            anim.setToValue(0);
        }

        private void initContentHBox(LocalDate now) {
            this.contentHBox = new HBox();
            contentHBox.setPrefSize(WIDTH, CONTENT_HEIGHT);
            contentHBox.setFillHeight(true);
            // adds the border
            this.border = new CustomBox(GraphicalUI.DRAW_THICKNESS);
            CustomBox.applyCustomBorder(contentHBox, border);
            // adds the name display
            contentHBox.getChildren().add(createNameLabel(countdown));
            // adds the divider
            contentHBox.getChildren().add(createVerticalDivider());
            // adds the day countdown pane
            contentHBox.getChildren().add(createCountdownDisplay(countdown, now));

            // contentHBox.setBackground(new Background(new BackgroundFill(Color.GRAY, null,
            // null)));
        }

        private Label createNameLabel(Countdown cd) {
            String name = cd.getName();
            Label nameLabel = new Label(name);
            Font nameFont = Font.font(Vals.FontTools.FONT_FAM, FontWeight.SEMI_BOLD, 17);
            nameLabel.setAlignment(Pos.CENTER);
            nameLabel.setTextAlignment(TextAlignment.JUSTIFY);
            nameLabel.setTextOverrun(OverrunStyle.ELLIPSIS);
            nameLabel.setFont(nameFont);
            nameLabel.setTextFill(Color.WHITE);
            nameLabel.setPrefWidth(NAME_WIDTH);
            nameLabel.prefHeightProperty().bind(this.heightProperty());

            HBox.setMargin(nameLabel, new Insets(10, -2, 10, 10));
            HBox.setHgrow(nameLabel, Priority.ALWAYS);
            return nameLabel;
        }

        private Pane createVerticalDivider() {
            Pane pane = new Pane();
            Color colour = Color.rgb(255, 255, 255, 0.3);
            CustomLine separator = new CustomLine(2, CustomLine.Type.VERTICAL_TYPE);
            pane.setPrefSize(DIV_WIDTH, HEIGHT);
            separator.setStrokeColour(colour);
            separator.setPadding(20);
            CustomLine.applyCustomBorder(pane, separator);
            return pane;
        }

        private VBox createCountdownDisplay(Countdown cd, LocalDate now) {
            VBox display = new VBox();
            int daysLeft = Math.abs(cd.daysUntilDue(now));

            Label numLabel = new Label(Vals.GraphicalUI.intToString(daysLeft));
            Font numFont =
                Font.font(Vals.FontTools.FONT_FAM, FontWeight.BOLD, FontPosture.ITALIC, 30);
            numLabel.setAlignment(Pos.CENTER);
            numLabel.setTextAlignment(TextAlignment.CENTER);
            numLabel.setFont(numFont);
            numLabel.setTextFill(Color.WHITE);
            numLabel.prefWidthProperty().bind(display.widthProperty());
            display.getChildren().add(numLabel);

            Font textFont =
                Font.font(Vals.FontTools.FONT_FAM, FontWeight.BOLD, FontPosture.ITALIC, 13);
            String textNoun = (daysLeft != 1) ? "DAYS" : "DAY";
            String textAdverb = (cd.isOverdue(now)) ? "AGO" : "LEFT";
            Label textLabel = new Label(textNoun + "\n" + textAdverb);
            textLabel.setAlignment(Pos.CENTER);
            textLabel.setTextAlignment(TextAlignment.CENTER);
            textLabel.setFont(textFont);
            textLabel.setTextFill(Color.WHITE);
            textLabel.prefWidthProperty().bind(display.widthProperty());
            display.getChildren().add(textLabel);
            HBox.setMargin(display, new Insets(10, 10, 10, 0));
            HBox.setHgrow(display, Priority.ALWAYS);
            return display;
        }

        private void initSelectable(CountdownPane thisInstance) {
            contentHBox.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getButton().equals(MouseButton.SECONDARY)) {
                        thisInstance.onSecondaryMousePress(event.getSceneX(), event.getSceneY());
                    } else {
                        thisInstance.onPrimaryMousePress();
                    }
                    event.consume();
                }
            });
        }

        private void onPrimaryMousePress() {
            if (this.selected) {
                applyDeselectStyle();
            } else {
                applySelectStyle();
            }
            this.selected = !this.selected;
            updateMode();
            RightClickMenu.close();
        }

        private void onSecondaryMousePress(double x, double y) {
            if (!this.selected)
                CountdownPaneView.getInstance().deselectAll();
            applySelectStyle();
            this.selected = true;
            updateMode(); // needs to be called before RightClickMenu opens
            RightClickMenu.openAt(x, y);
        }

        public void applyDeselectStyle() {
            ft.stop();
            hoverHBox.setOpacity(1);
            border.setStrokeColour(Color.WHITE);
            statusLabel.setTextFill(Vals.Colour.TXT_GHOST);
            endDateLabel.setTextFill(Vals.Colour.TXT_GHOST);
        }

        public void applySelectStyle() {
            ft.stop();
            hoverHBox.setOpacity(1);
            border.setStrokeColour(Vals.Colour.SELECTED);
            statusLabel.setTextFill(Vals.Colour.SELECTED);
            endDateLabel.setTextFill(Vals.Colour.SELECTED);
        }

        public Countdown getCountdown() {
            return countdown;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
            CountdownPaneView.getInstance().updateMode();
        }
    }
}
