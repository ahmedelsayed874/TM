package com.blogspot.gm4s1.sqlitecommands;

/**
 * Created by GloryMaker on 11/12/2016.
 */

public enum Constraints {
    /**
     Ensures that a column cannot have NULL value.
     */
    NOT_NULL,

    /**
     Provides a default value for a column when none is specified.
     */
    DEFAULT,

    /**
     Ensures that all values in a column are different.
     */
    UNIQUE,

    /**
     Uniquely identified each rows/records in a database table.
     */
    PRIMARY_Key,

    AUTOINCREMENT,

    /**
     The CHECK constraint ensures that all values in a column satisfy certain conditions.
     */
    CHECK;

    public String getName(){
        if (this.name().indexOf("_") >= 0) return this.name().replace('_', ' ');
        else return this.name();
    }
}
