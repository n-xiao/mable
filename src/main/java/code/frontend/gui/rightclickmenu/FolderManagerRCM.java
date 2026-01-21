/*
   Copyright (C) 2026  Nicholas Siow <nxiao.dev@gmail.com>
*/

package code.frontend.gui.rightclickmenu;

import code.backend.data.CountdownFolder.SpecialType;
import code.backend.utils.FolderHandler;
import code.frontend.foundation.panels.buttons.Button;
import code.frontend.gui.sidebar.SidebarFolderManager;
import code.frontend.misc.Vals.Colour;
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
    private static final double HEIGHT = 85;

    private final Button EDIT;
    private final Button REMOVE;

    private FolderManagerRCM() {
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

        Button[] buttons = {edit, remove};
        Color[] colours = {Colour.BTTN_EDIT, Colour.BTTN_REMOVE};

        super(WIDTH, HEIGHT, buttons, colours);

        this.EDIT = edit;
        this.REMOVE = remove;
        this.getCustomBorder().setCornerRadii(0.35);
    }

    @Override
    public void setMode() {
        SpecialType type = FolderHandler.getCurrentlySelectedFolder().getType();
        if (type == SpecialType.ALL_INCOMPLETE) {
            this.EDIT.setEnabled(false);
            this.REMOVE.setEnabled(false);
        } else if (type == SpecialType.ALL_COMPLETE) {
            this.EDIT.setEnabled(false);
            this.REMOVE.setEnabled(false);
        } else {
            this.EDIT.setEnabled(true);
            this.REMOVE.setEnabled(true);
        }
    }
}
