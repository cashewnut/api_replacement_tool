package pers.xyy.api_replacement_tool.persistence.impl;

import org.springframework.stereotype.Service;
import pers.xyy.api_replacement_tool.model.DeprecatedAPI;
import pers.xyy.api_replacement_tool.persistence.IDeprecatedAPIDAO;
import pers.xyy.api_replacement_tool.utils.DBUtil;
import pers.xyy.api_replacement_tool.utils.LoadProperties;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Service
public class DeprecatedAPIDAO implements IDeprecatedAPIDAO {

    private final static String GET = LoadProperties.get("SQL_GET");
    private final static String GETBYID = LoadProperties.get("SQL_GET_BY_ID");
    private final static String GETBYDAPI = LoadProperties.get("SQL_GET_BY_DEPRECATED_API");

    @Override
    public DeprecatedAPI getById(Integer id) {
        DeprecatedAPI api = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement(GETBYID);
            preparedStatement.setInt(1, id);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                api = new DeprecatedAPI();
                api.setId(rs.getInt(1));
                api.setPackageName(rs.getString(2));
                api.setClassName(rs.getString(3));
                api.setMethodName(rs.getString(4));
                api.setMethodReturnType(rs.getString(5));
                api.setMethodArgs(rs.getString(6));
                api.setrPackageName(rs.getString(7));
                api.setrClassName(rs.getString(8));
                api.setrMethodName(rs.getString(9));
                api.setrReturnType(rs.getString(10));
                api.setrMethodArgs(rs.getString(11));
                api.setrInvoker(rs.getString(12));
                api.setType(rs.getInt(13));
                api.setReplace(rs.getString(14));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResultset(rs);
            DBUtil.closePreparedStatement(preparedStatement);
            DBUtil.closeConnection(connection);
        }
        return api;
    }

    @Override
    public List<DeprecatedAPI> getDeprecatedAPI() {
        List<DeprecatedAPI> apis = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement(GET);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                DeprecatedAPI api = new DeprecatedAPI();
                api.setId(rs.getInt(1));
                api.setPackageName(rs.getString(2));
                api.setClassName(rs.getString(3));
                api.setMethodName(rs.getString(4));
                api.setMethodReturnType(rs.getString(5));
                api.setMethodArgs(rs.getString(6));
                api.setrPackageName(rs.getString(7));
                api.setrClassName(rs.getString(8));
                api.setrMethodName(rs.getString(9));
                api.setrReturnType(rs.getString(10));
                api.setrMethodArgs(rs.getString(11));
                api.setrInvoker(rs.getString(12));
                api.setType(rs.getInt(13));
                api.setReplace(rs.getString(14));
                apis.add(api);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResultset(rs);
            DBUtil.closePreparedStatement(preparedStatement);
            DBUtil.closeConnection(connection);
        }
        return apis;
    }

    @Override
    public DeprecatedAPI getByDeprecated(DeprecatedAPI api) {
        DeprecatedAPI ret = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement(GETBYDAPI);
            preparedStatement.setString(1, api.getPackageName());
            preparedStatement.setString(2, api.getClassName());
            preparedStatement.setString(3, api.getMethodName());
            preparedStatement.setString(4, api.getMethodReturnType());
            preparedStatement.setString(5, api.getMethodArgs());
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                ret = new DeprecatedAPI();
                ret.setId(rs.getInt(1));
                ret.setPackageName(rs.getString(2));
                ret.setClassName(rs.getString(3));
                ret.setMethodName(rs.getString(4));
                ret.setMethodReturnType(rs.getString(5));
                ret.setMethodArgs(rs.getString(6));
                ret.setrPackageName(rs.getString(7));
                ret.setrClassName(rs.getString(8));
                ret.setrMethodName(rs.getString(9));
                ret.setrReturnType(rs.getString(10));
                ret.setrMethodArgs(rs.getString(11));
                ret.setrInvoker(rs.getString(12));
                ret.setType(rs.getInt(13));
                ret.setReplace(rs.getString(14));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResultset(rs);
            DBUtil.closePreparedStatement(preparedStatement);
            DBUtil.closeConnection(connection);
        }
        return ret;
    }
}
