package shaverma.exceptions;

public class ApplicationException extends RuntimeException {

    private Type type;

    public enum Type {
        DEFAULT,
        ORDER_STATUS,
        STORAGE,
        SQL,
        REFLECTION
    }

    public ApplicationException(String message, Type type) {
        super(message);
        this.type = type;
    }

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException() {
        super();
    }

    public Type getType() {
        return type;
    }
}
