package de.hhu.cs.dbs.propra.gui;

import com.alexanderthelen.applicationkit.gui.ViewController;
import de.hhu.cs.dbs.propra.Application;
import de.hhu.cs.dbs.propra.UserLevel;
import de.hhu.cs.dbs.propra.helpers.GUIHelper;
import de.hhu.cs.dbs.propra.table.Account.Administrator;
import de.hhu.cs.dbs.propra.table.Account.AllBenutzer;
import de.hhu.cs.dbs.propra.table.Account.Mitarbeiter;
import de.hhu.cs.dbs.propra.table.Account.MyAccount;
import de.hhu.cs.dbs.propra.table.Films.BewertungVonFilm;
import de.hhu.cs.dbs.propra.table.Films.Film;
import de.hhu.cs.dbs.propra.table.Films.FilmHatSchauspieler;
import de.hhu.cs.dbs.propra.table.Films.MyBewertung;
import de.hhu.cs.dbs.propra.table.Genre.FilmHatGenre;
import de.hhu.cs.dbs.propra.table.Tickets.AllTickets;
import de.hhu.cs.dbs.propra.table.Tickets.MyTickets;
import de.hhu.cs.dbs.propra.table.Vorstellungs.Vorstellung;
import javafx.scene.control.TreeItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MasterViewController extends com.alexanderthelen.applicationkit.gui.MasterViewController {
    protected MasterViewController(String name) {
        super(name);
    }

    public static MasterViewController createWithName(String name) throws IOException {
        MasterViewController controller = new MasterViewController(name);
        controller.loadView();
        return controller;
    }

    @Override
    protected ArrayList<TreeItem<ViewController>> getTreeItems() {
        Logger logger = Logger.getLogger(this.getClass().getName());
        int userLevel = 0;
        ArrayList<TreeItem<ViewController>> treeItems = new ArrayList<>();


        try {
            userLevel = Integer.valueOf(Application.getInstance().getData().get("UserLevel").toString());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "User permission level not set!", ex);
        }

        if (userLevel >= UserLevel.READ_ONLY) {
            logger.info("User is at least of permission level read only.");

            TreeItem<ViewController> accounts = GUIHelper.addTableOfClassToTree(new MyAccount(), "Mein Account", treeItems);

            TreeItem<ViewController> Films = GUIHelper.addTableOfClassToTree(new Film(), "Films", treeItems);
            GUIHelper.addTableOfClassToTreeItem(new FilmHatGenre(), "FilmHatGenre", Films);
            GUIHelper.addTableOfClassToTreeItem(new FilmHatSchauspieler(), "FilmHatSchauspieler", Films);
            GUIHelper.addTableOfClassToTreeItem(new MyBewertung(), "MyBewertung", Films);



            TreeItem<ViewController> Tickets = GUIHelper.addTableOfClassToTree(new MyTickets(), "MyTickets", treeItems);

            TreeItem<ViewController> Vorstellungs = GUIHelper.addTableOfClassToTree(new Vorstellung(), "Vorstellung", treeItems);


            if (userLevel >= UserLevel.CUSTOMER) {
                logger.info("User is at least of permission level customer.");

                if (userLevel >= UserLevel.EMPLOYEE) {
                    logger.info("User is at least of permission level employee.");

                    GUIHelper.addTableOfClassToTreeItem(new AllBenutzer(), "AllBenutzer", accounts);
                    GUIHelper.addTableOfClassToTreeItem(new Mitarbeiter(), "Mitarbeiter", accounts);
                    GUIHelper.addTableOfClassToTreeItem(new Administrator(), "Administrator", accounts);
                    GUIHelper.addTableOfClassToTreeItem(new BewertungVonFilm(), "AllBewertung", Films);
                    GUIHelper.addTableOfClassToTreeItem(new AllTickets(), "AllTicket", Tickets);
                }
            }
        }
        return (ArrayList<TreeItem<ViewController>>)treeItems;
    }
}
