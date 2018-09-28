package shaverma.logic;

import shaverma.exceptions.ApplicationException;

public interface StorageAccessor extends Accessor {
    Storage getInstance() throws ApplicationException;
}
