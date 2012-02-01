package com.soletta.seek.util;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Iterator;

public class JDBCQuery implements Iterable<ResultSet>  {

    private Connection conn;
    private final boolean prepare;
    private String query;
    private ResultSet resultSet;
    private PreparedStatement prepared;
    private Statement statement;

    public JDBCQuery(Connection conn, String query) {
        this(conn, query, false);
    }
    
    public JDBCQuery(Connection conn, String query, boolean prepare) {
        this.conn = conn;
        this.query = query;
        this.prepare = prepare;
    }
    
    public ResultSet executeQuery() throws SQLException {
        customizeConnection(conn);
        query = customizeQuery(query);
        if (prepare) {
            if (prepared == null) {
                prepared = conn.prepareStatement(query);
                customizeStatement(prepared);
            }            
            resultSet = prepared.executeQuery();
        } else {
            statement = conn.createStatement();
            customizeStatement(statement);
            resultSet = statement.executeQuery(query);
        }
        return resultSet;
    }
    
    public PreparedStatement getPrepared() {
        return prepared;
    }
    
    protected void customizeConnection(Connection conn2) {
    }

    protected void customizeStatement(Statement s) {
    }

    protected String customizeQuery(String query) {
        return query;
    }

    public void close() throws SQLException {
        try {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
            } finally {
                if (prepared != null) {
                    prepared.close();
                    prepared = null;
                }
                if (statement != null) {
                    statement.close();
                    statement = null;
                }
            }
        } finally {
            if (conn != null) {
                conn.close();
                conn = null;
            }
        }
    }

    @Override
    public Iterator<ResultSet> iterator() {
        try {
            return new Iterator<ResultSet>() {
                final ResultSet rs = executeQuery();
                Boolean n;
                
                @Override
                public boolean hasNext() {
                    if (n == null)
                        try {
                            n = rs.next();
                            if (!n) 
                                rs.close();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    return n;
                }
    
                @Override
                public ResultSet next() {
                    if (hasNext()) {
                        n = null;
                        return rs;
                    } else {
                        throw new IllegalStateException("No more results");
                    }
                }
    
                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }};
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setArray(int i, Collection<String> crawlUuids) throws SQLException {
        Array arr = conn.createArrayOf(prepared.getMetaData().getColumnTypeName(i), crawlUuids.toArray());
        prepared.setArray(i, arr);
    }

}
