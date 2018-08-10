package be.gestatech.elotto.infrastructure.persistence.encryption;

import org.mongodb.morphia.converters.SimpleValueConverter;
import org.mongodb.morphia.converters.TypeConverter;
import org.mongodb.morphia.mapping.MappedField;
import org.mongodb.morphia.mapping.MappingException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class EncryptedFieldConverter extends TypeConverter implements SimpleValueConverter {

    private static final Charset ENCODING = Charset.forName("UTF-8");

    private static final Class[] validDataTypedForEncryptionAsArray = new Class[]{EncryptedFieldValueWrapper.class, String.class, byte[].class, Integer.class, EncryptedZonedDateTime.class};

    private static final List<Class> validDataTypedForEncryption = Arrays.asList(validDataTypedForEncryptionAsArray);

    private EncryptionService encryptionService;


    public EncryptedFieldConverter() {
        super(validDataTypedForEncryptionAsArray);
    }

    @Override
    public Object encode(Object value, MappedField optionalExtraInfo) {
        if (Objects.isNull(value)) {
            return null;
        }
        byte[] contentToEncrypt;
        if (value instanceof EncryptedFieldValueWrapper) {
            contentToEncrypt = ((EncryptedFieldValueWrapper) value).getValue();
        } else if ((Objects.isNull(optionalExtraInfo)) || (!optionalExtraInfo.getField().isAnnotationPresent(Encrypted.class))) {
            return value;
        } else if (optionalExtraInfo.getField().isAnnotationPresent(Encrypted.class) && (!isValidDataTypeAnnotated(optionalExtraInfo))) {
            throw new MappingException("The annotation @Encrypted can only be applied to fields of type String, byte[] and Integer");
        } else if (optionalExtraInfo.getType() == byte[].class) {
            contentToEncrypt = (byte[]) value;
        } else if (optionalExtraInfo.getType() == EncryptedZonedDateTime.class) {
            contentToEncrypt = Long.valueOf(((EncryptedZonedDateTime) value).getZonedDateTime().toEpochSecond() * 1000).toString().getBytes(ENCODING);
        } else {
            String stringValue = value.toString();
            contentToEncrypt = stringValue.getBytes(ENCODING);
        }
        return getEncryptionService().encryptData(contentToEncrypt);
    }

    private boolean isValidDataTypeAnnotated(final MappedField optionalExtraInfo) {
        return (validDataTypedForEncryption.contains(optionalExtraInfo.getType()));
    }


    @Override
    public Object decode(Class<?> targetClass, Object fromDBObject, MappedField optionalExtraInfo) {
        if (Objects.isNull(fromDBObject) || !(fromDBObject instanceof byte[]) || (!optionalExtraInfo.getField().isAnnotationPresent(Encrypted.class))) {
            return fromDBObject;
        }
        byte[] decrypted = getEncryptionService().decryptData((byte[]) fromDBObject);
        if (optionalExtraInfo.getType() == String.class) {
            return new String(decrypted, ENCODING);
        } else if (optionalExtraInfo.getType() == Integer.class) {
            String string = new String(decrypted, ENCODING);
            return Integer.parseInt(string);
        } else if (optionalExtraInfo.getType() == EncryptedZonedDateTime.class) {
            String string = new String(decrypted, ENCODING);
            return new EncryptedZonedDateTime(ZonedDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(string)), ZoneId.of("UTC")));
        } else {
            return decrypted;
        }
    }

    public EncryptionService getEncryptionService() {
        if (Objects.isNull(encryptionService )) {
            try {
                InitialContext jndi = new InitialContext();
                encryptionService = (EncryptionService) jndi.lookup("java:global/lottoritter/EncryptionService");
            } catch (NamingException nex) {
                return new EncryptionService();
            }
        }
        return encryptionService;
    }
}
