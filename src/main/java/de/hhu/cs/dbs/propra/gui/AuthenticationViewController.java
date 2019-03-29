package de.hhu.cs.dbs.propra.gui;

import com.alexanderthelen.applicationkit.database.Connection;
import com.alexanderthelen.applicationkit.database.Data;
import de.hhu.cs.dbs.propra.Application;
import de.hhu.cs.dbs.propra.UserLevel;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthenticationViewController extends com.alexanderthelen.applicationkit.gui.AuthenticationViewController {
    protected AuthenticationViewController(String name) {
        super(name);
    }

    public static AuthenticationViewController createWithName(String name) throws IOException {
        AuthenticationViewController viewController = new AuthenticationViewController(name);
        viewController.loadView();
        return viewController;
    }

    @Override
    public void loginUser(Data data) throws SQLException {
        //throw new SQLException(getClass().getName() + ".loginUser(Data) nicht implementiert.");
        boolean authenticated = false;
        Logger logger = Logger.getLogger(this.getClass().getName()+ "Login Event");
        logger.info("User tries to login: " + data.toString());

        PreparedStatement login = Application.getInstance().getConnection().prepareStatement("SELECT Email, Passwort From Benutzer WHERE Email = ?");
        login.setString(1,(String) data.get("email"));
        ResultSet loginResult = login.executeQuery();
        while (loginResult.next()){
            String dbEmail = loginResult.getString("Email");
            String dbPasswort = loginResult.getString("Passwort");
            String enteredPasswort = (String) data.get("password");
            logger.info("Found user with E-Mail: " + dbEmail + " and password: " + dbPasswort + ", entered password was: " + enteredPasswort);
            if(dbPasswort.equals(enteredPasswort)){
                logger.info("User credentials correct. Done searching for users.");
                Application.getInstance().getData().put("email", dbEmail);
                //check Administrator
                PreparedStatement Administrator = Application.getInstance().getConnection().prepareStatement("SELECT Email FROM Administrator as ad LEFT JOIN Benutzer as b on b.BenutzerID=ad.AdministratorID WHERE Email = ?");
                Administrator.setString(1, dbEmail);
                ResultSet AdministratorResult = Administrator.executeQuery();
                if(!AdministratorResult.isClosed()&&AdministratorResult.getString("Email").equals(dbEmail)){
                    Application.getInstance().getData().put("UserLevel", UserLevel.ADMINISTRATOR);
                } else {
                    // Check if Employee
                    PreparedStatement Mitarbeiter = Application.getInstance().getConnection().prepareStatement(
                            "SELECT Email FROM Mitarbeiter as m LEFT JOIN Benutzer as b on b.BenutzerID=m.MitarbeiterID WHERE Email = ?");
                    Mitarbeiter.setString(1, dbEmail);
                    ResultSet MitarbeiterResult = Mitarbeiter.executeQuery();

                    if (!MitarbeiterResult.isClosed() && MitarbeiterResult.getString("Email").equals(dbEmail)) {
                        Application.getInstance().getData().put("UserLevel", UserLevel.EMPLOYEE);
                    } else {
                        Application.getInstance().getData().put("UserLevel", UserLevel.CUSTOMER);
                    }
                }

                logger.info("UserLevel is: " + UserLevel.UserLevelToString(Integer.valueOf(String.valueOf(Application.getInstance().getData().get("UserLevel")))));
                authenticated = true;
            }
        }
        if (!authenticated) {
            logger.warning("User credentials incorrect. Done searching for users.");
            throw new SQLException("Username or password incorrect!");
        }

    }

    @Override
    public void registerUser(Data data) throws SQLException {
        Logger logger = Logger.getLogger(this.getClass().getName() + " Registration Event");
        logger.info("User tries to register: " + data.toString());

        //check age, because java never knows that trigger is fired...
        SimpleDateFormat sdfBirthday=new SimpleDateFormat("yyyy-MM-dd");
        //get Date of today-18year in format yyyy-MM-dd
        Date today = new Date();
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(today);
        rightNow.add(Calendar.YEAR,-18);//-18year
        Date dt1=rightNow.getTime();
        //get Date of Birthday
        String regDate = (String)data.get("birthday");
        Date dt2=null;
        try {
            dt2=sdfBirthday.parse(regDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //compare the Birthday with the Date of today-18year
        if (!data.get("password1").equals(data.get("password2"))) {
            throw new SQLException("Passwords do not match!");
        }else if(dt1.getTime()<dt2.getTime()){
            throw new SQLException("At least 18!");
        }

        Connection con = Application.getInstance().getConnection();

        con.getRawConnection().setAutoCommit(false);

        try {
            //Insert into Person
            PreparedStatement PersonInsert = con.prepareStatement("INSERT INTO Person (Vorname, Nachname,Geburtsdatum) VALUES (?, ?, ?)");
            PersonInsert.setString(1, (String) data.get("firstName"));
            PersonInsert.setString(2, (String) data.get("lastName"));
            PersonInsert.setString(3, (String) data.get("birthday"));
            PersonInsert.executeUpdate();
            //get PersonalID
            PersonInsert = con.prepareStatement("SELECT max(PersonalID) AS PersonalID FROM Person");
            ResultSet PIDResult = PersonInsert.executeQuery();
            int PersonalID = PIDResult.getInt("PersonalID");
            //Insert into Benutzer
            PersonInsert = con.prepareStatement("INSERT INTO Benutzer VALUES (?, ?, ?)");
            PersonInsert.setInt(1, PersonalID);
            PersonInsert.setString(2, (String)data.get("eMail"));
            PersonInsert.setString(3, (String) data.get("password1"));
            PersonInsert.executeUpdate();

            con.getRawConnection().commit();
            con.getRawConnection().setAutoCommit(true);
            logger.info("Registration successful!");
        } catch (SQLException sqle) {
            con.getRawConnection().rollback();
            con.getRawConnection().setAutoCommit(true);
            logger.log(Level.SEVERE, "User failed to register!", sqle);
            throw sqle;
        }
    }
}
