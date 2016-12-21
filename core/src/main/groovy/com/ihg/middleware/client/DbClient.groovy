package com.ihg.middleware.client

import groovy.sql.GroovyRowResult
import groovy.sql.Sql

/**
 * A class to perform query to the database.
 *
 * @author ilya.lapitan@ihg.com
 */
class DbClient {

    /**
     * The database URL
     */
    String dbUrl

    /**
     * The database user
     */
    String dbUser

    /**
     * The user's password
     */
    String dbPassword

    String dbDriverClass = "oracle.jdbc.driver.OracleDriver"

    DbClient(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl
        this.dbUser = dbUser
        this.dbPassword = dbPassword
    }

    /**
     * Execute query to the database.
     * @param query to execute
     */
    GroovyRowResult executeQuery(String query) {
        def sql = Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriverClass)
        def rowResult = sql.firstRow(query)
        sql.close() // release resources
        rowResult
    }

    /**
     * Execute query to the database.
     * @param query to execute
     */
    def executeQueryWithoutResult(String query) {
        def sql = Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriverClass)
        sql.execute(query)
        sql.close() // release resources
    }

    String executeQuery(String query, String nameOfRow) {
        def sql = Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriverClass)
        def str = StringConverter.clob2String(sql.firstRow(query).get(nameOfRow))
        sql.close() // release resources
        str
    }


    List<GroovyRowResult> executeQueryWithParam(final def query, final def params) {
        def sql = Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriverClass)
        def rowResult = sql.rows(query, params)
        sql.close() // release resources
        rowResult
    }

    List<GroovyRowResult> executeQueryAndReturnAllRows(String query) {
        def sql = Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriverClass)
        def rows = sql.rows(query)
        sql.close()
        rows
    }
}