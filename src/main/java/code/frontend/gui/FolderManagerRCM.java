/*
   Copyright (C) 2026  Nicholas Siow <nxiao.dev@gmail.com>
*/

package code.frontend.gui;

import code.backend.CountdownFolder.SpecialType;
import code.backend.StorageHandler;
import code.frontend.misc.Vals.Colour;
import code.frontend.panels.Button;
import code.frontend.panels.CountdownPaneView;
import code.frontend.panels.SidebarFolderManager;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class FolderManagerRCM extends RightClickMenuTemplate {
    private static FolderManagerRCM instance = null;

    /**
     * Important! This assumes that the current selected folder according to StorageHandler
     * is the target. So, be sure to always select the folder before spawning this RCM.
     */
    public static void spawnInstance(double x, double y) {
        if (instance == null)
            instance = new FolderManagerRCM();
        instance.openAt(x, y);
    }

    protected static void despawn() {
        if (instance == null)
            return;
        instance.close();
        instance = null;
    }

    private static final double WIDTH = 160;
    private static final double HEIGHT = 130;

    private final Button EDIT;
    private final Button REMOVE;
    private final Button MARK_ALL_COMPLETE;

    private FolderManagerRCM() {
        final boolean SELECTED_ARE_COMPLETED =
            CountdownPaneView.getInstance().allSelectedAreCompleted();

        Button edit = new Button("Edit name") {
            @Override
            public void executeOnClick(MouseEvent event) {
                SidebarFolderManager.getInstance().editSelectedFolder();
                FolderManagerRCM.despawn();
            }
        };

        Button remove = new Button("Remove folder") {
            @Override
            public void executeOnClick(MouseEvent event) {
                SidebarFolderManager.getInstance().deleteSelectedFolder();
                FolderManagerRCM.despawn();
            }
        };

        Button mark = new Button("Mark as complete") {
            @Override
            public void executeOnClick(MouseEvent event) {
                CountdownPaneView.getInstance().selectAll();
                CountdownPaneView.getInstance().markSelectedAsComplete(!SELECTED_ARE_COMPLETED);
                FolderManagerRCM.despawn();
            }
        };

        Button[] buttons = {mark, null, edit, remove};
        Color[] colours = {Colour.BTTN_MARK_COMPLETE, null, Colour.BTTN_EDIT, Colour.BTTN_REMOVE};

        super(WIDTH, HEIGHT, buttons, colours);

        this.EDIT = edit;
        this.REMOVE = remove;
        this.MARK_ALL_COMPLETE = mark;

        this.getCustomBorder().setCornerOffset(0.20);
        this.getCustomBorder().setCornerDeviation(0.02);
        this.getCustomBorder().setCornerDeviation(0.02);
    }

    @Override
    public void setMode() {
        SpecialType type = StorageHandler.getCurrentlySelectedFolder().getType();
        if (type == SpecialType.ALL_INCOMPLETE) {
            this.MARK_ALL_COMPLETE.setEnabled(true);
            this.EDIT.setEnabled(false);
            this.REMOVE.setEnabled(false);
        } else if (type == SpecialType.ALL_COMPLETE) {
            this.MARK_ALL_COMPLETE.setEnabled(false);
            this.EDIT.setEnabled(false);
            this.REMOVE.setEnabled(false);
        } else {
            this.MARK_ALL_COMPLETE.setEnabled(true);
            this.EDIT.setEnabled(true);
            this.REMOVE.setEnabled(true);
        }
    }
}
