package com.applifit.bilifit.server.formulaires.DAO;

import java.util.List;

import com.applifit.bilifit.server.formulaires.Element;

public interface ElementDao {

	Element ajouterElement(Element element);

	List<Element> getElements();

	Element getElementByParams(int position_x, int position_y, Long form);

	List<Element> getElementsByForm(long formulaire);

	Element getElementById(long id);

}
