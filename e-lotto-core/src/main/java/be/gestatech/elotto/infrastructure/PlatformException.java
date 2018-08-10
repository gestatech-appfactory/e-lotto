package be.gestatech.elotto.infrastructure;

public class PlatformException extends RuntimeException {

    private static final long serialVersionUID = -3144333989833509239L;

    public PlatformException(Exception causedException) {
        super(causedException);
    }
}
