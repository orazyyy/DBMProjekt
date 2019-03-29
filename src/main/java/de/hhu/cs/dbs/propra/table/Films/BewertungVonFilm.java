package de.hhu.cs.dbs.propra.table.Films;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;
import de.hhu.cs.dbs.propra.Application;
import de.hhu.cs.dbs.propra.UserLevel;
import de.hhu.cs.dbs.propra.helpers.LogInfoHelper;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BewertungVonFilm extends Table {
    @Override
    public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
        UserLevel.hasSufficientUserLevel(UserLevel.READ_ONLY, this.getClass().getName());
        LogInfoHelper.logShow(this.getClass().getName());

        String selectQuery = "SELECT BBenutzerID, BFilmID, Stern FROM B_bewertet_F ";
        if (filter != null && !filter.isEmpty()){
            selectQuery += "WHERE BFilmID =" + filter;
        }

        LogInfoHelper.logShowDone(this.getClass().getName(), selectQuery);
        return selectQuery;
    }

    @Override
    public String getSelectQueryForRowWithData(Data data) throws SQLException {
        UserLevel.hasSufficientUserLevel(UserLevel.EMPLOYEE, this.getClass().getName());
        LogInfoHelper.logSelect(this.getClass().getName(), data);

        String selectQuery = "SELECT BBenutzerID, BFilmID, Stern FROM B_bewertet_F "
                + "WHERE BFilmID = '"
                + String.valueOf(data.get("B_bewertet_F.BFilmID"))
                + "' AND BBenutzerID = '"
                + String.valueOf(data.get("B_bewertet_F.BBenutzerID")) + "'"
                +" AND Stern = " + String.valueOf(data.get("B_bewertet_F.Stern"));

        LogInfoHelper.logSelectDone(this.getClass().getName(), data, selectQuery);
        return selectQuery;
    }

    @Override
    public void insertRowWithData(Data data) throws SQLException {
        UserLevel.hasSufficientUserLevel(UserLevel.EMPLOYEE, this.getClass().getName());
        LogInfoHelper.logInsert(this.getClass().getName(), data);
        String printInsert = "INSERT INTO B_bewertet_F(BBenutzerID, BFilmID, Stern) values ("
                +String.valueOf(data.get("B_bewertet_F.BBenutzerID")+", "
                +String.valueOf(data.get("B_bewertet_F.BFilmID")) + ", "
                +String.valueOf(data.get("B_bewertet_F.Stern"))+")");

        PreparedStatement insertFGStatement = Application.getInstance().getConnection().prepareStatement(printInsert);
        insertFGStatement.executeUpdate();
        LogInfoHelper.logInsertDone(this.getClass().getName(), data,
                String.valueOf("B_bewertet_F.BFilmID") + "-"
                        + String.valueOf(data.get("B_bewertet_F.BBenutzerID")));
        //LogInfoHelper.logInsert(this.getClass().getName(),data);
        System.out.println(printInsert);
    }

    @Override
    public void updateRowWithData(Data oldData, Data newData) throws SQLException {
        UserLevel.hasSufficientUserLevel(UserLevel.EMPLOYEE, this.getClass().getName());
        LogInfoHelper.logUpdate(this.getClass().getName(), oldData, newData);

        PreparedStatement updateArtikelSchlagwortStatement = Application.getInstance().getConnection().prepareStatement(
                "UPDATE B_bewertet_F "
                        + "SET BFilmID = ?, BBenutzerID = ?, Stern = ? "
                        + "WHERE BFilmID = ? AND BBenutzerID = ?");
        updateArtikelSchlagwortStatement.setInt(1, Integer.valueOf(String.valueOf(newData.get("B_bewertet_F.BFilmID"))));
        updateArtikelSchlagwortStatement.setInt(2, Integer.valueOf(String.valueOf(newData.get("B_bewertet_F.BBenutzerID"))));
        updateArtikelSchlagwortStatement.setInt(3, Integer.valueOf(String.valueOf(newData.get("B_bewertet_F.Stern"))));
        updateArtikelSchlagwortStatement.setInt(4, Integer.valueOf(String.valueOf(oldData.get("B_bewertet_F.BFilmID"))));
        updateArtikelSchlagwortStatement.setInt(5, Integer.valueOf(String.valueOf(oldData.get("B_bewertet_F.BBenutzerID"))));
        updateArtikelSchlagwortStatement.executeUpdate();

        LogInfoHelper.logUpdateDone(this.getClass().getName(), oldData, newData,
                String.valueOf(newData.get("B_bewertet_F.BFilmID")) + "-"
                        + String.valueOf(newData.get("B_bewertet_F.BBenutzerID")));
    }

    @Override
    public void deleteRowWithData(Data data) throws SQLException {
        UserLevel.hasSufficientUserLevel(UserLevel.EMPLOYEE, this.getClass().getName());
        LogInfoHelper.logDelete(this.getClass().getName(), data);

        PreparedStatement deleteArtikelSchlagwortStatement = Application.getInstance().getConnection().prepareStatement(
                "DELETE FROM B_bewertet_F WHERE BBenutzerID = ? AND BFilmID = ?");
        deleteArtikelSchlagwortStatement.setInt(1, Integer.valueOf(String.valueOf(data.get("B_bewertet_F.BBenutzerID"))));
        deleteArtikelSchlagwortStatement.setInt(2, Integer.valueOf(String.valueOf(data.get("B_bewertet_F.BFilmID"))));

        deleteArtikelSchlagwortStatement.executeUpdate();

        LogInfoHelper.logDeleteDone(this.getClass().getName(), data,
                String.valueOf(data.get("B_bewertet_F.BBenutzerID")) + "-"
                        + String.valueOf(data.get("B_bewertet_F.BFilmID")));
    }
}
