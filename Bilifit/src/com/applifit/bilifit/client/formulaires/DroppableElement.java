package com.applifit.bilifit.client.formulaires;

import static com.applifit.bilifit.client.formulaires.FormulaireBoard.SQUARE_NUMBER_col;
import static com.applifit.bilifit.client.formulaires.FormulaireBoard.SQUARE_NUMBER_ligne;
import static com.applifit.bilifit.client.formulaires.FormulaireController.CHOICE;
import static com.applifit.bilifit.client.formulaires.FormulaireController.OPTION;
import static com.google.gwt.query.client.GQuery.$;
import gwtquery.plugins.droppable.client.events.DropEvent;
import gwtquery.plugins.droppable.client.events.DropEvent.DropEventHandler;
import gwtquery.plugins.droppable.client.gwt.DroppableWidget;

import java.util.ArrayList;
import java.util.Iterator;

import com.applifit.bilifit.client.formulaires.FormulaireController.Position;
import com.applifit.bilifit.client.outils.Utils;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class DroppableElement extends DroppableWidget<HorizontalPanel>
		implements HasWidgets, DropEventHandler {

	/*** Position (X, Y) de l'element ***/
	public Position position;
	/*** True si l'element est parmis les choix d'un element checkbox ou radio ***/
	public boolean choix = false;
	/*** Le type de l'element (text , radio, combobox, time ....) ***/
	public String type = null;
	/*** Utilisé pour regrouper les differents choix d'un element ***/
	public String name = null;
	/***
	 * Utilisé pour regrouper les differentes options d'un champs liste
	 * déroulante
	 ***/
	public String option = null;
	public ArrayList<Boolean> listChoix = null;
	public ArrayList<String> listValue = null;
	public ArrayList<String> listName = null;

	/*** Constructeur d'un element droppable (Les cases de la partie création) ***/
	public DroppableElement(Position p) {
		this.position = p;
		init();
	}

	/*** Vider la case ***/
	public void clear() {
		((HorizontalPanel) getWidget()).clear();

	}

	/*** Rendre la case capable de recevoir un element ***/

	public void enable() {
		setDisabled(false);
	}

	public Iterator<Widget> iterator() {
		return ((HorizontalPanel) getWidget()).iterator();
	}

	/***
	 * Rendre la case incapable de recevoir un element (Utilisée dans le cas où
	 * la case est déja pleine)
	 ***/
	public void lock() {
		setDisabled(true);
	}

	/*** Evenement de drop de la cellule ***/
	public void onDrop(DropEvent event) {

		/** Récuperer l'image à recevoir ***/
		final Element draggingPiece = (Element) event.getDraggableWidget();
		// Pour faire disparaiter l'image

		draggingPiece.getElement().getStyle().setTop(0, Unit.PX);
		draggingPiece.getElement().getStyle().setLeft(0, Unit.PX);
		Position p = null;
		// Ajouter le champs correspondant à la case
		boolean kt = false;
		if (draggingPiece.getType().equals("deplace")) {
			kt = true;
			p = draggingPiece.getPosition();
		}
		add(draggingPiece);

		/***
		 * si l'image draguée correspond à un chmaps radio, checkbox ou
		 * combobox, on fait apparaitre le popup de "nombre de choix"
		 ***/

		if (kt) {
			delete(p);
			int si = listChoix.size() + 1;
			if (si > 1 && si - 1 > getDistance() && getDistance() != -1) {
				int distance = getDistance();
				insertNewRow(si - 1, type);
				ordonnerCell(si - 1);
				if (distance > 0)
					ordonnerCol(si, distance);
			} else if (si - 1 <= getDistance() || getDistance() == -1) {
				if (position.getY() + si > FormulaireBoard.SQUARE_NUMBER_ligne) {
					insertNewRow(si - 1, type);
					ordonnerCell(si - 1);
				} else {
					for (int i = 1; i < si; i++) {
						// Ajouter le chmaps de choix
						ajouterChoix(position.getY() + i, position.getX(),
								type, null);
					}
				}
			}
			remplir();
		} else {
			if (draggingPiece.getType().equals("checkbox")
					|| draggingPiece.getType().equals("radio")
					|| draggingPiece.getType().equals("combobox"))
				createPopUpChoix(draggingPiece.getType()).show();
			// Rendre la case incapable de recevoir un element puisqu'elle est
			// pleine maintenant
		}
		lock();

	}

	/***
	 * Création de la fenetre de choix du nombre de choix Retourne la fenêtre
	 * dialogueBox aprés sa création
	 * ***/
	public DialogBox createPopUpChoix(final String type) {
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Informations additionnelles");
		dialogBox.setAnimationEnabled(true);
		Button validerButton = new Button("Valider");
		validerButton.getElement().setId("valideButton");
		Label textToServerLabel = new Label();
		// Création du champs de type number avec minimum du nombre saisi egal à
		// 1
		final HTML nbr_choix = new HTML(
				"<input type='number' id='number' min='1' style='border: 1px solid #ccc;' />");
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.setWidth("300px");
		dialogVPanel.add(new HTML("<b>Nombre de valeurs:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(nbr_choix);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(validerButton);
		dialogBox.setWidget(dialogVPanel);
		dialogBox.setPopupPosition(450, 450);

		/*** Action de clique sur le button de valisation ***/
		validerButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// Récuperer le champs input pour pouvoir récuperer sa valeur
				InputElement element = nbr_choix.getElement()
						.getElementsByTagName("INPUT").getItem(0).cast();
				int size = Integer.parseInt(element.getValue());
				if (type == "checkbox" || type == "radio") {
					// Ajouter des lignes aprés l'element ajouté
					if (size > 1 && size - 1 > getDistance()
							&& getDistance() != -1) {
						int distance = getDistance();
						insertNewRow(size - 1, type);
						ordonnerCell(size - 1);
						if (distance > 0)
							ordonnerCol(size, distance);
					} else if (size - 1 <= getDistance() || getDistance() == -1) {
						if (position.getY() + size > FormulaireBoard.SQUARE_NUMBER_ligne) {
							insertNewRow(size - 1, type);
							ordonnerCell(size - 1);
						} else {
							for (int i = 1; i < size; i++) {
								// Ajouter le chmaps de choix
								ajouterChoix(position.getY() + i,
										position.getX(), type, null);
							}
						}
						CHOICE++;
					} else if (size == 1)
						CHOICE++;
					// Faire disparaitre le popup
					dialogBox.hide();
					// Repositionner les cellules et décaler les champs de type
					// choix

				} else {
					dialogBox.hide();
					// Si le champs est de type combobox, on crée une suite de
					// popups pour la saisie des options
					createPopUpOptions(Integer.parseInt(element.getValue()), 1)
							.show();
				}

			}
		});

		return dialogBox;
	}

	/***
	 * Création de la fenetre de saisie des options Retourne la fenêtre
	 * dialogueBox aprés sa création
	 * ***/
	public DialogBox createPopUpOptions(final int nbr, final int ordre) {
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Informations additionnelles");
		dialogBox.setAnimationEnabled(true);
		Button validerButton = new Button("Suivant");
		validerButton.getElement().setId("valideButton");
		Label textToServerLabel = new Label();
		// Création du champs de type text
		final HTML option = new HTML(
				"<input type='text' id='option' min='1' style='border: 1px solid #ccc;' />");
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.setWidth("300px");
		dialogVPanel.add(new HTML("<b>Valeur " + ordre + ":</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(option);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(validerButton);
		dialogBox.setWidget(dialogVPanel);
		dialogBox.setPopupPosition(450, 450);

		/*** Action de clique sur le button de valisation ***/
		validerButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// Récuperer le champs input pour pouvoir récuperer sa valeur
				InputElement element = option.getElement()
						.getElementsByTagName("INPUT").getItem(0).cast();

				// Ajouter l'option au champs combobox
				ListBox lb = (ListBox) ((HorizontalPanel) getWidget())
						.getWidget(1);
				lb.addItem(element.getValue(), element.getValue());

				if (nbr > 1) {
					dialogBox.hide();
					// Recurcivité pour afficher le popup de saisie nbr fois
					createPopUpOptions(nbr - 1, ordre + 1).show();

				} else {
					dialogBox.hide();
				}
			}
		});

		return dialogBox;
	}

	/***
	 * Insère nbr ligne aprés la ligne du champs ajouté
	 * 
	 * @param nbr
	 *            , le nombre de ligne à ajouter
	 * @param type
	 *            , le type des champs à ajouter
	 */
	public void insertNewRow(int nbr, String type) {

		FormulaireBoard c = (FormulaireBoard) $("#formboard").widget();
		Widget cell = null;
		for (int i = 0; i < nbr; i++) {
			// Insérer une ligne aprés la position de la cellule
			c.insertRow(position.getY() + 1);
			// Remplire les cases ajoutées par des cellules droppables
			for (int j = 0; j < SQUARE_NUMBER_col; j++) {
				cell = new DroppableElement(new Position(position.getX(),
						position.getY() + 1));
				cell.addStyleName("container_formulaire_type");
				c.setWidget(position.getY() + 1, j, cell);
			}
			// Ajouter le chmaps de choix
			ajouterChoix(position.getY() + 1, position.getX(), type, null);
		}

		// Mettre à jours le nombre de ligne dans l'inteface de création
		SQUARE_NUMBER_ligne = SQUARE_NUMBER_ligne + nbr;
		// Incrémenter le nombre static choice , utilisé comme attribut name
		// pour les champs ajoutés
		CHOICE++;

	}

	/**
	 * Récuperer la position du premier element existant au dessous du nouveau
	 * element ajouté
	 */
	public int getDistance() {
		Boolean found = false;
		int j = position.getY() + 1;
		final FormulaireController gc = FormulaireController.getInstance();
		FormulaireBoard cb = gc.getCheckerBoard();
		while (!found && j < SQUARE_NUMBER_ligne) {
			DroppableElement cellule = (DroppableElement) cb.getCell(j,
					position.getX());
			if (cellule.isDisabled()) {
				found = true;
			}
			j++;
		}
		if (!found)
			return -1;
		else
			return j - 2 - position.getY();
	}

	/**
	 * Ajouter un champs de choix du type "type" et position (x,y)
	 * 
	 * @param x
	 *            , la column d'ajout du champs
	 * @param y
	 *            , la ligne d'ajout du champs
	 * @param type
	 *            , le type de champs à ajouter
	 */
	public static void ajouterChoix(int x, int y, String type,
			String label_choix) {

		final FormulaireController gc = FormulaireController.getInstance();
		FormulaireBoard cb = gc.getCheckerBoard();
		Widget element = null;
		TextBox label = new TextBox();
		label.addStyleName("inputLabel labelChoix");
		if (label_choix != null) {
			label.setText(label_choix);
		} else {
			label.getElement().setAttribute("placeholder", "Choix");
		}

		DroppableElement cellule = (DroppableElement) cb.getCell(x, y);
		cellule.setChoix(true);
		if (type.equals("checkbox")) {
			CheckBox c = new CheckBox();
			c.setStyleName("checkChoix");
			element = c;
			cellule.type = "checkbox";
		} else if (type.equals("radio")) {
			RadioButton r = new RadioButton("Choix");
			r.setStyleName("checkChoix");
			element = r;
			cellule.type = "radio";
		}
		cellule.name = "" + CHOICE;
		HorizontalPanel hp = cellule.getWidgetElement();
		hp.add(element);
		hp.add(label);
		cellule.choix = true;
		cellule.lock();
	}

	/**
	 * Repositionner les cellules et décaler les champs du formulaire
	 * 
	 * @param nbr
	 *            , le nombre de champs ajoutés
	 */
	public void ordonnerCell(int nbr) {
		final FormulaireController gc = FormulaireController.getInstance();
		FormulaireBoard c = gc.getCheckerBoard();
		// Repositionner les cellules décalées
		for (int row = position.getY() + 1; row < SQUARE_NUMBER_ligne; row++) {
			for (int column = 0; column < SQUARE_NUMBER_col; column++) {
				DroppableElement cellule = (DroppableElement) c.getCell(row,
						column);
				cellule.setposition(column, row);
			}
		}
		// Décaler les champs
		for (int column = 0; column < SQUARE_NUMBER_col; column++) {
			if (column != position.getX()) {
				int row = position.getY() + nbr;
				boolean check = false;
				while ((row < position.getY() + 2 * nbr)
						&& (row < SQUARE_NUMBER_ligne) && !check) {
					DroppableElement cellule = (DroppableElement) c.getCell(
							row, column);
					if ((cellule.type == "checkbox" || cellule.type == "radio")
							&& !cellule.choix) {
						check = true;
					} else if (cellule.choix || cellule.type == "checkbox"
							|| cellule.type == "radio") {
						DroppableElement celluleOld = (DroppableElement) c
								.getCell(row - nbr, column);
						while (cellule.getWidgetElement().getWidgetCount() > 0) {
							((HorizontalPanel) celluleOld.getWidget())
									.add(cellule.getWidgetElement()
											.getWidget(0));
						}
						celluleOld.type = cellule.type;
						celluleOld.choix = cellule.choix;
						celluleOld.name = cellule.name;
						celluleOld.lock();
						cellule.getWidgetElement().clear();
						cellule.enable();
						cellule.choix = false;
						cellule.type = null;
						cellule.name = null;
					}

					row++;
				}
			}
		}

		/*
		 * for (int row = position.getY()+ nbr; row < position.getY() + 2 * nbr;
		 * row++) for (int column = 0; column < SQUARE_NUMBER_col; column++) {
		 * if(column != position.getX() ){ DroppableElement cellule =
		 * (DroppableElement) c.getCell(row,column);
		 * 
		 * if (cellule.choix || cellule.type == "checkbox" || cellule.type ==
		 * "radio") { DroppableElement celluleOld = (DroppableElement)
		 * c.getCell( row - nbr, column); while
		 * (cellule.getWidgetElement().getWidgetCount() > 0) {
		 * ((HorizontalPanel) celluleOld.getWidget()).add(cellule
		 * .getWidgetElement().getWidget(0)); } celluleOld.type = cellule.type;
		 * celluleOld.choix = cellule.choix; celluleOld.name = cellule.name;
		 * celluleOld.lock(); cellule.getWidgetElement().clear();
		 * cellule.enable(); cellule.choix = false; cellule.type = null;
		 * cellule.name = null; } }
		 * 
		 * }
		 */

		for (int row = position.getY() + nbr; row < SQUARE_NUMBER_ligne; row++)
			for (int column = 0; column < SQUARE_NUMBER_col; column++) {
				if (column != position.getX()) {
					DroppableElement cellule = (DroppableElement) c.getCell(
							row, column);

					if (cellule.type != null) {
						final DroppableElement celluleOld = (DroppableElement) c
								.getCell(row - nbr, column);
						while (cellule.getWidgetElement().getWidgetCount() > 0) {
							((HorizontalPanel) celluleOld.getWidget())
									.add(cellule.getWidgetElement()
											.getWidget(0));
						}
						celluleOld.type = cellule.type;
						celluleOld.choix = cellule.choix;
						celluleOld.name = cellule.name;
						celluleOld.lock();
						cellule.getWidgetElement().clear();
						cellule.choix = false;
						cellule.type = null;
						cellule.name = null;
						cellule.enable();
						Image suppression = null;
						HorizontalPanel hp = (HorizontalPanel) celluleOld
								.getWidget();
						if (celluleOld.type.equals("text")
								|| celluleOld.type.equals("combobox")
								|| celluleOld.type.equals("chiffre")
								|| celluleOld.type.equals("date")
								|| celluleOld.type.equals("time")|| celluleOld.type.equals("image")) {
							suppression = (Image) hp.getWidget(3);
						} else if ((celluleOld.type.equals("checkbox") || celluleOld.type
								.equals("radio")) && !celluleOld.choix) {
							suppression = (Image) hp.getWidget(4);
						}
						
						if (suppression != null) {
							// Action de la suppression
							suppression.addClickHandler(new ClickHandler() {
								public void onClick(ClickEvent event) {
									// Vider la cellule
									celluleOld.clear();
									// Si le champs est de type checkbox ou
									// radio, il faix vider les champs de choix
									// également
									if (celluleOld.type == "checkbox"
											|| celluleOld.type == "radio") {
										final FormulaireController gc = FormulaireController
												.getInstance();
										FormulaireBoard c = gc
												.getCheckerBoard();
										int y = celluleOld.position.getY() + 1;
										boolean end = false;
										while (!end) {
											DroppableElement cellule = (DroppableElement) c
													.getCell(y,
															celluleOld.position
																	.getX());
											if (cellule.choix) {
												cellule.clear();
												cellule.type = null;
												cellule.choix = false;
												cellule.name = null;
												cellule.enable();
												y++;
											} else
												end = true;

										}
									}

									// Rendre le champs capable de recevoir des
									// elements puisqu'on l'a vidé
									celluleOld.enable();
									celluleOld.type = null;

								}
							});
						}
					}
				}
			}
	}

	public void delete(Position p) {
		listChoix = new ArrayList<Boolean>();
		listName = new ArrayList<String>();
		listValue = new ArrayList<String>();
		final FormulaireController gc = FormulaireController.getInstance();
		FormulaireBoard c = gc.getCheckerBoard();
		int i = p.getY() + 1;
		int j = p.getX();
		DroppableElement cellule = (DroppableElement) c.getCell(i, j);
		while (i < SQUARE_NUMBER_ligne && cellule.choix) {
			HorizontalPanel hp = ((HorizontalPanel) cellule.getWidget());
			InputElement ie = hp.getWidget(0).getElement().cast();
			InputElement ie1 = hp.getWidget(1).getElement().cast();
			listChoix.add(ie.isChecked());
			listName.add(cellule.name);
			listValue.add(ie1.getValue());
			cellule.name = null;
			cellule.setType(null);
			cellule.choix = false;
			cellule.getWidgetElement().clear();
			cellule.enable();
			i++;
			try {
				cellule = (DroppableElement) c.getCell(i, j);
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	}

	public void remplir() {
		int si = listChoix.size();
		final FormulaireController gc = FormulaireController.getInstance();
		FormulaireBoard c = gc.getCheckerBoard();
		for (int i = 0; i < si; i++) {
			DroppableElement celluleOld = (DroppableElement) c.getCell(
					position.getY() + i + 1, position.getX());
			HorizontalPanel hp = ((HorizontalPanel) celluleOld.getWidget());
			celluleOld.setType(type);
			celluleOld.name = listName.get(i);
			InputElement ie = hp.getWidget(0).getElement().cast();
			;
			ie.setChecked(listChoix.get(i));
			InputElement ie1 = hp.getWidget(1).getElement().cast();
			;
			ie1.setValue(listValue.get(i));
			celluleOld.choix = true;
			celluleOld.lock();
		}
	}

	public void ordonnerCol(int nbr, int distance) {

		final FormulaireController gc = FormulaireController.getInstance();
		FormulaireBoard c = gc.getCheckerBoard();

		// Décaler les champs

		int row = position.getY() + nbr + distance;
		while (row < SQUARE_NUMBER_ligne) {
			DroppableElement cellule = (DroppableElement) c.getCell(row,
					position.getX());
			final DroppableElement celluleOld = (DroppableElement) c.getCell(
					row - distance, position.getX());
			while (cellule.getWidgetElement().getWidgetCount() > 0) {
				((HorizontalPanel) celluleOld.getWidget()).add(cellule
						.getWidgetElement().getWidget(0));

			}
			celluleOld.type = cellule.type;
			celluleOld.choix = cellule.choix;
			celluleOld.name = cellule.name;
			Element elem = null;
			Image suppression = null;
			HorizontalPanel hp = (HorizontalPanel) celluleOld.getWidget();
			if (celluleOld.type.equals("text")
					|| celluleOld.type.equals("combobox")
					|| celluleOld.type.equals("chiffre")
					|| celluleOld.type.equals("date")
					|| celluleOld.type.equals("time")|| celluleOld.type.equals("image")) {
				elem = (Element) hp.getWidget(2);
				suppression = (Image) hp.getWidget(3);
				elem.setPosition(celluleOld.getPosition());
			} else if ((celluleOld.type.equals("checkbox") || celluleOld.type
					.equals("radio")) && !celluleOld.choix) {
				elem = (Element) hp.getWidget(3);
				suppression = (Image) hp.getWidget(4);
				elem.setPosition(celluleOld.getPosition());
			} 
			if (suppression != null) {
				// Action de la suppression
				suppression.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						// Vider la cellule
						celluleOld.clear();
						// Si le champs est de type checkbox ou
						// radio, il faix vider les champs de choix
						// également
						if (celluleOld.type == "checkbox"
								|| celluleOld.type == "radio") {
							final FormulaireController gc = FormulaireController
									.getInstance();
							FormulaireBoard c = gc.getCheckerBoard();
							int y = celluleOld.position.getY() + 1;
							boolean end = false;
							while (!end) {
								DroppableElement cellule1 = (DroppableElement) c
										.getCell(y, celluleOld.position.getX());
								if (cellule1.choix) {
									cellule1.clear();
									cellule1.type = null;
									cellule1.choix = false;
									cellule1.name = null;
									cellule1.enable();
									y++;
								} else
									end = true;

							}
						}

						// Rendre le champs capable de recevoir des
						// elements puisqu'on l'a vidé
						celluleOld.enable();
						celluleOld.type = null;

					}
				});
			}
			celluleOld.lock();
			cellule.getWidgetElement().clear();
			cellule.enable();
			cellule.choix = false;
			cellule.type = null;
			cellule.name = null;

			row++;
		}
	}

	/**
	 * Supprimer un element de la cellule
	 */
	public boolean remove(Widget w) {
		return ((HorizontalPanel) getWidget()).remove(w);
	}

	private void init() {
		initWidget(new HorizontalPanel());
		addDropHandler(this);
	}

	/**
	 * Ajouter un champs à la cellule
	 */
	@Override
	public void add(Widget w) {
		// TODO Auto-generated method stub
		Element p = (Element) w;
		if (p.getType().equals("deplace")) {

			FormulaireController gc = FormulaireController.getInstance();
			FormulaireBoard c1 = gc.getCheckerBoard();
			DroppableElement cellule = (DroppableElement) c1.getCell(p
					.getPosition().getY(), p.getPosition().getX());
			HorizontalPanel hp = cellule.getWidgetElement();
			while (cellule.getWidgetElement().getWidgetCount() > 0) {
				((HorizontalPanel) getWidget()).add(cellule.getWidgetElement()
						.getWidget(0));

			}
			type = cellule.getType();
			choix = cellule.choix;
			name = cellule.name;
			lock();
			Element elem = null;
			Image suppression = null;

			HorizontalPanel hp1 = (HorizontalPanel) getWidget();
			if (type.equals("text") || type.equals("combobox")
					|| type.equals("chiffre") || type.equals("date")
					|| type.equals("time")|| type.equals("image")) {
				elem = (Element) hp1.getWidget(2);
				suppression = (Image) hp1.getWidget(3);
				elem.setPosition(getPosition());
			} else if ((type.equals("checkbox") || type.equals("radio"))
					&& !choix) {
				elem = (Element) hp1.getWidget(3);
				suppression = (Image) hp1.getWidget(4);
				elem.setPosition(getPosition());
			}
			cellule.setType(null);
			cellule.name = null;
			cellule.choix = false;
			cellule.enable();
			if (suppression != null) {
				// Action de la suppression
				suppression.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						// Vider la cellule
						clear();
						// Si le champs est de type checkbox ou
						// radio, il faix vider les champs de choix
						// également
						if (type == "checkbox" || type == "radio") {
							final FormulaireController gc = FormulaireController
									.getInstance();
							FormulaireBoard c = gc.getCheckerBoard();
							int y = position.getY() + 1;
							boolean end = false;
							while (!end) {
								DroppableElement cellule1 = (DroppableElement) c
										.getCell(y, position.getX());
								if (cellule1.choix) {
									cellule1.clear();
									cellule1.type = null;
									cellule1.choix = false;
									cellule1.name = null;
									cellule1.enable();
									y++;
								} else
									end = true;

							}
						}

						// Rendre le champs capable de recevoir des
						// elements puisqu'on l'a vidé
						enable();
						type = null;

					}
				});
			}
			// InputElement label1 = hp.getWidget(0).getElement().cast();
			// TextBox label = new TextBox();
			// label.addStyleName("inputLabel");
			// label.getElement().setAttribute("placeholder", "Label");
			// label.setValue(label1.getValue());
			// TextBox choix = null;
			// // L'image de la suppression
			// Image suppression = new Image();
			// suppression.setUrl("images/formulaire/delete.png");
			// suppression.addStyleName("deleteForm");
			//
			// Element elem = new Element(position, "deplace");
			// elem.getElement().setClassName(elem.getType() + "Style");
			//
			// Widget element = null;
			// String id = "";
			//
			// if (cellule.getType().equals("date"))
			// id = "date" + Utils.generateId();
			// if (cellule.getType().equals("time"))
			// id = "time" + Utils.generateId();
			//
			// // Création de l'element selon le type
			// if (cellule.getType().equals("text")) {
			// Window.alert("xxx2");
			// element = new TextBox();
			// type = "text";
			// InputElement tb = hp.getWidget(1).getElement().cast();
			// ((TextBox) element).setValue(tb.getValue());
			// } else if (cellule.getType().equals("checkbox")) {
			// Window.alert("xxx3");
			// CheckBox c = new CheckBox();
			// // Ajout de l'attribut name pour les champs checkbox et radio
			// name = "" + cellule.name;
			// element = c;
			// InputElement tb = hp.getWidget(1).getElement().cast();
			// ((CheckBox) element).setValue(tb.isChecked());
			// // Ajout du label de choix pour les champs de type checkbox et
			// // radio
			// choix = new TextBox();
			// choix.addStyleName("inputLabel");
			// choix.getElement().setAttribute("placeholder", "Choix");
			// InputElement tb1 = hp.getWidget(2).getElement().cast();
			// ((TextBox) choix).setValue(tb1.getValue());
			// type = "checkbox";
			// } else if (cellule.getType().equals("radio")) {
			// Window.alert("xxx4");
			// RadioButton r = new RadioButton("Choix");
			// name = "" + cellule.name;
			// element = r;
			// InputElement tb = hp.getWidget(1).getElement().cast();
			// ((CheckBox) element).setValue(tb.isChecked());
			// choix = new TextBox();
			// choix.addStyleName("inputLabel");
			// choix.getElement().setAttribute("placeholder", "Choix");
			// InputElement tb1 = hp.getWidget(2).getElement().cast();
			// ((TextBox) choix).setValue(tb1.getValue());
			// type = "radio";
			// } else if (cellule.getType().equals("combobox")) {
			// Window.alert("xxx5");
			// // OPTION++;
			// // Ajout de l'option pour les champs de type combobox
			// option = "" + cellule.name;
			// element = new ListBox();
			// SelectElement tb = hp.getWidget(1).getElement().cast();
			// ListBox lbo = ListBox.wrap(tb);
			// int si = lbo.getItemCount();
			// for (int i = 0; i < si; i++) {
			// ((ListBox) element).addItem(lbo.getItemText(i),
			// lbo.getValue(i));
			// }
			// ((ListBox) element).setVisibleItemCount(1);
			// type = "combobox";
			// } else if (cellule.getType().equals("chiffre")) {
			// Window.alert("xxx6");
			// InputElement tb = hp.getWidget(1).getElement().cast();
			// if (tb.getValue() != null) {
			// element = new HTML(
			// "<input type='text' class='gwt-TextBox' value='"
			// + tb.getValue()
			// +
			// "' onkeypress=\"if((event.keyCode < 48 || event.keyCode > 57) && event.keyCode != 46){ event.returnValue=false;}else if(event.keyCode == 46 && (this.value == '' || this.value.indexOf('.') != -1)){ event.returnValue =false;} \" />");
			// } else {
			// element = new HTML(
			// "<input type='text' class='gwt-TextBox' onkeypress=\"if((event.keyCode < 48 || event.keyCode > 57) && event.keyCode != 46){ event.returnValue=false;}else if(event.keyCode == 46 && (this.value == '' || this.value.indexOf('.') != -1)){ event.returnValue =false;} \" />");
			//
			// }
			// type = "chiffre";
			// } else if (cellule.getType().equals("date")) {
			// Window.alert("xxx7");
			// InputElement tb = hp.getWidget(1).getElement().cast();
			// element = new HTML(
			// "<input type='text' class='gwt-TextBox'  id='" + id
			// + "' placeholder='JJ/MM/AAAA' value='"
			// + Date.parse(tb.getValue()) + "'/> ");
			// type = "date";
			// } else {
			// Window.alert("xxx8");
			// InputElement tb = hp.getWidget(1).getElement().cast();
			// element = new HTML(
			// "<input type='text' class='gwt-TextBox' id='" + id
			// + "' value='" + tb.getValue() + "' />");
			// type = "time";
			//
			// }
			// cellule.clear();
			// // Si le champs est de type checkbox ou radio, il faix vider
			// // les
			// // champs de choix également
			// if (type == "checkbox" || type == "radio") {
			// System.out.println("xx2");
			// listChoix = new ArrayList<Boolean>();
			// listName = new ArrayList<String>();
			// listValue = new ArrayList<String>();
			// int y = cellule.getPosition().getY() + 1;
			// boolean end = false;
			// while (!end) {
			// DroppableElement cellule1 = (DroppableElement) c1.getCell(
			// y, cellule.getPosition().getX());
			// if (cellule1.choix) {
			// HorizontalPanel hp2 = cellule1.getWidgetElement();
			// InputElement label3 = hp2.getWidget(0).getElement()
			// .cast();
			// listChoix.add(label3.isChecked());
			// listName.add(cellule1.name);
			// InputElement label2 = hp2.getWidget(1).getElement()
			// .cast();
			// listValue.add(label2.getValue());
			// cellule1.clear();
			// cellule1.type = null;
			// cellule1.choix = false;
			// cellule1.name = null;
			// cellule1.enable();
			// y++;
			// } else
			// end = true;
			//
			// }
			// }
			// // Rendre le champs capable de recevoir des elements
			// // puisqu'on
			// // l'a vidé
			// cellule.enable();
			// cellule.setType(null);
			// ((HorizontalPanel) getWidget()).add(label);
			// ((HorizontalPanel) getWidget()).add(element);
			// if (choix != null) {
			// ((HorizontalPanel) getWidget()).add(choix);
			// }
			// ((HorizontalPanel) getWidget()).add(elem);
			// ((HorizontalPanel) getWidget()).add(suppression);
			//
			// // Action de la suppression
			// suppression.addClickHandler(new ClickHandler() {
			// public void onClick(ClickEvent event) {
			// // Vider la cellule
			// clear();
			// // Si le champs est de type checkbox ou radio, il faix vider
			// // les
			// // champs de choix également
			// if (type == "checkbox" || type == "radio") {
			//
			// final FormulaireController gc = FormulaireController
			// .getInstance();
			// FormulaireBoard c = gc.getCheckerBoard();
			// int y = position.getY() + 1;
			// boolean end = false;
			// while (!end) {
			// DroppableElement cellule1 = (DroppableElement) c
			// .getCell(y, position.getX());
			// if (cellule1.choix) {
			// cellule1.clear();
			// cellule1.type = null;
			// cellule1.choix = false;
			// cellule1.name = null;
			// cellule1.enable();
			// y++;
			// } else
			// end = true;
			//
			// }
			// }
			// // Rendre le champs capable de recevoir des elements
			// // puisqu'on
			// // l'a vidé
			// enable();
			// type = null;
			//
			// }
			// });
			//
			// FormulaireController.nativeMethod(id, p.getType());
		} else {
			// Le label du champs
			TextBox label = new TextBox();
			label.addStyleName("inputLabel");
			label.getElement().setAttribute("placeholder", "Label");
			// Le label du choix
			TextBox choix = null;
			// L'image de la suppression
			Image suppression = new Image();
			suppression.setUrl("images/formulaire/delete.png");
			suppression.addStyleName("deleteForm");

			Element elem = new Element(position, "deplace");
			elem.getElement().setClassName(elem.getType() + "Style");

			Widget element = null;
			String id = "";

			if (p.getType().equals("date"))
				id = "date" + Utils.generateId();
			if (p.getType().equals("time"))
				id = "time" + Utils.generateId();

			// Création de l'element selon le type
			if (p.getType().equals("texte")) {
				element = new TextBox();
				type = "text";
			}else if (p.getType().equals("image")) {
				element =new Image("../images/formulaire/Photo.png");
				type = "image";
			} else if (p.getType().equals("checkbox")) {
				CheckBox c = new CheckBox();

				// Ajout de l'attribut name pour les champs checkbox et radio
				name = "" + CHOICE;
				element = c;
				// Ajout du label de choix pour les champs de type checkbox et
				// radio
				choix = new TextBox();
				choix.addStyleName("inputLabel");
				choix.getElement().setAttribute("placeholder", "Choix");
				type = "checkbox";
			} else if (p.getType().equals("radio")) {
				RadioButton r = new RadioButton("Choix");
				name = "" + CHOICE;
				element = r;
				choix = new TextBox();
				choix.addStyleName("inputLabel");
				choix.getElement().setAttribute("placeholder", "Choix");
				type = "radio";
			} else if (p.getType().equals("combobox")) {
				OPTION++;
				// Ajout de l'option pour les champs de type combobox
				option = "" + OPTION;
				element = new ListBox();
				type = "combobox";
			} else if (p.getType().equals("chiffre")) {
				element = new HTML(
						"<input type='text' class='gwt-TextBox' onkeypress=\"if((event.keyCode < 48 || event.keyCode > 57) && event.keyCode != 46){ event.returnValue=false;}else if(event.keyCode == 46 && (this.value == '' || this.value.indexOf('.') != -1)){ event.returnValue =false;} \" />");
				type = "chiffre";
			} else if (p.getType().equals("date")) {
				element = new HTML(
						"<input type='text' class='gwt-TextBox'  id='" + id
								+ "' placeholder='JJ/MM/AAAA' /> ");
				type = "date";
			} else {
				element = new HTML(
						"<input type='text' class='gwt-TextBox' id='" + id
								+ "' />");
				type = "time";
			}
			this.choix = false;
			((HorizontalPanel) getWidget()).add(label);
			((HorizontalPanel) getWidget()).add(element);
			if (choix != null) {
				((HorizontalPanel) getWidget()).add(choix);
			}
			((HorizontalPanel) getWidget()).add(elem);
			((HorizontalPanel) getWidget()).add(suppression);

			// Action de la suppression
			suppression.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					// Vider la cellule
					clear();
					// Si le champs est de type checkbox ou radio, il faix vider
					// les
					// champs de choix également
					if (type == "checkbox" || type == "radio") {
						final FormulaireController gc = FormulaireController
								.getInstance();
						FormulaireBoard c = gc.getCheckerBoard();
						int y = position.getY() + 1;
						boolean end = false;
						while (!end) {
							DroppableElement cellule = (DroppableElement) c
									.getCell(y, position.getX());
							if (cellule.choix) {
								cellule.clear();
								cellule.type = null;
								cellule.choix = false;
								cellule.name = null;
								cellule.enable();
								y++;
							} else
								end = true;

						}
					}
					// Rendre le champs capable de recevoir des elements
					// puisqu'on
					// l'a vidé
					enable();
					type = null;

				}
			});
			FormulaireController.nativeMethod(id, p.getType());
		}

	}

	// Récuperer la cellule
	public HorizontalPanel getWidgetElement() {
		return ((HorizontalPanel) getWidget());
	}

	// Changer la position de la cellule
	public void setposition(int x, int y) {
		position = new Position(x, y);
	}

	public void setChoix(boolean choix) {
		this.choix = choix;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public boolean isChoix() {
		return choix;
	}

}
