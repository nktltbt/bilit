package com.applifit.bilifit.server.formulaires.DAO;

import java.util.ArrayList;
import java.util.List;

import com.applifit.bilifit.server.formulaires.Element_valeur;

public interface Element_valeurDao {

	@SuppressWarnings("rawtypes")
	ArrayList getDistinctCompteIndice(ArrayList<Long> e);

	Element_valeur ajouterValeur(String valeur, long element, long compte, String date,int indice);

	Element_valeur getValue(Long element, int indice);

	List<Element_valeur> getAllValues();

	List<Element_valeur> getValuesByIndice(int indice);

	void modifierValeur(String valeur, long element, int indice);

	@SuppressWarnings("rawtypes")
	ArrayList getDistinctCompteIndice(ArrayList<Long> e, long compte);


}
