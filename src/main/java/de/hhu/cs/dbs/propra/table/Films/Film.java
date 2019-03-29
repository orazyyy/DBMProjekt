package de.hhu.cs.dbs.propra.table.Films;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;
import de.hhu.cs.dbs.propra.Application;
import de.hhu.cs.dbs.propra.UserLevel;
import de.hhu.cs.dbs.propra.helpers.LogInfoHelper;

import java.sql.SQLException;

public class Film extends Table {
    @Override
    public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
        UserLevel.hasSufficientUserLevel(UserLevel.READ_ONLY, this.getClass().getName());
        LogInfoHelper.logShow(this.getClass().getName());
        //user's own account, don't need to search anything with filter.
        String selectQuery = "SELECT FilmID, Titel, Beschreibungstext, Spiellaenge, Oeffentlichungsdatum, FSK, Filmcover, Trailor ";

        if (filter != null && !filter.isEmpty()) {
            String[] filterSplit = filter.split(",");
            String SearchArea = filterSplit[0];
                if (filterSplit.length >= 2) {
                    LogInfoHelper.logFilter(this.getClass().getName(), filter);
                    switch (SearchArea){
                        case "Titel":
                            selectQuery += "FROM Film WHERE Titel LIKE '%" + filterSplit[1] + "%'";
                            break;
                        case "Genre":
                            selectQuery += ",GROUP_CONCAT(FText) AS Genres FROM Film LEFT JOIN F_gehoert_zu_G ON Film.FilmID=F_gehoert_zu_G.FFilmID "
                                    +"GROUP BY Titel "
                                    +"HAVING Genres LIKE '%'";
                            for(int i = 1; i<filterSplit.length; i++){
                                selectQuery += " AND Genres LIKE '%"+ filterSplit[i]+"%'";
                            }
                            break;
                        case "Veroefentlichungsjahr":
                            selectQuery += "FROM Film WHERE strftime('%Y', Oeffentlichungsdatum) = strftime('%Y','" + filterSplit[1].toString()+"-01-01"+"')";
                            break;
                        case "Schauspieler":
                            selectQuery += ", group_concat(Vorname||' '||Nachname||' --'||ifnull(Kuenstlername,' ')||'--') AS Schauspielers FROM Film LEFT JOIN Sch_spielt_F JOIN Schauspieler JOIN Person "
                                    +"ON Film.FilmID=Sch_spielt_F.SchFilmID AND Sch_spielt_F.SchSchauspielerID=Schauspieler.SchauspielerID AND Schauspieler.SchauspielerID=Person.PersonalID "
                                    +"GROUP BY Titel "
                                    +"HAVING Schauspielers LIKE '%'";
                            for(int i = 1; i<filterSplit.length; i++){
                                selectQuery += " AND Schauspielers LIKE '%"+ filterSplit[i]+"%'";
                            }
                            break;
                        default:
                            throw new SQLException("Invalid search option!");
                    }
                }else {
                    throw new SQLException("Invalid search option!");
                }
            }else{
                selectQuery +="FROM Film";
            }

        LogInfoHelper.logShowDone(this.getClass().getName(), selectQuery);
        //System.out.println(selectQuery);
        return selectQuery;
    }

    @Override
    public String getSelectQueryForRowWithData(Data data) throws SQLException {
        UserLevel.hasSufficientUserLevel(UserLevel.READ_ONLY, this.getClass().getName());
        LogInfoHelper.logShow(this.getClass().getName());
        //user's own account, don't need to search anything with filter.
        String selectQuery = "SELECT Titel, GROUP_CONCAT(FText) AS Tag, Beschreibungstext, Spiellaenge, Oeffentlichungsdatum, FSK, Filmcover, Trailor, "
                +"count(*) AS BewertungsAnzahl, avg(Stern) AS Durchschnitt "
                +"FROM Film LEFT JOIN F_gehoert_zu_G JOIN B_bewertet_F ON Film.FilmID=F_gehoert_zu_G.FFilmID AND B_Bewertet_F.BFilmID=Film.FilmID"
                + " WHERE Titel = '" + String.valueOf(data.get("Film.Titel") + "' GROUP BY Titel");
//TODO Bewertung count() avg()
        LogInfoHelper.logShowDone(this.getClass().getName(), selectQuery);
        return selectQuery;
    }

    @Override
    public void insertRowWithData(Data data) throws SQLException {
        UserLevel.hasSufficientUserLevel(UserLevel.EMPLOYEE, this.getClass().getName());
//TODO finish insert film
        //INSERT INTO Film (Titel, Beschreibungstext, Spiellaenge, Oeffentlichungsdatum, FSK, Filmcover, Trailor) values (?,?,?,?,?,?,?);
    }

    @Override
    public void updateRowWithData(Data oldData, Data newData) throws SQLException {
        UserLevel.hasSufficientUserLevel(UserLevel.EMPLOYEE, this.getClass().getName());
        //TODO finish update film
    }

    @Override
    public void deleteRowWithData(Data data) throws SQLException {
        UserLevel.hasSufficientUserLevel(UserLevel.EMPLOYEE, this.getClass().getName());
        //TODO finish delete film
    }
}
