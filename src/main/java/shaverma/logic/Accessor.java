package shaverma.logic;

import shaverma.exceptions.ApplicationException;

import java.util.List;

public interface Accessor {

    Entity getById(int id) throws ApplicationException;

    List<? extends Entity> getAll() throws ApplicationException;

    int saveObject(Entity object) throws ApplicationException;

    int updateObject(Entity object) throws ApplicationException;

    AccessorRegistry getRegistry();

    List<? extends Entity> getRelatedList(int id, Accessor accessor) throws ApplicationException;
}
