package shaverma.data;

import shaverma.exceptions.ApplicationException;
import shaverma.logic.AccessorRegistry;
import shaverma.logic.Component;
import shaverma.logic.ComponentAccessor;
import shaverma.logic.Entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ComponentMapper extends BasicMapper implements ComponentAccessor{

    public ComponentMapper(String url, AccessorRegistry registry) {
        super(url, registry);
    }

    @Override
    Entity parseResultSetEntry(ResultSet resultSet, String idColumn) throws ApplicationException {
        try {
            int id = resultSet.getInt(idColumn);
            String name = resultSet.getString("name");
            return new Component(name, getRegistry(), id);
        } catch (SQLException e) {
            throw new ApplicationException(String.format("SQL exception: %s", e.getMessage()));
        }
    }

    @Override
    Map<String, Object> getDatabaseFields(Entity entity) {
        Map<String, Object> fieldMap = new HashMap<>();
        Component component = (Component) entity;
        fieldMap.put("name", component.getName());
        return fieldMap;
    }

    @Override
    String getTableNameBase() {
        return "component";
    }

    @Override
    public Optional<Component> getByName(String name) throws ApplicationException {
        try(Connection connection = getConnection()){
            String selectStatement = String.format((new StringBuilder())
                    .append("SELECT * ")
                    .append("FROM %s ")
                    .append("WHERE name = ? ")
                    .toString(), getTableName());
            PreparedStatement preparedSelectStatement = connection.prepareStatement(selectStatement);
            preparedSelectStatement.setString(1, name);
            ResultSet resultSet = preparedSelectStatement.executeQuery();
            if (resultSet.next()) {
                Component component = (Component) parseResultSetEntry(resultSet);
                return Optional.of(component);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new ApplicationException(String.format("SQL exception: %s", e.getMessage()));
        }
    }
}
