package com.example.garbagedisposal;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.*;


import androidx.annotation.Nullable;

import com.example.garbagedisposal.DbService;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbServiceTest {
    @Mock
    Connection con;
    @Mock
    ResultSet uleSetMock;
    @Mock
    Statement statementMock;
    @Mock
    ResultSet resultSetMock;

    DbService dbService;
    String wasteType;
    String sql;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        dbService = new DbService(con);
        wasteType = "cardboard";
        sql = "SELECT way_of_reuse FROM Reusage WHERE waste_type='" + wasteType + "';";
    }

    @After
    public void tearDown() {
        MockitoAnnotations.openMocks(this);
        dbService = new DbService(con);
        wasteType = "cardboard";
        sql = "SELECT way_of_reuse FROM Reusage WHERE waste_type='" + "wasteType" + "';";

    }
    @Test
    public void getReUseMethodFromDbTest_return_empty_when_getStringFromDb_return_null() {
        when(dbService.getStringFromDb(sql, 1)).thenReturn(null);
        String result = dbService.getReUseMethodFromDb(wasteType);

        assertThat(result).isEmpty();
    }
    @Test
    public void getReUseMethodFromDbTest_return_empty_when_wasteType_is_null_and_getStringFromDb_return_null() {
        when(dbService.getStringFromDb(sql, 1)).thenReturn(null);

        String result = dbService.getReUseMethodFromDb(null);

        assertThat(result).isEmpty();
    }
    @Test
    public void getStringFromDbTest_return_null_when_connection_is_null() {
        con = null;
        String result = dbService.getStringFromDb(sql, 1);

        assertThat(result).isNull();
    }

    @Test
    public void TestConnection(){
        assertThat(con).isNotNull();
    }


    @Test
    public void getStringFromDbTest_return_null_when_connection_createStatement_throws() throws SQLException {
        when(con.createStatement()).thenThrow(new SQLException("error creating statement"));
        String result = dbService.getStringFromDb(sql, 1);

        assertThat(result).isNull();
    }
    @Test
    public void getStringFromDbTest_return_null_when_connection_executeQuery_throws() throws SQLException {
        when(con.createStatement()).thenThrow(new SQLException("error creating statement"));
        String result = dbService.getStringFromDb(sql, 1);

        assertThat(result).isNull();
    }
    @Test
    public void getStringFromDbTest_return_null_when_no_matching_records_in_db() throws SQLException {
        when(con.createStatement()).thenReturn(statementMock);
        when(statementMock.executeQuery(sql)).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true);
        String result = dbService.getStringFromDb(sql, 1);

        assertThat(result).isNull();
    }
}