package com.pji.de.awareway.utilitaires;

import android.location.Location;
import android.location.LocationManager;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Geoffrey on 20/03/2016.
 */
public class ParseurCSVPosition {
    
    public static List<Location> parseCSVToLocations(InputStream CSVDocument) throws IOException {
        List<Location> locations = new ArrayList<Location>();

        InputStreamReader is = new InputStreamReader(CSVDocument);

        BufferedReader reader = new BufferedReader(is);
        reader.readLine();
        String line;
        while ((line = reader.readLine()) != null) {
            String[] elems = line.split(";");
            Location location = new Location(LocationManager.GPS_PROVIDER);
            location.setAltitude(Double.parseDouble(elems[0]));
            location.setLongitude(Double.parseDouble(elems[1]));
            locations.add(location);
        }
        return locations;
    }
}
