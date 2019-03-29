package de.hhu.cs.dbs.propra.helpers;

import com.alexanderthelen.applicationkit.database.Connection;
import com.alexanderthelen.applicationkit.database.Data;
import de.hhu.cs.dbs.propra.Application;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

public class AccountHelper {/*
    public static void ChangeData(Data oldData, Data newData) throws SQLException{
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
        int MyPersonalID = getPersonalIDbyEmail(String.valueOf(Application.getInstance().getData().get("eMail")));

        Connection con = Application.getInstance().getConnection();
        con.getRawConnection().setAutoCommit(false);

        try {
            //Update data in Table 'Person'
            PreparedStatement UpdateUserData = con.prepareStatement(
                    "UPDATE Person SET Vorname = ?, Nachname = ?, Geburtsdatum = ? "
                            + "WHERE PersonalID = ?"
            );
            UpdateUserData.setString(1,String.valueOf(newData.get("Person.Vorname")));
            UpdateUserData.setString(2,String.valueOf(newData.get("Person.Nachname")));
            UpdateUserData.setString(3,String.valueOf(newData.get("Person.Geburtsdatum")));
            UpdateUserData.setInt(4,MyPersonalID);
            UpdateUserData.executeUpdate();
            //Update data in Table Benutzer
            UpdateUserData = con.prepareStatement(
                    "UPDATE Benutzer SET Email = ?, Passwort = ? "
                            + "WHERE BenutzerID = ?"
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

    public static void DeleteAccountWhitEmail(String Email) throws SQLException{
        /*
        LogInfoHelper.logDelete("Account", Email);
        String[] tables = {"Administrator","","",""};
        Connection con = Application.getInstance().getConnection();
        con.getRawConnection().setAutoCommit(false);

        try{
            for (String tablename:DatabaseInfo.TABLES_WITH_E_MAIL_ADDRESS) {
                deleteAllEntriesWithEMailAddressInTable(tablename, eMail, con);
            }
            con.getRawConnection().commit();
            con.getRawConnection().setAutoCommit(true);
        }catch (Exception e){
            con.getRawConnection().rollback();
            con.getRawConnection().setAutoCommit(true);
            throw e;
        }

    }


    public static void deleteAllEntriesWithEMailAddressInTable(String tablename, String Email, Connection con) throws SQLException {
        Logger logger = Logger.getLogger(AccountHelper.class.getName());
        logger.info("Trying to delete all entries with E-Mail-Address " + Email + " from table " + tablename + "...");

        con.getRawConnection().setAutoCommit(false);
        //get MyPersonalID
        int MyPersonalID = getPersonalIDbyEmail(Email);
        //Delete by ID
        PreparedStatement deleteEntriesStatement = con.prepareStatement(
                "DELETE FROM " + tablename + " WHERE " + tablename + "ID = ?");
        deleteEntriesStatement.setInt(1, MyPersonalID);
        deleteEntriesStatement.executeUpdate();

        logger.info("Deleting entries for Email " + Email + " in table " + tablename + " done!");
    }*/

    public static int getPersonalIDbyEmail(String Email)throws SQLException{
        Connection con = Application.getInstance().getConnection();
        //get MyPersonalID
        PreparedStatement UpdateUserData = con.prepareStatement(
                "SELECT PersonalID FROM Benutzer LEFT JOIN Person ON Person.PersonalID = Benutzer.BenutzerID"
                        +" WHERE Email = ?"
        );
        UpdateUserData.setString(1,Email);
        ResultSet PIDResult = UpdateUserData.executeQuery();
        int MyPersonalID = PIDResult.getInt("PersonalID");
        return MyPersonalID;
    }

}
