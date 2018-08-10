package be.gestatech.elotto.infrastructure.persistence.migration.version;

import be.gestatech.elotto.infrastructure.persistence.migration.control.Migrateable;
import org.mongodb.morphia.Datastore;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unused")
public class V_000001 implements Migrateable {

//    private int[] fixedLengthFields = {8, 1, 58, 5, 35, 27, 5, 11, 2, 6, 10};

    @Override
    public void executeMigration(Datastore datastore) throws IOException {
        final BufferedReader in = getBufferedReaderForFile();
        String line;
        String lastBic = "";
        List<Bank> bankList = new LinkedList<>();
        while ((line = in.readLine()) != null) {
            String bic = line.substring(139, 150).trim();
            if (bic.length() == 0) {
                bic = lastBic;
            } else {
                lastBic = bic;
            }
            Bank bank = new Bank(line.substring(0, 8),
                    line.substring(9, 67).trim(),
                    line.substring(67, 72).trim(),
                    line.substring(72, 107).trim(),
                    line.substring(107, 134).trim(),
                    bic);
            bankList.add(bank);
        }
        datastore.save(bankList);
    }

    BufferedReader getBufferedReaderForFile() throws IOException {
        final InputStream resourceAsStream = getInputStream();
        ByteArrayOutputStream downloadableFileStream = new ByteArrayOutputStream();
        int index;
        byte[] buffer = new byte[4096];
        while ((index = resourceAsStream.read(buffer)) != -1) {
            downloadableFileStream.write(buffer, 0, index);
        }
        ByteArrayInputStream bis = new ByteArrayInputStream(downloadableFileStream.toByteArray());
        return new BufferedReader(new InputStreamReader(bis, StandardCharsets.ISO_8859_1));
    }

    InputStream getInputStream() {
        return V_000001.class.getResourceAsStream("blz_2017_03_06_txt.txt");
    }
}
