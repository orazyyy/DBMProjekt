package de.hhu.cs.dbs.propra.helpers;

import java.io.IOException;
import java.util.ArrayList;

import com.alexanderthelen.applicationkit.database.Table;
import com.alexanderthelen.applicationkit.gui.TableViewController;

import com.alexanderthelen.applicationkit.gui.ViewController;
import javafx.scene.control.TreeItem;

@SuppressWarnings("restriction")
public class GUIHelper {
    public static TreeItem<ViewController> addTableOfClassToTree(Table table, String title, ArrayList<TreeItem<ViewController>> treeItems) {
        TreeItem<ViewController> treeItem = createTreeItemForTableOfClass(table, title);
        treeItems.add(treeItem);
        return treeItem;
    }

    public static TreeItem<ViewController> createTreeItemForTableOfClass(Table table, String title) {
        table.setTitle(title);
        TableViewController MyTableViewController;
        try {
            MyTableViewController = TableViewController.createWithNameAndTable(title, table);
            MyTableViewController.setTitle(title);
        } catch (IOException e) {
            MyTableViewController = null;
        }
        TreeItem<ViewController> MyTreeItem = new TreeItem<>(MyTableViewController);

        return MyTreeItem;
    }

    public static TreeItem<ViewController> addTableOfClassToTreeItem(Table table, String title, TreeItem<ViewController> parentItem) {
        TreeItem<ViewController> treeItem = createTreeItemForTableOfClass(table, title);
        parentItem.getChildren().add(treeItem);
        parentItem.setExpanded(true);
        return treeItem;
    }
}