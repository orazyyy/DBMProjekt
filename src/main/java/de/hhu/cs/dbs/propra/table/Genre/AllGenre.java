package de.hhu.cs.dbs.propra.table.Genre;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;
import de.hhu.cs.dbs.propra.Application;
import de.hhu.cs.dbs.propra.UserLevel;
import de.hhu.cs.dbs.propra.helpers.LogInfoHelper;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AllGenre extends Table {
    @Override
    public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
        LogInfoHelper.logShow(this.getClass().getName());

        String selectQuery = "SELECT Text FROM Genre";

        if (filter != null && !filter.isEmpty()) {
            LogInfoHelper.logFilter(this.getClass().getName(), filter);
            selectQuery += " WHERE Text LIKE '%" + filter + "%'";
        }

        LogInfoHelper.logShowDone(this.getClass().getName(), selectQuery);
        return selectQuery;
    }

    @Override
    public String getSelectQueryForRowWithData(Data data) throws SQLException {
        UserLevel.hasSufficientUserLevel(UserLevel.READ_ONLY, this.getClass().getName());
        LogInfoHelper.logSelect(this.getClass().getName(), data);

        String selectQuery = "SELECT Text AS Genres FROM Genre "
                + "WHERE Text = '"
                + String.valueOf(data.get("Genre.Text")) + "'";

        LogInfoHelper.logSelectDone(this.getClass().getName(), data, selectQuery);
        return selectQuery;
    }

    @Override
    public void insertRowWithData(Data data) throws SQLException {
        UserLevel.hasSufficientUserLevel(UserLevel.EMPLOYEE, this.getClass().getName());
        LogInfoHelper.logInsert(this.getClass().getName(), data);

        PreparedStatement insertSchlagwortStatement = Application.getInstance().getConnection().prepareStatement(
                "INSERT INTO Genre VALUES (?)");
        insertSchlagwortStatement.setString(1, String.valueOf(data.get("Genre.Text")));
        insertSchlagwortStatement.executeUpdate();

        LogInfoHelper.logInsertDone(this.getClass().getName(), data, String.valueOf(data.get("Genre.Text")));
    }

    @Override
    public void updateRowWithData(Data oldData, Data newData) throws SQLException {
        UserLevel.hasSufficientUserLevel(UserLevel.EMPLOYEE, this.getClass().getName());
        LogInfoHelper.logUpdate(this.getClass().getName(), oldData, newData);

        PreparedStatement updateSchlagwortStatement = Application.getInstance().getConnection().prepareStatement(
                "UPDATE Genre SET Text = ? WHERE Text = ?");
        updateSchlagwortStatement.setString(1, String.valueOf(newData.get("Genre.Text")));
        updateSchlagwortStatement.setString(2, String.valueOf(oldData.get("Genre.Text")));
        updateSchlagwortStatement.executeUpdate();

        LogInfoHelper.logUpdateDone(this.getClass().getName(), oldData, newData, String.valueOf(newData.get("Genre.Text")));
    }

    @Override
    public void deleteRowWithData(Data data) throws SQLException {
        UserLevel.hasSufficientUserLevel(UserLevel.EMPLOYEE, this.getClass().getName());
        LogInfoHelper.logDelete(this.getClass().getName(), data);

        PreparedStatement deleteSchlagwortStatement = Application.getInstance().getConnection().prepareStatement(
                "DELETE FROM Genre WHERE Text = ?");
        deleteSchlagwortStatement.setString(1, String.valueOf(data.get("Genre.Text")));
        deleteSchlagwortStatement.executeUpdate();

        LogInfoHelper.logDeleteDone(this.getClass().getName(), data, String.valueOf(data.get("Genre.Text")));
    }
}
