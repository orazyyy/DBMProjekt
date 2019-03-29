package de.hhu.cs.dbs.propra.table.Account;

import com.alexanderthelen.applicationkit.database.Connection;
import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;
import de.hhu.cs.dbs.propra.Application;
import de.hhu.cs.dbs.propra.UserLevel;
import de.hhu.cs.dbs.propra.helpers.AccountHelper;
import de.hhu.cs.dbs.propra.helpers.LogInfoHelper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

public class MyAccount extends Table {


    @Override
    public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
        UserLevel.hasSufficientUserLevel(UserLevel.READ_ONLY, this.getClass().getName());
        LogInfoHelper.logShow(this.getClass().getName());
        //user's own account, don't need to search anything with filter.
        String selectQuery = "SELECT Email, Passwort, Vorname, Nachname, Geburtsdatum FROM Benutzer LEFT JOIN Person ON Person.PersonalID = Benutzer.BenutzerID "
                + "WHERE Email = '"
                + String.valueOf(Application.getInstance().getData().get("email")) + "'";
        LogInfoHelper.logShowDone(this.getClass().getName(), selectQuery);
        return selectQuery;
    }

    @Override
    public String getSelectQueryForRowWithData(Data data) throws SQLException {
        UserLevel.hasSufficientUserLevel(UserLevel.READ_ONLY, this.getClass().getName());
        LogInfoHelper.logSelect(this.getClass().getName(), data);
        String selectQuery = "SELECT Email, Passwort, Vorname, Nachname, Geburtsdatum FROM Benutzer LEFT JOIN Person ON Person.PersonalID = Benutzer.BenutzerID"
                + " WHERE Email = '"+ String.valueOf(data.get("Benutzer.Email")) + "'";

        LogInfoHelper.logSelectDone(this.getClass().getName(), data, selectQuery);
        return selectQuery;
    }

    @Override
    public void insertRowWithData(Data data) throws SQLException {
        UserLevel.hasSufficientUserLevel(UserLevel.IMPOSSIBLE, this.getClass().getName());
        throw new SQLException("Es können keine weiteren Accounts für einen Kunden angelegt werden!");
    }

    @Override
    public void updateRowWithData(Data oldData, Data newData) throws SQLException {
        UserLevel.hasSufficientUserLevel(UserLevel.CUSTOMER, this.getClass().getName());
        LogInfoHelper.logUpdate("MyAccount", oldData, newData);
        //check age, because java never knows that trigger is fired...
        SimpleDateFormat sdfBirthday=new SimpleDateFormat("yyyy-MM-dd");
        //get Date of today-18year in format yyyy-MM-dd
        Date today = new Date();
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(today);
        rightNow.add(Calendar.YEAR,-18);//-18year
        Date dt1=rightNow.getTime();
        //get Date of Birthday
        String toUpdateDate = (String)newData.get("Person.Geburtsdatum");
        Date dt2=null;
        try {
            dt2=sdfBirthday.parse(toUpdateDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //compare the Birthday with the Date of today-18year
        if(dt1.getTime()<dt2.getTime()){
            throw new SQLException("At least 18!");
        }
        //Birthday ok, try others
        //get MyPersonalID

        String myemail = String.valueOf(oldData.get("Benutzer.Email"));
        int MyPersonalID = AccountHelper.getPersonalIDbyEmail(myemail);
        Connection con = Application.getInstance().getConnection();
        con.getRawConnection().setAutoCommit(false);
        String updateQuery = "UPDATE Benutzer SET Email = '"+String.valueOf(newData.get("Benutzer.Email"))
                +"', Passwort = '"+ String.valueOf(newData.get("Benutzer.Passwort"))
                +"' WHERE BenutzerID = "+String.valueOf(MyPersonalID);
        System.out.println(updateQuery);
        try {
            //Update data in Table 'Person'
            PreparedStatement UpdateUserData = con.prepareStatement(
                    "UPDATE Person SET Vorname = ?, Nachname = ?, Geburtsdatum = ? "
                            + "WHERE PersonalID = ?; "
            );
            UpdateUserData.setString(1,String.valueOf(newData.get("Person.Vorname")));
            UpdateUserData.setString(2,String.valueOf(newData.get("Person.Nachname")));
            UpdateUserData.setString(3,String.valueOf(newData.get("Person.Geburtsdatum")));
            UpdateUserData.setInt(4,MyPersonalID);
            UpdateUserData.executeUpdate();
            //con.getRawConnection().commit();
            //Update data in Table Benutzer

            UpdateUserData = con.prepareStatement(
                    "UPDATE Benutzer SET Email = ?,  Passwort = ? WHERE BenutzerID= ?"
            );
            UpdateUserData.setString(1,String.valueOf(newData.get("Benutzer.Email")));
            UpdateUserData.setString(2,String.valueOf(newData.get("Benutzer.Passwort")));
            UpdateUserData.setInt(3,MyPersonalID);
            UpdateUserData.executeUpdate();
            con.getRawConnection().commit();
            con.getRawConnection().setAutoCommit(true);
        }catch (SQLException sqle){
            con.getRawConnection().rollback();
            con.getRawConnection().setAutoCommit(true);
            throw sqle;
        }
        LogInfoHelper.logUpdateDone("Account", oldData, newData, String.valueOf(newData.get("Benutzer.Email")));
    }

    @Override
    public void deleteRowWithData(Data data) throws SQLException {
        String Email = String.valueOf(data.get("Benutzer.Email"));
        UserLevel.hasSufficientUserLevel(UserLevel.CUSTOMER, this.getClass().getName());
        //AccountDataHelper.deleteAccountByEMail(String.valueOf(data.get("Benutzer.Email")));
//-static void deleteAccountByEMail (String eMail)------------------------------------------------------------------

        LogInfoHelper.logDelete("Account", Email);

        Connection con = Application.getInstance().getConnection();
        con.getRawConnection().setAutoCommit(false);

        try {
            Logger logger = Logger.getLogger(AccountHelper.class.getName());
            logger.info("Trying to delete all entries with E-Mail-Address " + Email + " from table Benutzer...");

            PreparedStatement deleteEntriesStatement = con.prepareStatement(
                    "DELETE FROM Benutzer WHERE Email = ?");
            deleteEntriesStatement.setString(1, Email);
            deleteEntriesStatement.executeUpdate();
            con.getRawConnection().commit();
            con.getRawConnection().setAutoCommit(true);
            //logger.info("Deleting entries for Email " + Email + " in table " + tablename + " done!");
        } catch (Exception ex) {
            con.getRawConnection().rollback();
            con.getRawConnection().setAutoCommit(true);
            throw ex;
        }
    }
}
