package shaverma.logic;

import shaverma.exceptions.ApplicationException;

import java.util.Optional;

public interface ComponentAccessor extends Accessor {
    Optional<Component> getByName(String name) throws ApplicationException;
}
