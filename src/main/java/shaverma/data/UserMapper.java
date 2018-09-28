package shaverma.data;

import shaverma.logic.Entity;
import shaverma.logic.UserAccessor;
import shaverma.logic.BaseUser;
import shaverma.logic.AccessorRegistry;
import shaverma.logic.User;
import shaverma.exceptions.ApplicationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserMapper extends BasicMapper implements UserAccessor {

    public UserMapper(String url, AccessorRegistry registry) {
        super(url, registry);
    }

    @Override
    Entity parseResultSetEntry(ResultSet resultSet, String idColumn) throws ApplicationException {
        try {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            return new BaseUser(name, getRegistry(), id);
        } catch (SQLException e) {
            throw new ApplicationException(String.format("SQL exception: %s", e.getMessage()));
        }
    }

    @Override
    Map<String, Object> getDatabaseFields(Entity entity) {
        Map<String, Object> fieldMap = new HashMap<>();
        User user = (User) entity;
        fieldMap.put("name", user.getName());
        return fieldMap;
    }

    @Override
    String getTableNameBase() {
        return "user";
    }

    @Override
    public void dropData() throws SQLException {
        Connection c = getConnection();
        String deleteStatementSQL = "TRUNCATE public.shav_user_role CASCADE ";
        PreparedStatement deleteStatement = c.prepareStatement(deleteStatementSQL);
        deleteStatement.execute();
        super.dropData();
    }

    @Override
    public BaseUser getUser(String userName, User.Role role) throws ApplicationException{
        try {
            Connection connection = getConnection();
            String roleStringLiteral = role.name();
            boolean withRole = role != User.Role.NONE;
            StringBuilder selectBuilder = (new StringBuilder())
                    .append("SELECT u.id ")
                    .append("FROM shav_user u ");
            if (withRole)
                selectBuilder
                        .append("JOIN shav_user_role ur on u.id = ur.user_id ")
                        .append("JOIN shav_role r on r.id = ur.role_id ");
            selectBuilder.append("WHERE u.name = ? ");
            if (withRole)
                selectBuilder.append("AND r.name = ?");
            String selectString = selectBuilder.toString();

            PreparedStatement selectStatement = connection.prepareStatement(selectString);
            selectStatement.setString(1, userName);
            if (withRole)
                selectStatement.setString(2, roleStringLiteral);
            ResultSet resultSet = selectStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                BaseUser user = getRegistry().newUser(userName, role);
                user.setId(id);
                return user;
            }
            return null;
        } catch (SQLException e) {
            throw new ApplicationException(String.format("SQL exception: %s", e.getMessage()));
        }
    }

    @Override
    public int addUser(String userName, User.Role role) throws ApplicationException{
        try {
            Connection connection = getConnection();
            String roleStringLiteral = role.name();
            String insertString = (new StringBuilder())
                    .append("INSERT INTO shav_user (name) ")
                    .append("VALUES (?) ")
                    .append("RETURNING id")
                    .toString();

            PreparedStatement insertUserStatement = connection.prepareStatement(insertString);
            insertUserStatement.setString(1, userName);
            ResultSet resultSet = insertUserStatement.executeQuery();
            if (resultSet.next()) {
                int newUserId = resultSet.getInt("id");
                String addRoleString = (new StringBuilder())
                        .append("INSERT INTO shav_user_role (user_id, role_id) ")
                        .append("SELECT ? user_id, id as role_id ")
                        .append("FROM shav_role ")
                        .append("WHERE name = ? ")
                        .append("RETURNING id ")
                        .toString();
                PreparedStatement insertRoleStatement = connection.prepareStatement(addRoleString);
                insertRoleStatement.setInt(1, newUserId);
                insertRoleStatement.setString(2, roleStringLiteral);
                insertRoleStatement.executeQuery();
                return newUserId;
            }
            throw new ApplicationException(String.format("User was not created"));
        } catch (SQLException e) {
            throw new ApplicationException(String.format("SQL exception: %s", e.getMessage()));
        }
    }

    @Override
    public List<BaseUser> getAllUsers(User.Role role) {
        return null;
    }
}
