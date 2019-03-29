package de.hhu.cs.dbs.propra.table.Vorstellungs;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;
import de.hhu.cs.dbs.propra.Application;
import de.hhu.cs.dbs.propra.UserLevel;
import de.hhu.cs.dbs.propra.helpers.LogInfoHelper;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Vorstellung extends Table {
    @Override
    public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
        UserLevel.hasSufficientUserLevel(UserLevel.READ_ONLY, this.getClass().getName());
        LogInfoHelper.logShow(this.getClass().getName());

        String selectQuery = "SELECT VorstellungID, Uhrzeit, Datum, Sprache, `3D`, Saalname, VfilmID FROM B_reserviert_T ";
        if (filter != null && !filter.isEmpty()){
            selectQuery += "WHERE strftim =" + filter;
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
        UserLevel.hasSufficientUserLevel(UserLevel.EMPLOYEE, this.getClass().getName());
        LogInfoHelper.logInsert(this.getClass().getName(), data);
        String printInsert = "INSERT INTO Vorstellung(Uhrzeit, Datum, Sprache, `3D`, Saalname, VfilmID) values ("
                +String.valueOf(data.get("Vorstellung.Uhrzeit")+", "
                +String.valueOf(data.get("Vorstellung.Datum")) + ", "
                +String.valueOf(data.get("Vorstellung.Sprache")) + ", '"
                +String.valueOf(data.get("Vorstellung.3D")) + "', "
                +String.valueOf(data.get("Vorstellung.Saalname"))+ "', "
                +String.valueOf(data.get("Vorstellung.VfilmID"))+")");

        PreparedStatement insertFGStatement = Application.getInstance().getConnection().prepareStatement(printInsert);
        insertFGStatement.executeUpdate();
        LogInfoHelper.logInsertDone(this.getClass().getName(), data, "done");
        //LogInfoHelper.logInsert(this.getClass().getName(),data);
        System.out.println(printInsert);
    }


    @Override
    public void updateRowWithData(Data oldData, Data newData) throws SQLException {
        UserLevel.hasSufficientUserLevel(UserLevel.EMPLOYEE, this.getClass().getName());
        LogInfoHelper.logUpdate(this.getClass().getName(), oldData, newData);

        PreparedStatement updateArtikelSchlagwortStatement = Application.getInstance().getConnection().prepareStatement(
                "UPDATE Vorstellung "
                        + "SET VorstellungID=?, Uhrzeit=?, Datum=?, Sprache=?, `3D`=?, Saalname=?, VfilmID=? "
                        + "WHERE VorstellungID = ?");
        updateArtikelSchlagwortStatement.setInt(1, Integer.valueOf(String.valueOf(newData.get("Vorstellung.VorstellungID"))));
        updateArtikelSchlagwortStatement.setString(2, String.valueOf(newData.get("Vorstellung.Uhrzeit")));
        updateArtikelSchlagwortStatement.setString(3, String.valueOf(newData.get("Vorstellung.Datum")));
        updateArtikelSchlagwortStatement.setString(4, String.valueOf(newData.get("Vorstellung.Sprache")));
        updateArtikelSchlagwortStatement.setInt(5, Integer.valueOf(String.valueOf(newData.get("Vorstellung.`3D`"))));
        updateArtikelSchlagwortStatement.setString(6, String.valueOf(newData.get("Vorstellung.Saalname")));
        updateArtikelSchlagwortStatement.setInt(7, Integer.valueOf(String.valueOf(newData.get("Vorstellung.VfilmID"))));
        updateArtikelSchlagwortStatement.setInt(1, Integer.valueOf(String.valueOf(oldData.get("Vorstellung.VorstellungID"))));
        updateArtikelSchlagwortStatement.executeUpdate();

        LogInfoHelper.logUpdateDone(this.getClass().getName(), oldData, newData,"done");

    }

    @Override
    public void deleteRowWithData(Data data) throws SQLException {
        UserLevel.hasSufficientUserLevel(UserLevel.EMPLOYEE, this.getClass().getName());
        LogInfoHelper.logDelete(this.getClass().getName(), data);

        PreparedStatement deletTicketStatement = Application.getInstance().getConnection().prepareStatement(
                "DELETE FROM Vorstellung WHERE VorstellungID = ?");
        deletTicketStatement.setInt(1, Integer.valueOf(String.valueOf(data.get("Vorstellung.VorstellungID"))));
        deletTicketStatement.executeUpdate();

        LogInfoHelper.logDeleteDone(this.getClass().getName(), data, String.valueOf(data.get("Vorstellung.VorstellungID")));
    }
}
