package com.pji.de.awareway.utilitaires;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.pji.de.awareway.bean.Relation;
import com.pji.de.awareway.bean.Poi;
import com.pji.de.awareway.bean.Noeud;
import com.pji.de.awareway.liste.ListeNoeuds;
import com.pji.de.awareway.liste.ListePois;

public class ParseurXmlToBean {

	public static final String POI_ELEMENT = "poi";
	public static final String LATITUDE_ELEMENT = "latitude";
	public static final String LONGITUD_ELEMENT = "longitude";
	public static final String VISIBLED_ELEMENT = "visibleDebut";
	public static final String VISIBLEF_ELEMENT = "visibleFin";
	public static final String COMMENTAIRE_ELEMENT = "commentaire";
	public static final String CATEGORIE_ELEMENT = "categorie";
	public static final String NOM_ELEMENT = "name";
	public static final String VALID_ELEMENT = "valid";
	public static final String ID_ATTRIBUT = "id";
	public static final String LIEN_ELEMENT="lien";
	public static final String LIEN_WEB_ELEMENT="lienweb";
	
	public static final String RELATION_ELEMENT = "relation";
	
	public static final String NODE_ELEMENT = "node";
	public static final String GARE_ELEMENT = "gare";


	public static ListePois parseXmlToPoiList(String xmlDocument) {

		ListePois listePoi = new ListePois();

		NodeList liste = getNodeListFromXml(xmlDocument);

		for (int i = 0; i < liste.getLength(); i++) {
			Node item = liste.item(i);

			if (item.getNodeName().equals(POI_ELEMENT)) {
				Poi poi = new Poi();
				Map<String, String> caracteristiquesPoi = new HashMap<String, String>();

				poi.setId(item.getAttributes().getNamedItem(ID_ATTRIBUT)
						.getNodeValue());

				NodeList listeChild = item.getChildNodes();
				for (int y = 0; y < listeChild.getLength(); y++) {
					Node itemChild = listeChild.item(y);
					caracteristiquesPoi.put(itemChild.getNodeName(),
							itemChild.getTextContent());
				}

				poi.setValid(Boolean.getBoolean(caracteristiquesPoi.get(VALID_ELEMENT)));

				poi.setNom(caracteristiquesPoi.get(NOM_ELEMENT));
				poi.setCategorie(caracteristiquesPoi.get(CATEGORIE_ELEMENT));
				poi.setCommentaire(caracteristiquesPoi.get(COMMENTAIRE_ELEMENT));
				poi.setDebutVisible(Float.valueOf(caracteristiquesPoi
						.get(VISIBLED_ELEMENT)));
				poi.setFinVisible(Float.valueOf(caracteristiquesPoi
						.get(VISIBLEF_ELEMENT)));
				poi.setLat(caracteristiquesPoi.get(LATITUDE_ELEMENT));
				poi.setLon(caracteristiquesPoi.get(LONGITUD_ELEMENT));
				poi.setLienImage(caracteristiquesPoi.get(LIEN_ELEMENT));
				poi.setLienWeb(caracteristiquesPoi.get(LIEN_WEB_ELEMENT));
				
				listePoi.add(poi);
			}

		}

		return listePoi;
	}
	
	public static List<Relation> parseXmlToRelationList(String xmlDocument) {

		List<Relation> listeRelation= new ArrayList<Relation>();

		NodeList liste = getNodeListFromXml(xmlDocument);

		for (int i = 0; i < liste.getLength(); i++) {
			Node item = liste.item(i);

			if (item.getNodeName().equals(RELATION_ELEMENT)) {
				Relation relation = new Relation();

				relation.setIdentifiant((item.getAttributes().getNamedItem(ID_ATTRIBUT).getNodeValue()));
				relation.setNom(item.getTextContent());
				listeRelation.add(relation);
			}

		}

		return listeRelation;
	}
	
	public static ListeNoeuds parseXmlToNoeudList(String xmlDocument) {

		ListeNoeuds listeNoeud = new ListeNoeuds();

		NodeList liste = getNodeListFromXml(xmlDocument);

		for (int i = 0; i < liste.getLength(); i++) {
			Node item = liste.item(i);

			if (item.getNodeName().equals(NODE_ELEMENT)) {
				Noeud poi = new Noeud();
				Map<String, String> caracteristiquesPoi = new HashMap<String, String>();

				poi.setId(item.getAttributes().getNamedItem(ID_ATTRIBUT)
						.getNodeValue());

				NodeList listeChild = item.getChildNodes();
				for (int y = 0; y < listeChild.getLength(); y++) {
					Node itemChild = listeChild.item(y);
					caracteristiquesPoi.put(itemChild.getNodeName(),
							itemChild.getTextContent());
				}

				poi.setNom(caracteristiquesPoi.get(NOM_ELEMENT));
				poi.setLat(caracteristiquesPoi.get(LATITUDE_ELEMENT));
				poi.setLon(caracteristiquesPoi.get(LONGITUD_ELEMENT));
				poi.setEstUneGare(caracteristiquesPoi.get(GARE_ELEMENT) == null ? false : true);
				
				listeNoeud.add(poi);
			}

		}

		return listeNoeud;
	}

	private static NodeList getNodeListFromXml(String xmlDocument) {

		NodeList liste = null;

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlDocument));
			Document doc = builder.parse(is);

			Element root = doc.getDocumentElement();

			liste = root.getChildNodes();

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return liste;
	}

}
