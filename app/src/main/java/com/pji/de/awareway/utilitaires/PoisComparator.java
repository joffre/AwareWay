package com.pji.de.awareway.utilitaires;

import java.util.Comparator;

import com.pji.de.awareway.bean.Poi;

/**
 * Created by Sim on 29/03/2016.
 */
public class PoisComparator implements Comparator<Poi> {
    @Override
    public int compare(Poi poi1, Poi poi2) {
        return poi1.getPointKilometrique() < poi2.getPointKilometrique() ? -1 : 1;
    }
}
