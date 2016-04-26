package com.pji.de.awareway.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Noeud implements Parcelable, Comparable<Noeud> {

	private String id;

	private String lat;

	private String lon;

	private String nom;

	private String idRelation;

	private boolean estUneGare;

	public Noeud() {
		super();
	}

	public Noeud(String id, String lat, String lon, String nom,
			String idRelation, boolean estUneGare) {
		super();
		this.id = id;
		this.lat = lat;
		this.lon = lon;
		this.nom = nom;
		this.idRelation = idRelation;
		this.estUneGare = estUneGare;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getIdRelation() {
		return idRelation;
	}

	public void setIdRelation(String idRelation) {
		this.idRelation = idRelation;
	}

	public boolean isEstUneGare() {
		return estUneGare;
	}

	public void setEstUneGare(boolean estUneGare) {
		this.estUneGare = estUneGare;
	}

	public Noeud(Parcel in) {
		this.getFromParcel(in);
	}

	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public Noeud createFromParcel(Parcel in) {
			return new Noeud(in);
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

	// On ecrit dans le parcel les donnees de notre objet
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.nom);
		dest.writeString(this.lat);
		dest.writeString(this.lon);
		dest.writeString(this.id);
		boolean[] tab = new boolean[1];
		tab[0] = this.estUneGare;
		dest.writeBooleanArray(tab);

	}

	// On va ici hydrater notre objet a partir du Parcel
	public void getFromParcel(Parcel in) {
		this.setNom(in.readString());
		this.setLat(in.readString());
		this.setLon(in.readString());
		this.setId(in.readString());
		boolean[] tab = new boolean[1];
		in.readBooleanArray(tab);
		this.setEstUneGare(tab[0]);

	}

	// si un point a une latitude sup�rieur � l'autre il est plus en haut
	// si un point a une longitude sup�rieur � l'autre il est plus � droite
	@Override
	public int compareTo(Noeud another) {
		
		if (Double.valueOf(this.getLat()) > Double.valueOf(another
						.getLat())) {
			return 1;
		} else if ( Double.valueOf(this.getLat()) < Double.valueOf(another
						.getLat())) {
			return -1;
		} else {
			return 0;
		}

	}

}
