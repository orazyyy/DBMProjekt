package de.hhu.cs.dbs.propra.table.Tickets;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;
import de.hhu.cs.dbs.propra.Application;
import de.hhu.cs.dbs.propra.UserLevel;
import de.hhu.cs.dbs.propra.helpers.AccountHelper;
import de.hhu.cs.dbs.propra.helpers.LogInfoHelper;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MyTickets extends Table {
    @Override
    public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
        UserLevel.hasSufficientUserLevel(UserLevel.READ_ONLY, this.getClass().getName());
        UserLevel.hasSufficientUserLevel(UserLevel.READ_ONLY, this.getClass().getName());
        LogInfoHelper.logShow(this.getClass().getName());

        String selectQuery = "SELECT RTicketID, RBenutzerID, RVorstellungID, RKinderticket, RReihe, RNummer FROM B_reserviert_T ";
        if (filter != null && !filter.isEmpty()){
            selectQuery += "WHERE RBenutzerID ="
                    + String.valueOf(AccountHelper.getPersonalIDbyEmail(String.valueOf(Application.getInstance().getData().get("email"))));
        }

        LogInfoHelper.logShowDone(this.getClass().getName(), selectQuery);
        return selectQuery;
    }

    @Override
    public String getSelectQueryForRowWithData(Data data) throws SQLException {
        UserLevel.hasSufficientUserLevel(UserLevel.READ_ONLY, this.getClass().getName());
        LogInfoHelper.logSelect(this.getClass().getName(), data);

        String selectQuery = "SELECT RTicketID, RBenutzerID, RVorstellungID, RKinderticket, RReihe, RNummer FROM B_reserviert_T "
                + "WHERE RTicketID= "
                + String.valueOf(data.get("B_reserviert_T.RTicketID")
        );

        LogInfoHelper.logSelectDone(this.getClass().getName(), data, selectQuery);
        return selectQuery;
    }

    @Override
    public void insertRowWithData(Data data) throws SQLException {
        UserLevel.hasSufficientUserLevel(UserLevel.CUSTOMER, this.getClass().getName());
        LogInfoHelper.logInsert(this.getClass().getName(), data);
        //Query
        PreparedStatement insertTicketStatement = Application.getInstance().getConnection().prepareStatement(
                "INSERT INTO B_reserviert_T (RTicketID, RBenutzerID, RVorstellungID, RKinderticket, RReihe, RNummer) "
                        +"values (1, 7, 1, 0, 'A', 10)");
        insertTicketStatement.setInt(1,Integer.valueOf(String.valueOf(data.get("B_reserviert_T.RTicketID"))));
        insertTicketStatement.setInt(2,Integer.valueOf(String.valueOf(data.get("B_reserviert_T.RBenutzerID"))));
        insertTicketStatement.setInt(3,Integer.valueOf(String.valueOf(data.get("B_reserviert_T.RVorstellungID"))));
        insertTicketStatement.setInt(4,Integer.valueOf(String.valueOf(data.get("B_reserviert_T.RKinderticket"))));
        insertTicketStatement.setString(5, String.valueOf(data.get("B_reserviert_T.RReihe")));
        insertTicketStatement.setInt(6,Integer.valueOf(String.valueOf(data.get("B_reserviert_T.RNummer"))));
        insertTicketStatement.executeUpdate();

        LogInfoHelper.logInsertDone(this.getClass().getName(), data,
                String.valueOf("B_reserviert_T.RTicketID") + "-"
                        + String.valueOf(data.get("B_reserviert_T.RBenutzerID"))+ "-"
                        + String.valueOf(data.get("B_reserviert_T.RVorstellungID"))+ "-"
                        + String.valueOf(data.get("B_reserviert_T.RVorstellungID"))+"-"
                        + String.valueOf(data.get("B_reserviert_T.RReihe"))+"-"
                        + String.valueOf(data.get("B_reserviert_T.RNummer")));
    }

    @Override
    public void updateRowWithData(Data oldData, Data newData) throws SQLException {
        UserLevel.hasSufficientUserLevel(UserLevel.CUSTOMER, this.getClass().getName());
        LogInfoHelper.logUpdate(this.getClass().getName(), oldData, newData);

        PreparedStatement updateArtikelSchlagwortStatement = Application.getInstance().getConnection().prepareStatement(
                "UPDATE B_reserviert_T "
                        + "SET RTicketID=?, RBenutzerID=?, RVorstellungID=?, RKinderticket=?, RReihe=?, RNummer=? "
                        + "WHERE RTicketID = ?");
        updateArtikelSchlagwortStatement.setInt(1, Integer.valueOf(String.valueOf(oldData.get("B_reserviert_T.RTicketID"))));
        updateArtikelSchlagwortStatement.setInt(2, Integer.valueOf(String.valueOf(oldData.get("B_reserviert_T.RBenutzerID"))));
        updateArtikelSchlagwortStatement.setInt(3, Integer.valueOf(String.valueOf(newData.get("B_reserviert_T.RVorstellungID"))));
        updateArtikelSchlagwortStatement.setInt(4, Integer.valueOf(String.valueOf(newData.get("B_reserviert_T.RKinderticket"))));
        updateArtikelSchlagwortStatement.setString(5, String.valueOf(newData.get("B_reserviert_T.RReihe")));
        updateArtikelSchlagwortStatement.setInt(6, Integer.valueOf(String.valueOf(newData.get("B_reserviert_T.RNummer"))));
        updateArtikelSchlagwortStatement.setInt(7, Integer.valueOf(String.valueOf(oldData.get("B_reserviert_T.RTicketID"))));
        updateArtikelSchlagwortStatement.executeUpdate();

        LogInfoHelper.logUpdateDone(this.getClass().getName(), oldData, newData,"done");

    }

    @Override
    public void deleteRowWithData(Data data) throws SQLException {
        UserLevel.hasSufficientUserLevel(UserLevel.CUSTOMER, this.getClass().getName());
        LogInfoHelper.logDelete(this.getClass().getName(), data);

        PreparedStatement deletTicketStatement = Application.getInstance().getConnection().prepareStatement(
                "DELETE FROM B_reserviert_T WHERE RTicketID = ?");
        deletTicketStatement.setInt(1, Integer.valueOf(String.valueOf(data.get("B_reserviert_T.RTicketID"))));
        deletTicketStatement.executeUpdate();

        LogInfoHelper.logDeleteDone(this.getClass().getName(), data, String.valueOf(data.get("B_reserviert_T.RTicketID")));
    }
}
