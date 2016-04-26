package com.pji.de.awareway.bean;

public class Relation {
	
	private String identifiant;
	private String nom;
	
	public Relation(){
		super();
	}
	
	public Relation(String identifiant, String nom) {
		super();
		this.identifiant = identifiant;
		this.nom = nom;
	}

	public String getIdentifiant() {
		return identifiant;
	}

	public void setIdentifiant(String identifiant) {
		this.identifiant = identifiant;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

}
