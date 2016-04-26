package com.pji.de.awareway.utilitaires;

import com.pji.de.awareway.activity.CarteActivity;
import com.pji.de.awareway.liste.ListeNoeuds;

/**
 * Created by Geoffrey on 20/03/2016.
 */
public class FakeLocation extends Thread {

    private CarteActivity carteActivity;

    public FakeLocation(CarteActivity activityCard){
        this.carteActivity = activityCard;
    }
    /*@Override
    protected Void doInBackground(CarteActivity... carteActivities) {
        CarteActivity carteActivity = carteActivities[0];
        List<Location> locations = new ArrayList<>();
        try {
            AssetManager asm = carteActivity.getResources().getAssets();
            locations = ParseurCSVPosition.parseCSVToLocations(asm.open("locations1.csv"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int index = 0;
        while(index < locations.size()){
            Location location = locations.get(index);
            carteActivity.setLocation(location);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            index++;
        }

        return null;
    }*/
    @Override
    public void run() {
        ListeNoeuds nodeList = carteActivity.getNodeList();

        int index = 0;
        while(index < nodeList.size()){
            carteActivity.setLocation(index);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            index++;
        }
    }
}
