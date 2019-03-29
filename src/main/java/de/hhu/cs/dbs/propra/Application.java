package de.hhu.cs.dbs.propra;

import com.alexanderthelen.applicationkit.database.Connection;
import com.alexanderthelen.applicationkit.gui.MasterDetailViewController;
import com.alexanderthelen.applicationkit.gui.WindowController;
import de.hhu.cs.dbs.propra.gui.AuthenticationViewController;
import de.hhu.cs.dbs.propra.gui.LoginViewController;
import de.hhu.cs.dbs.propra.gui.MasterViewController;
import de.hhu.cs.dbs.propra.gui.RegistrationViewController;

public class Application extends com.alexanderthelen.applicationkit.Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start() throws Exception {
        setConnection(new Connection("jdbc:sqlite:database.db"));

        WindowController mainWindowController = WindowController.createWithName("window");
        mainWindowController.setTitle("Projekt");

        AuthenticationViewController authenticationViewController = AuthenticationViewController
                .createWithName("authentication");
        authenticationViewController.setTitle("Authentifizierung");

        LoginViewController loginViewController = LoginViewController.createWithName("login");
        loginViewController.setTitle("Anmeldung");
        authenticationViewController.setLoginViewController(loginViewController);

        RegistrationViewController registrationViewController = RegistrationViewController
                .createWithName("registration");
        registrationViewController.setTitle("Registrierung");
        authenticationViewController.setRegistrationViewController(registrationViewController);

        MasterDetailViewController mainViewController = MasterDetailViewController.createWithName("main");
        mainViewController.setTitle("Projekt");
        mainViewController.setMasterViewController(MasterViewController.createWithName("master"));
        authenticationViewController.setMainViewController(mainViewController);

        mainWindowController.setViewController(authenticationViewController);
        setWindowController(mainWindowController);
        show();
    }
}