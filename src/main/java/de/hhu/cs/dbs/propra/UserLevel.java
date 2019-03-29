package de.hhu.cs.dbs.propra;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserLevel {

    public static final int UNAUTHORIZED = 0;
    public static final int READ_ONLY = 1;
    public static final int CUSTOMER = 2;
    public static final int EMPLOYEE = 3;
    public static final int ADMINISTRATOR = 4;
    public static final int IMPOSSIBLE = 5;

    public static String UserLevelToString (int UserLevel) {
        switch (UserLevel) {
            case UNAUTHORIZED: return "Unauthorized";
            case READ_ONLY: return "Read-only";
            case CUSTOMER: return "Customer";
            case EMPLOYEE: return "Employee";
            case ADMINISTRATOR: return "Administrator";
            case IMPOSSIBLE: return "Impossible";
            default: return "Undefined.";
        }
    }

    public static boolean hasSufficientUserLevel (int requiredUserLevel, String parentClass) throws SQLException {
        Logger logger = Logger.getLogger(UserLevel.class.getName() + " for parent class: " + parentClass);
        int currentUserLevel = Integer.valueOf(String.valueOf(Application.getInstance().getData().get("UserLevel")));

        if (currentUserLevel >= requiredUserLevel) {
            logger.info("UserLevel ok" + parentClass + ". Required: " +
                    UserLevel.UserLevelToString(requiredUserLevel) + " (" +
                    String.valueOf(requiredUserLevel) + "), user has: " +
                    UserLevel.UserLevelToString(currentUserLevel) + " (" +
                    String.valueOf(currentUserLevel) + ").");
            return true;
        } else {
            SQLException sqle = new SQLException("Insufficient UserLevel! Required: " +
                    UserLevel.UserLevelToString(requiredUserLevel) + " (" +
                    String.valueOf(requiredUserLevel) + "), user has: " +
                    UserLevel.UserLevelToString(currentUserLevel) + " (" +
                    String.valueOf(currentUserLevel) + ").");
            logger.log(Level.WARNING, "Insufficient UserLevel!", sqle);
            throw sqle;
        }
    }
}
