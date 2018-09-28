package shaverma.logic;

public interface User {

    enum Role {
        CLIENT,
        MANAGER,
        PROVIDER,
        NONE
    }

    Role getRole();

    String getName();
}
