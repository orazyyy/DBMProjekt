package de.hhu.cs.dbs.propra.table.Account;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;
import de.hhu.cs.dbs.propra.UserLevel;
import de.hhu.cs.dbs.propra.helpers.LogInfoHelper;

import java.sql.SQLException;

public class Mitarbeiter extends Table {

    @Override
    public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
        UserLevel.hasSufficientUserLevel(UserLevel.EMPLOYEE, this.getClass().getName());
        LogInfoHelper.logShow(this.getClass().getName());

        String selectQuery = "SELECT Email, Passwort, Vorname, Nachname, Geburtsdatum"
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
        return null;
    }

    @Override
    public void insertRowWithData(Data data) throws SQLException {

    }

    @Override
    public void updateRowWithData(Data oldData, Data newData) throws SQLException {

    }

    @Override
    public void deleteRowWithData(Data data) throws SQLException {

    }
}
