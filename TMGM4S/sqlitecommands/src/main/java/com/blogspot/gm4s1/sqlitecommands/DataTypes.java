package com.blogspot.gm4s1.sqlitecommands;

/**
 * Created by GloryMaker on 11/12/2016.
 */

public enum DataTypes {
    /**
     The value is a NULL value.
     */
    NULL,

    /**
     The value is a signed integer, stored in 1, 2, 3, 4, 6, or 8 bytes
     depending on the magnitude of the value.
     */
    INTEGER,

    /**
     The value is a floating point value, stored as an 8-byte IEEE
     floating point number.
     */
    REAL,

    /**
     The value is a text string, stored using the database
     encoding (UTF-8, UTF-16BE or UTF-16LE)
     */
    TEXT,

    /**
     The value is a blob of data, stored exactly as it was input.
     */
    BLOB
}
