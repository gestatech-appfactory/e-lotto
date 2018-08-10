package be.gestatech.elotto.infrastructure.persistence.migration.version;

import be.gestatech.elotto.infrastructure.persistence.migration.control.Migrateable;
import org.mongodb.morphia.Datastore;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unused")
public class V_000002 implements Migrateable {

    @Override
    public void executeMigration(Datastore datastore) throws IOException {
        BufferedReader in = getBufferedReader();
        String line;
        List<PlzCityState> plzCityStateList = new LinkedList<>();
        while ((line = in.readLine()) != null) {
            final String[] strings = line.split("\\t");
            PlzCityState plzCityState = new PlzCityState(strings[1], strings[2], strings[0]);
            plzCityStateList.add(plzCityState);
        }
        datastore.save(plzCityStateList);
    }

    BufferedReader getBufferedReader() throws IOException {
        final InputStream resourceAsStream = getInputStream();
        ByteArrayOutputStream downloadableFileStream = new ByteArrayOutputStream();
        int index;
        byte[] buffer = new byte[4096];
        while ((index = resourceAsStream.read(buffer)) != -1) {
            downloadableFileStream.write(buffer, 0, index);
        }
        ByteArrayInputStream bis = new ByteArrayInputStream(downloadableFileStream.toByteArray());
        return new BufferedReader(new InputStreamReader(bis, StandardCharsets.UTF_8));
    }

    InputStream getInputStream() {
        return V_000002.class.getResourceAsStream("OpenGeoDB_bundesland_plz_ort_de.csv");
    }
}
