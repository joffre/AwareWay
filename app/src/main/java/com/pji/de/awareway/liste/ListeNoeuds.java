package com.pji.de.awareway.liste;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;
import com.pji.de.awareway.bean.Noeud;

public class ListeNoeuds extends ArrayList<Noeud> implements Parcelable {
	
	 public ListeNoeuds()
	    {
	 
	    }
	 
	    public ListeNoeuds(Parcel in)
	    {
	        this.getFromParcel(in);
	    }
	 
	    @SuppressWarnings("rawtypes")
	    public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
	    {
	        public ListeNoeuds createFromParcel(Parcel in)
	        {
	            return new ListeNoeuds(in);
	        }
	 
	        @Override
	        public Object[] newArray(int size) {
	            return null;
	        }
	    };
	 
	    @Override
	    public int describeContents() {
	        return 0;
	    }
	 
	    @Override
	    public void writeToParcel(Parcel dest, int flags)
	    {
	        //Taille de la liste
	        int size = this.size();
	        dest.writeInt(size);
	        for(int i=0; i < size; i++)
	        {
	            Noeud noeud = this.get(i); //On vient lire chaque objet personne
		        dest.writeString(noeud.getNom());
		        dest.writeString(noeud.getLat());
		        dest.writeString(noeud.getLon());
		        dest.writeString(noeud.getId());
		        boolean[] tab = new boolean[1];
		        tab[0] = noeud.isEstUneGare();
		        dest.writeBooleanArray(tab);
	        }
	    }
	 
	    public void getFromParcel(Parcel in)
	    {
	        // On vide la liste avant tout remplissage
	        this.clear();
	 
	        //Recuperation du nombre d'objet
	        int size = in.readInt();
	 
	        //On repeuple la liste avec de nouveau objet
	        for(int i = 0; i < size; i++)
	        {
	            Noeud noeud = new Noeud();
	            noeud.setNom(in.readString());
	            noeud.setLat(in.readString());
	            noeud.setLon(in.readString());
	            noeud.setId(in.readString());
		        boolean[] tab = new boolean[1];
		        in.readBooleanArray(tab);
		        noeud.setEstUneGare(tab[0]);
	            this.add(noeud);
	        }
	 
	    }

}
