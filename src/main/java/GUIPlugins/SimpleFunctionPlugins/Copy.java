package GUIPlugins.SimpleFunctionPlugins;

import Main.Storable;
import javafx.scene.control.TreeItem;

/**
 * @author Isabel
 * @since 03.12.2016
 */
public class Copy extends SimpleFunctionPlugin {

    @Override
    public boolean operatesOnAllStorables() {
        return true;
    }
    @Override
    public Storable execute(Object object) {

        Storable storable = (Storable) object;
        Storable copy = storable.otherName(storable.getName()+"_Copy");
        TreeItem<String> selected = gui.getOverviewController().getTreeView().getSelectionModel().getSelectedItem();
        String parent = selected.getParent().getValue().toLowerCase();
        Class parentClass = gui.getCli().lookUpTable.get(parent);
        String name = copy.getName();

        //gui.refresh();
        return copy;
    }


    @Override
    String getName() {
        return "Copy";
    }

    @Override
    public Class inputType() {
        String parent = gui.getOverviewController().getTreeView().getSelectionModel().getSelectedItem().getParent().getValue().toLowerCase();
        return gui.getCli().lookUpTable.get(parent);
    }

    @Override
    Class outputType() {
        String parent = gui.getOverviewController().getTreeView().getSelectionModel().getSelectedItem().getParent().getValue().toLowerCase();
        return gui.getCli().lookUpTable.get(parent);
    }
}
