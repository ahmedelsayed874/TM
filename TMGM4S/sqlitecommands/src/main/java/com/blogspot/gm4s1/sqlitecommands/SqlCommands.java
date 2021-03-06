package com.blogspot.gm4s1.sqlitecommands;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GloryMaker on 11/12/2016.
 */

public class SqlCommands {

    abstract class Command {
        private String tableName;

        public Command(String tableName){
            if (tableName == null || tableName.length() == 0) {
                throw new RuntimeException("Table Name not allowed to be null or empty");

            } else {
                this.tableName = tableName;
            }
        }

        public String getTableName() {
            return tableName;
        }
    }

    public class CreateTable extends Command implements ICommand {
        private List<Column> columns = new ArrayList<>();

        public CreateTable(String tableName){
            super(tableName);
        }

        public void addColumn(String name, DataTypes dataType, Constraints[] constraints){
            columns.add(new Column(name, dataType, constraints));
        }

        @Override
        public String getCode() {
            String cols = "";
            for (Column col : columns) {
                cols += col.getCode() + ", ";
            }

            if (cols.length() == 0) throw new RuntimeException("No Column are added to " + this.getTableName() + " table");
            else {
                return "CREATE TABLE " + this.getTableName() + " (" + cols.substring(0, cols.length()-2) + ");";
            }
        }

        @Override
        public String toString() {
            return getCode();
        }
    }

    public class DropTable extends Command implements ICommand {

        public DropTable(String tableName){
            super(tableName);
        }

        @Override
        public String getCode() {
            return "DROP TABLE "+ this.getTableName();
        }

        @Override
        public String toString() {
            return getCode();
        }
    }

    public class RowCount extends Command implements ICommand {

        public RowCount(String tableName){
            super(tableName);
        }

        @Override
        public String getCode() {
            return "select count(*) from "+ this.getTableName();
        }

        @Override
        public String toString() {
            return getCode();
        }
    }
}
