package de.hhu.cs.dbs.propra.table.Account;

import com.alexanderthelen.applicationkit.database.Connection;
import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;
import de.hhu.cs.dbs.propra.Application;
import de.hhu.cs.dbs.propra.UserLevel;
import de.hhu.cs.dbs.propra.helpers.AccountHelper;
import de.hhu.cs.dbs.propra.helpers.LogInfoHelper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

public class AllBenutzer extends Table {
    @Override
    public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
        UserLevel.hasSufficientUserLevel(UserLevel.EMPLOYEE, this.getClass().getName());
        LogInfoHelper.logShow(this.getClass().getName());

        String selectQuery = "SELECT Email, PersonalID, Vorname, Nachname, Geburtsdatum "
                + "FROM Benutzer LEFT JOIN Person "
                + "ON Person.PersonalID = Benutzer.BenutzerID";
        if (filter != null && !filter.isEmpty()) {
            LogInfoHelper.logFilter(this.getClass().getName(), filter);
            selectQuery += " WHERE Benutzer.Email LIKE '%" + filter + "%'";
        }

        LogInfoHelper.logShowDone(this.getClass().getName(), selectQuery);
        return selectQuery;
    }

    @Override
    public String getSelectQueryForRowWithData(Data data) throws SQLException {
        UserLevel.hasSufficientUserLevel(UserLevel.EMPLOYEE, this.getClass().getName());
        LogInfoHelper.logSelect(this.getClass().getName(), data);
        int userLevel = Integer.valueOf(Application.getInstance().getData().get("UserLevel").toString());

        String selectQuery = "SELECT PersonalID, Vorname, Nachname, Geburtsdatum ";
        if(userLevel>=UserLevel.ADMINISTRATOR){
            selectQuery += ", Email, Passort ";
        }
        selectQuery += "FROM Person JOIN Benutzer ON Person.PersonalID=Benutzer.BenutzerID "
                + "WHERE Benutzer.Email = '"
                + String.valueOf(data.get("Benutzer.Email")) + "'";
        LogInfoHelper.logSelectDone(this.getClass().getName(), data, selectQuery);
        return selectQuery;
    }

    @Override
    public void insertRowWithData(Data data) throws SQLException {
        UserLevel.hasSufficientUserLevel(UserLevel.IMPOSSIBLE, this.getClass().getName());
        throw new SQLException("You can only delete your own account!");

    }

    @Override
    public void updateRowWithData(Data oldData, Data newData) throws SQLException {
        UserLevel.hasSufficientUserLevel(UserLevel.EMPLOYEE, this.getClass().getName());

        LogInfoHelper.logUpdate("AccountInfo", oldData, newData);
        Connection con = Application.getInstance().getConnection();
        con.getRawConnection().setAutoCommit(false);
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
        int MyPersonalID = Integer.valueOf(String.valueOf(oldData.get("Person.PersonalID")));



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
            int userLevel = Integer.valueOf(Application.getInstance().getData().get("UserLevel").toString());
            if(userLevel>=UserLevel.ADMINISTRATOR){
                UpdateUserData = con.prepareStatement(
                        "UPDATE Benutzer SET Email = ?, Passwort = ? "
                                + "WHERE BenutzerID = ?"
                );
                UpdateUserData.setString(1,String.valueOf(newData.get("Benutzer.Email")));
                UpdateUserData.setString(2,String.valueOf(newData.get("Benutzer.Passwort")));
                UpdateUserData.setInt(3,MyPersonalID);
            }
            con.getRawConnection().commit();
            con.getRawConnection().setAutoCommit(true);
        }catch (SQLException sqle){
            con.getRawConnection().rollback();
            con.getRawConnection().setAutoCommit(true);
            throw sqle;
        }
        LogInfoHelper.logUpdateDone("AccountInfo", oldData, newData, String.valueOf(newData.get("Person.PersonalID")));
    }

    @Override
    public void deleteRowWithData(Data data) throws SQLException {
        int pid = Integer.valueOf(String.valueOf(data.get("Person.PersonalID")));
        UserLevel.hasSufficientUserLevel(UserLevel.EMPLOYEE, this.getClass().getName());
        LogInfoHelper.logDelete("Account", String.valueOf(pid));

        Connection con = Application.getInstance().getConnection();
        con.getRawConnection().setAutoCommit(false);

        try {
            Logger logger = Logger.getLogger(AccountHelper.class.getName());
            logger.info("Trying to delete all entries with pid " + String.valueOf(pid) + " from table Benutzer...");

            PreparedStatement deleteEntriesStatement = con.prepareStatement(
                    "DELETE FROM Benutzer WHERE BenutzerID = ?");
            deleteEntriesStatement.setInt(1, pid);
            deleteEntriesStatement.executeUpdate();
            con.getRawConnection().commit();
            con.getRawConnection().setAutoCommit(true);
            logger.info("Deleting entries for pid " + String.valueOf(pid) + " in table Benutzer done!");
        } catch (Exception ex) {
            con.getRawConnection().rollback();
            con.getRawConnection().setAutoCommit(true);
            throw ex;
        }
    }
}
