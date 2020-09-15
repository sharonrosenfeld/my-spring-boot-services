package com.sharon.dataws.dao;

import com.sharon.dataws.model.APIUserData;
import com.sharon.dataws.model.SiteUserData;
import com.sharon.dataws.model.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@Component
public class UserDataDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDataDAO.class);
    private static final String USER_DATA_TABLE = "";
    private static final String[] key = new String[]{"ID"};

    private static final String COL_ID = "ID";
    private static final String COL_SALARY_API = "SALARY_API";
    private static final String COL_SALARY_SITE = "SALARY_SITE";
    private static final String COL_API_UPDATE_TS = "API_LAST_UPDATE";
    private static final String COL_SITE_UPDATE_TS = "SITE_LAST_UPDATE";


    @Autowired
    DataSource dataSource;

    public void upsertUserAPIData(APIUserData apiUserData) throws SQLException {
        LOGGER.info("upserting {} ", apiUserData);
        final String statement = String.format("INSERT INTO %s " +
                    "( ID, API_SALARY, API_LAST_UPDATE )" +
                    " VALUES (?, ?, CURRENT_TIMESTAMP )", USER_DATA_TABLE);
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(statement, key)) {
                preparedStatement.setString(1, apiUserData.getId());
                preparedStatement.setLong(2, apiUserData.getSalary());
                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                LOGGER.error("Failure inserting coupon detail to DB, error", ex);
                throw ex;
            }
        }

    public void upsertUserSiteData(SiteUserData siteUserData) throws SQLException {
        LOGGER.info("upserting {} ", siteUserData);
        final String statement = String.format("INSERT INTO %s " +
                "( ID, SITE_SALARY, SITE_LAST_UPDATE )" +
                " VALUES (?, ?, CURRENT_TIMESTAMP)", USER_DATA_TABLE);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(statement, key)) {
            preparedStatement.setString(1, siteUserData.getId());
            preparedStatement.setLong(2, siteUserData.getSalary());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.error("Failure inserting coupon detail to DB, error", ex);
            throw ex;
        }
    }

    private void populateUserData(ResultSet rs, UserData userData) throws SQLException{
            userData.setId(rs.getString(COL_ID));
            userData.setSalaryApi(rs.getInt(COL_SALARY_API));
            userData.setSalarySite(rs.getInt(COL_SALARY_SITE));
            userData.setUpdateDtApi(rs.getTimestamp(COL_API_UPDATE_TS));
            userData.setUpdateDtSite(rs.getTimestamp(COL_SITE_UPDATE_TS));
    }

    public UserData getUserData(String id) throws SQLException{
        LOGGER.info("query user {} ", id);
        final String statement = String.format("select * from %s where ID = ?", USER_DATA_TABLE);
        UserData userData = new UserData();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(statement, key)) {
            preparedStatement.setString(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                populateUserData(rs, userData);
            }
        } catch (SQLException ex) {
            LOGGER.error("Failure inserting coupon detail to DB, error", ex);
            throw ex;
        }
        LOGGER.info("retrieved {} ", userData);
        return userData;
    }
}

