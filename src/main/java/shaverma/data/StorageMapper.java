package shaverma.data;

import shaverma.logic.Entity;
import shaverma.logic.Item;
import shaverma.logic.StorageAccessor;
import shaverma.logic.AccessorRegistry;
import shaverma.logic.Storage;
import shaverma.exceptions.ApplicationException;
import shaverma.util.Pair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StorageMapper extends BasicMapper implements StorageAccessor {

    private Storage cachedStorage;

    public StorageMapper(String url, AccessorRegistry registry) {
        super(url, registry);
    }

    @Override
    Entity parseResultSetEntry(ResultSet resultSet, String idColumn) throws ApplicationException {
        try {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            return new Storage(getRegistry(), name, id);
        } catch (SQLException e) {
            throw new ApplicationException(String.format("SQL exception: %s", e.getMessage()));
        }
    }

    @Override
    Map<String, Object> getDatabaseFields(Entity entity) {
        Map<String, Object> fieldMap = new HashMap<>();
        Storage storage = (Storage) entity;
        String name = storage.getName();
        fieldMap.put("name", name);
        return fieldMap;
    }

    @Override
    String getTableNameBase() {
        return "storage";
    }

    @Override
    Map<String, Pair<List<? extends Entity>, BasicMapper>> getM2MFields(Entity entity) {
        Map<String, Pair<List<? extends Entity>, BasicMapper>> m2mFields = new HashMap<>();
        Storage storage = (Storage) entity;
        BasicMapper itemMapper = (ItemMapper) getRegistry().getAccessor(Item.class);
        m2mFields.put("items", new Pair<>(storage.getItems(), itemMapper));
        return m2mFields;
    }

    @Override
    public Storage getInstance() throws ApplicationException {
        if (cachedStorage != null) {
            return cachedStorage;
        }
        List allRecords = getAll();
        if (allRecords.size() > 1) {
            System.out.println(">1");
            throw new ApplicationException("Multiple objects returned", ApplicationException.Type.SQL);
        }
        if (allRecords.size() == 0) {
            Storage storage = new Storage(getRegistry());
            storage.create();
            return storage;
        }
        cachedStorage = (Storage) allRecords.get(0);
        return cachedStorage;
    }

    @Override
    public int updateObject(Entity object) throws ApplicationException {
        Storage storage = (Storage) object;
        for (Item i: storage.getItems()) {
            if (i.getId() != 0)
                i.update();
            else {
                i.create();
            }
        }
        return super.updateObject(object);
    }
}
