package be.gestatech.elotto.infrastructure.persistence.encryption;

import java.nio.charset.Charset;
import java.util.Objects;

public class EncryptedFieldValueWrapper {

    private static final Charset ENCODING = Charset.forName("UTF-8");

    private final byte[] value;


    /**
     * Use this for the encrypted field value.
     */
    public EncryptedFieldValueWrapper(byte[] value) {
        this.value = value;
    }

    /**
     * Use this for the unencrypted field value
     */
    public EncryptedFieldValueWrapper(Object value) {
        Objects.requireNonNull(value, String.format("The value of [%s] should not be null", value));
        String stringValue = value.toString();
        this.value = stringValue.getBytes(ENCODING);
    }

    public byte[] getValue() {
        return value;
    }
}
