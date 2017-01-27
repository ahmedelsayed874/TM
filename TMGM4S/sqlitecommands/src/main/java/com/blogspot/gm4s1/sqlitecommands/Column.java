package com.blogspot.gm4s1.sqlitecommands;

/**
 * Created by GloryMaker on 11/12/2016.
 */

public class Column implements ICommand {
    String name;
    DataTypes dataType;
    Constraints[] constraints;

    public Column(String name, DataTypes dataType, Constraints[] constraints){
        this.name = name;
        this.dataType = dataType;
        this.constraints = constraints;
    }

    @Override
    public String getCode() {
        String contraintstr = " ";
        if (constraints != null) {
            for (int i = 0; i < constraints.length; i++) {
                contraintstr += constraints[i].getName() + " ";
            }
        }
        return name + " " + dataType.name() + contraintstr;
    }
}
