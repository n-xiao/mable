/*
   Copyright (C) 2026  Nicholas Siow <nxiao.dev@gmail.com>
*/

package code.frontend.capabilities.countdown.components;

import code.backend.data.CountdownFolder.SpecialType;
import code.backend.utils.FolderHandler;
import code.frontend.libs.katlaf.buttons.Button;
import code.frontend.libs.katlaf.menus.RightClickMenu;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class FolderRCM extends RightClickMenu {
    private static FolderRCM instance = null;

    /**
     * Important! This assumes that the current selected folder according to StorageHandler
     * is the target. So, be sure to always select the folder before spawning this RCM.
     */
    public static void spawnInstance(double x, double y) {
        if (instance == null)
            instance = new FolderRCM();
        instance.openAt(x, y);
    }

    public static void despawn() {
        if (instance == null)
            return;
        instance.close();
        instance = null;
    }

    private static final double WIDTH = 160;
    private static final double HEIGHT = 85;

    private final Button EDIT;
    private final Button REMOVE;

    private FolderRCM() {
        Button edit = new Button("Edit name") {
            @Override
            public void executeOnClick(MouseEvent event) {
                SidebarFolders.getInstance().editSelectedFolder();
                FolderRCM.despawn();
            }
        };

        Button remove = new Button("Remove folder") {
            @Override
            public void executeOnClick(MouseEvent event) {
                SidebarFolders.getInstance().deleteSelectedFolder();
                FolderRCM.despawn();
            }
        };

        Button[] buttons = {edit, remove};
        Color[] colours = {RiceHandler.getColour("bttnEdit"), RiceHandler.getColour("bttnRemove")};

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
