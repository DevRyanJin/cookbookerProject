package com.cookbooker.logic;

import com.cookbooker.data.Database;
import com.cookbooker.data.Database_HSQL;

public class Service {

    private static Database databasePersistence = null;
    private static String databaseName = "demodb";

    /**
     * Should not be used by any classes outside the logic layer
     *
     * @return returns the currently initialized database
     */
    private static synchronized Database getDataBase() {
        if (databasePersistence == null)
            databasePersistence = new Database_HSQL(getDBPathName());
        return databasePersistence;
    }

    public static AccessRecipe getAccessRecipe() {
        return new AccessRecipe(getDataBase());
    }

    public static boolean databaseExists() {
        return databasePersistence != null;
    }

    /**
     * Copied from Franklin's sample project to get HSQL working
     *
     * @param name Name of the Database to be accessed.
     */
    public static void setDBPathName(final String name) {
        try {
            Class.forName("org.hsqldb.jdbcDriver").newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        databaseName = name;
    }

    /**
     * Copied from Franklin's sample project to get HSQL working
     *
     * @return Returns the name of the Database to be accessed
     */
    public static String getDBPathName() {
        return databaseName;
    }
}
