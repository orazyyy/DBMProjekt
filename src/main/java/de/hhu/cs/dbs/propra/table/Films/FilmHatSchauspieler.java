package de.hhu.cs.dbs.propra.table.Films;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;
import de.hhu.cs.dbs.propra.Application;
import de.hhu.cs.dbs.propra.UserLevel;
import de.hhu.cs.dbs.propra.helpers.LogInfoHelper;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FilmHatSchauspieler extends Table {
    @Override
    public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
        UserLevel.hasSufficientUserLevel(UserLevel.READ_ONLY, this.getClass().getName());
        LogInfoHelper.logShow(this.getClass().getName());
        //user's own account, don't need to search anything with filter.
        String selectQuery = "SELECT SchSchauspielerID, SchFilmID FROM Sch_spielt_F ";
        if (filter != null && !filter.isEmpty()){
            selectQuery += "WHERE SchSchauspielerID = " + filter;
        }


        LogInfoHelper.logShowDone(this.getClass().getName(), selectQuery);
        return selectQuery;
    }

    @Override
    public String getSelectQueryForRowWithData(Data data) throws SQLException {
        UserLevel.hasSufficientUserLevel(UserLevel.READ_ONLY, this.getClass().getName());
        LogInfoHelper.logSelect(this.getClass().getName(), data);

        String selectQuery = "SELECT SchFilmID, SchSchauspielerID FROM Sch_spielt_F "
                + "WHERE SchFilmID = '"
                + String.valueOf(data.get("Sch_spielt_F.SchFilmID"))
                + "' AND SchSchauspielerID = '"
                + String.valueOf(data.get("Sch_spielt_F.SchSchauspielerID")) + "'";

        LogInfoHelper.logSelectDone(this.getClass().getName(), data, selectQuery);
        return selectQuery;
    }

    @Override
    public void insertRowWithData(Data data) throws SQLException {
        UserLevel.hasSufficientUserLevel(UserLevel.EMPLOYEE, this.getClass().getName());
        LogInfoHelper.logInsert(this.getClass().getName(), data);
        String printInsert = "INSERT INTO Sch_spielt_F(SchSchauspielerID, SchFilmID) values ("
                +String.valueOf(data.get("Sch_spielt_F.SchSchauspielerID")+", "+String.valueOf(data.get("Sch_spielt_F.SchFilmID"))+")");

        PreparedStatement insertFGStatement = Application.getInstance().getConnection().prepareStatement(printInsert);
        //insertFGStatement.setInt(1,Integer.valueOf(String.valueOf(data.get("Sch_spielt_F.SchSchauspielerID"))));
        //insertFGStatement.setInt(1,Integer.valueOf(String.valueOf(data.get("Sch_spielt_F.SchFilmID"))));
        insertFGStatement.executeUpdate();
        LogInfoHelper.logInsertDone(this.getClass().getName(), data,
                String.valueOf("Sch_spielt_F.SchSchauspielerID") + "-"
                        + String.valueOf(data.get("Sch_spielt_F.SchFilmID")));
    }


    @Override
    public void updateRowWithData(Data oldData, Data newData) throws SQLException {
        UserLevel.hasSufficientUserLevel(UserLevel.EMPLOYEE, this.getClass().getName());
        LogInfoHelper.logUpdate(this.getClass().getName(), oldData, newData);

        PreparedStatement updateArtikelSchlagwortStatement = Application.getInstance().getConnection().prepareStatement(
                "UPDATE Sch_spielt_F "
                        + "SET SchSchauspielerID = ?, SchFilmID = ? "
                        + "WHERE SchSchauspielerID = ? AND SchFilmID = ?");
        updateArtikelSchlagwortStatement.setInt(1, Integer.valueOf(String.valueOf(newData.get("Sch_spielt_F.SchSchauspielerID"))));
        updateArtikelSchlagwortStatement.setInt(2, Integer.valueOf(String.valueOf(newData.get("Sch_spielt_F.SchFilmID"))));
        updateArtikelSchlagwortStatement.setInt(3, Integer.valueOf(String.valueOf(oldData.get("Sch_spielt_F.SchSchauspielerID"))));
        updateArtikelSchlagwortStatement.setInt(4, Integer.valueOf(String.valueOf(oldData.get("Sch_spielt_F.SchFilmID"))));
        updateArtikelSchlagwortStatement.executeUpdate();

        LogInfoHelper.logUpdateDone(this.getClass().getName(), oldData, newData,
                String.valueOf(newData.get("Sch_spielt_F.SchSchauspielerID")) + "-"
                        + String.valueOf(newData.get("Sch_spielt_F.SchFilmID")));
    }

    @Override
    public void deleteRowWithData(Data data) throws SQLException {
        UserLevel.hasSufficientUserLevel(UserLevel.EMPLOYEE, this.getClass().getName());
        LogInfoHelper.logDelete(this.getClass().getName(), data);

        PreparedStatement deleteArtikelSchlagwortStatement = Application.getInstance().getConnection().prepareStatement(
                "DELETE FROM Sch_spielt_F WHERE SchSchauspielerID = ? AND SchFilmID = ?");
        deleteArtikelSchlagwortStatement.setInt(1, Integer.valueOf(String.valueOf(data.get("Sch_spielt_F.SchSchauspielerID"))));
        deleteArtikelSchlagwortStatement.setInt(2, Integer.valueOf(String.valueOf(data.get("Sch_spielt_F.SchFilmID"))));

        deleteArtikelSchlagwortStatement.executeUpdate();

        LogInfoHelper.logDeleteDone(this.getClass().getName(), data,
                String.valueOf(data.get("Sch_spielt_F.SchSchauspielerID")) + "-"
                        + String.valueOf(data.get("Sch_spielt_F.SchFilmID")));

    }
}
