package com.pji.de.awareway.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Poi implements Parcelable, Comparable<Poi> {

	private String id;

	private boolean valid;

	private String lat;

	private String lon;

	private String nom;

	private String commentaire;

	private String categorie;

	private String idRelation;

	private float debutVisible;

	private float finVisible;

	private float distance;

	private float pointKilometrique;

	private String lienImage;

	private String lienWeb;

	public String getLienImage() {
		return lienImage;
	}

	public void setLienImage(String lienImage) {
		this.lienImage = lienImage;
	}

	public float getPointKilometrique() {
		return pointKilometrique;
	}

	public void setPointKilometrique(float pointKilometrique) {
		this.pointKilometrique = pointKilometrique;
	}

	public Poi(String id, String lat, String lon, String nom,
			   String commentaire, String categorie, String idRelation,
			   int debutVisible, int finVisible) {
		super();
		this.id = id;
		this.lat = lat;
		this.lon = lon;
		this.nom = nom;
		this.commentaire = commentaire;
		this.categorie = categorie;
		this.idRelation = idRelation;
		this.debutVisible = debutVisible;
		this.finVisible = finVisible;
	}

	public Poi() {
		super();
	}

	public boolean getValid(){
		return this.valid;
	}

	public void setValid(boolean valid){
		this.valid = valid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distancet) {
		this.distance = distancet;
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

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public String getCategorie() {
		return categorie;
	}

	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}

	public String getIdRelation() {
		return idRelation;
	}

	public void setIdRelation(String idRelation) {
		this.idRelation = idRelation;
	}

	public float getDebutVisible() {
		return debutVisible;
	}

	public void setDebutVisible(float debutVisible) {
		this.debutVisible = debutVisible;
	}

	public float getFinVisible() {
		return finVisible;
	}

	public void setFinVisible(float finVisible) {
		this.finVisible = finVisible;
	}

	public Poi(Parcel in) {
		this.getFromParcel(in);
	}

	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public Poi createFromParcel(Parcel in) {
			return new Poi(in);
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
		dest.writeString(this.commentaire);
		dest.writeString(this.categorie);
		dest.writeFloat(this.debutVisible);
		dest.writeFloat(this.finVisible);
		dest.writeString(this.lienImage);
		dest.writeString(this.lienWeb);

	}

	// On va ici hydrater notre objet � partir du Parcel
	public void getFromParcel(Parcel in) {
		this.setNom(in.readString());
		this.setLat(in.readString());
		this.setLon(in.readString());
		this.setId(in.readString());
		this.setCommentaire(in.readString());
		this.setCategorie(in.readString());
		this.setDebutVisible(in.readFloat());
		this.setFinVisible(in.readFloat());
		this.setLienImage((in.readString()));
		this.setLienWeb(in.readString());

	}

	@Override
	public int compareTo(Poi another) {
		if(distance < another.getDistance()){
			return -1;
		}else{
			return 1;
		}
	}

	public String getLienWeb() {
		return lienWeb;
	}

	public void setLienWeb(String lienWeb) {
		this.lienWeb = lienWeb;
	}

}
