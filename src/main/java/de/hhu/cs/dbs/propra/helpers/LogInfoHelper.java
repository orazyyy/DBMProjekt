package de.hhu.cs.dbs.propra.helpers;

import java.util.logging.Logger;

import com.alexanderthelen.applicationkit.database.Data;

public class LogInfoHelper {
    public static void logShow(String tableName) {
        Logger logger = Logger.getLogger(tableName);
        logger.info("Showing " + tableName + ".");
    }

    public static void logShowDone(String tableName, String selectQuery) {
        Logger logger = Logger.getLogger(tableName);
        logger.info("Returned the following Query for show on " + tableName + ":\n" + selectQuery);
    }

    public static void logFilter(String tableName, String filter) {
        Logger logger = Logger.getLogger(tableName);
        logger.info("Searching for " + filter + " in " + tableName + " table.");
    }

    public static void logSelect(String tableName, Data data) {
        Logger logger = Logger.getLogger(tableName);
        logger.info("Trying to get Data for Dataset " + data.toString() + " in table " + tableName + ".");
    }

    public static void logSelectDone(String tableName, Data data, String selectQuery) {
        Logger logger = Logger.getLogger(tableName);
        logger.info("Returned the following Query for select on " + tableName + ":\n" + selectQuery);
    }

    public static void logInsert(String tableName, Data data) {
        Logger logger = Logger.getLogger(tableName);
        logger.info("Trying to insert new Dataset with data: " + data.toString() + " in table " + tableName + ".");
    }

    public static void logInsertDone(String tableName, Data data, String key) {
        Logger logger = Logger.getLogger(tableName);
        logger.info("Dataset " + key + " inserted into " + tableName + "!");
    }

    public static void logUpdate(String tableName, Data oldData, Data newData) {
        Logger logger = Logger.getLogger(tableName);
        logger.info("Trying to change " + tableName + " data from " + oldData + " to " + newData + ".");
    }

    public static void logUpdateDone(String tableName, Data oldData, Data newData, String key) {
        Logger logger = Logger.getLogger(tableName);
        logger.info("Done changing " + tableName + " data for " + key + ".");
    }

    public static void logDelete(String tableName, Data data) {
        Logger logger = Logger.getLogger(tableName);
        logger.info("Trying to delete Dataset from " + tableName + " with data: " + data.toString());
    }

    public static void logDelete(String tableName, String key) {
        Logger logger = Logger.getLogger(tableName);
        logger.info("Trying to delete Dataset from " + tableName + " with data: " + key);
    }

    public static void logDeleteDone(String tableName, Data data, String key) {
        Logger logger = Logger.getLogger(tableName);
        logger.info("Dataset for " + key + " deleted from " + tableName + ".");
    }

    public static void logDeleteDone(String tableName, String key) {
        Logger logger = Logger.getLogger(tableName);
        logger.info("Dataset for " + key + " deleted from " + tableName + ".");
    }
}