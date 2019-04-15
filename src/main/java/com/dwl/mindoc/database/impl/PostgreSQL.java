package com.dwl.mindoc.database.impl;

import com.dwl.mindoc.database.Database;

/**
 * @program: mindoc
 * @description: PostgreSQL
 * @author: daiwenlong
 * @create: 2018-10-13 12:19
 **/
public class PostgreSQL implements Database {
    private String baseName;

    public PostgreSQL(String baseName) {
        this.baseName = baseName;
    }

    @Override
    public String getBaseName() {
        return this.baseName;
    }

    @Override
    public String getType() {
        return "PostgreSQL";
    }

    @Override
    public String getTablesSql(Database base) {
        return "\n" +
                "SELECT\n" +
                "\trelname AS table_name,\n" +
                "\tCAST (\n" +
                "\t\tobj_description (relfilenode, 'pg_class') AS VARCHAR\n" +
                "\t) AS table_comment\n" +
                "FROM\n" +
                "\tpg_class C,\n" +
                "\tpg_tables b\n" +
                "WHERE\n" +
                "\tC .relname = b.tablename\n" +
                "AND b.schemaname = '" + base.getBaseName()+"'";
    }

    @Override
    public String getColumnSql(Database base, String tableName) {
        return "SELECT\n" +
                "\tC . COLUMN_NAME,\n" +
                "\tpgd.description AS column_comment,\n" +
                "\tC .data_type column_type,\n" +
                "\tC .is_nullable,\n" +
                "\tC .is_identity column_key\n" +
                "FROM\n" +
                "\tpg_catalog.pg_statio_all_tables AS st\n" +
                "INNER JOIN pg_catalog.pg_description pgd ON (pgd.objoid = st.relid)\n" +
                "RIGHT JOIN information_schema. COLUMNS C ON (\n" +
                "\tpgd.objsubid = C .ordinal_position\n" +
                "\tAND C .table_schema = st.schemaname\n" +
                "\tAND C . TABLE_NAME = st.relname\n" +
                ")\n" +
                "WHERE\n" +
                "\tC . TABLE_NAME = '" + tableName+"'  order by c.ordinal_position " ;
    }
}


