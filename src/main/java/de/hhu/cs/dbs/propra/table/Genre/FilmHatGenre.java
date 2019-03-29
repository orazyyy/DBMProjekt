package de.hhu.cs.dbs.propra.table.Genre;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;
import de.hhu.cs.dbs.propra.Application;
import de.hhu.cs.dbs.propra.UserLevel;
import de.hhu.cs.dbs.propra.helpers.LogInfoHelper;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FilmHatGenre extends Table {
    @Override
    public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
        UserLevel.hasSufficientUserLevel(UserLevel.READ_ONLY, this.getClass().getName());
        LogInfoHelper.logShow(this.getClass().getName());
        //user's own account, don't need to search anything with filter.
        //String selectQuery = "SELECT Titel, FText AS Genre FROM Film JOIN F_gehoert_zu_G ON Film.FilmID=F_gehoert_zu_G.FFilmID ";
        String selectQuery = "SELECT FFilmID, FText FROM F_gehoert_zu_G ";
        if (filter != null && !filter.isEmpty()){
            selectQuery += "WHERE FFilmID LIKE '%" + filter + "%'";
        }
        LogInfoHelper.logShowDone(this.getClass().getName(), selectQuery);
        return selectQuery;
    }

    @Override
    public String getSelectQueryForRowWithData(Data data) throws SQLException {
        UserLevel.hasSufficientUserLevel(UserLevel.READ_ONLY, this.getClass().getName());
        LogInfoHelper.logSelect(this.getClass().getName(), data);

        String selectQuery = "SELECT FText, FFilmID FROM F_gehoert_zu_G "
                + "WHERE FFilmID = '"
                + String.valueOf(data.get("F_gehoert_zu_G.FFilmID"))
                + "' AND FText = '"
                + String.valueOf(data.get("F_gehoert_zu_G.FText")) + "'";

        LogInfoHelper.logSelectDone(this.getClass().getName(), data, selectQuery);
        return selectQuery;
    }

    @Override
    public void insertRowWithData(Data data) throws SQLException {
        UserLevel.hasSufficientUserLevel(UserLevel.EMPLOYEE, this.getClass().getName());
        LogInfoHelper.logInsert(this.getClass().getName(), data);
        String printInsert = "INSERT INTO F_gehoert_zu_G(FText, FFilmID) values ('"+String.valueOf(data.get("F_gehoert_zu_G.FText")+"', "+String.valueOf(data.get("F_gehoert_zu_G.FFilmID"))+")");

        PreparedStatement insertFGStatement = Application.getInstance().getConnection().prepareStatement(printInsert);
        insertFGStatement.executeUpdate();
        LogInfoHelper.logInsertDone(this.getClass().getName(), data,
                String.valueOf("F_gehoert_zu_G.FText") + "-"
                        + String.valueOf(data.get("F_gehoert_zu_G.FFilmID")));
        //LogInfoHelper.logInsert(this.getClass().getName(),data);
        System.out.println(printInsert);
    }

    @Override
    public void updateRowWithData(Data oldData, Data newData) throws SQLException {
        UserLevel.hasSufficientUserLevel(UserLevel.EMPLOYEE, this.getClass().getName());
        LogInfoHelper.logUpdate(this.getClass().getName(), oldData, newData);

        PreparedStatement updateArtikelSchlagwortStatement = Application.getInstance().getConnection().prepareStatement(
                "UPDATE F_gehoert_zu_G "
                        + "SET FText = ?, FFilmID = ? "
                        + "WHERE FText = ? AND FFilmID = ?");
        updateArtikelSchlagwortStatement.setString(1, String.valueOf(newData.get("F_gehoert_zu_G.FText")));
        updateArtikelSchlagwortStatement.setInt(2, Integer.valueOf(String.valueOf(newData.get("F_gehoert_zu_G.FFilmID"))));
        updateArtikelSchlagwortStatement.setString(3, String.valueOf(oldData.get("F_gehoert_zu_G.FText")));
        updateArtikelSchlagwortStatement.setInt(4, Integer.valueOf(String.valueOf(oldData.get("F_gehoert_zu_G.FFilmID"))));
        updateArtikelSchlagwortStatement.executeUpdate();

        LogInfoHelper.logUpdateDone(this.getClass().getName(), oldData, newData,
                String.valueOf(newData.get("F_gehoert_zu_G.FText")) + "-"
                        + String.valueOf(newData.get("F_gehoert_zu_G.FFilmID")));
    }

    @Override
    public void deleteRowWithData(Data data) throws SQLException {
        UserLevel.hasSufficientUserLevel(UserLevel.EMPLOYEE, this.getClass().getName());
        LogInfoHelper.logDelete(this.getClass().getName(), data);

        PreparedStatement deleteArtikelSchlagwortStatement = Application.getInstance().getConnection().prepareStatement(
                "DELETE FROM F_gehoert_zu_G WHERE FText = ? AND FFilmID = ?");
        deleteArtikelSchlagwortStatement.setString(1, String.valueOf(data.get("F_gehoert_zu_G.FText")));
        deleteArtikelSchlagwortStatement.setInt(2, Integer.valueOf(String.valueOf(data.get("F_gehoert_zu_G.FFilmID"))));

        deleteArtikelSchlagwortStatement.executeUpdate();

        LogInfoHelper.logDeleteDone(this.getClass().getName(), data,
                String.valueOf(data.get("F_gehoert_zu_G.FText")) + "-"
                        + String.valueOf(data.get("F_gehoert_zu_G.FFilmID")));
    }
}
