package com.applifit.bilifit.client.formulaires;

import static com.applifit.bilifit.client.formulaires.FormulaireBoard.FORMBOARD_SELECTOR;
import static com.applifit.bilifit.client.formulaires.FormulaireBoard.SQUARE_NUMBER_col;
import static com.applifit.bilifit.client.formulaires.FormulaireBoard.SQUARE_NUMBER_ligne;
import static com.google.gwt.query.client.GQuery.$;
import gwtquery.plugins.draggable.client.events.DragStartEvent;
import gwtquery.plugins.draggable.client.events.DragStartEvent.DragStartEventHandler;
import gwtquery.plugins.draggable.client.events.DragStopEvent;
import gwtquery.plugins.draggable.client.events.DragStopEvent.DragStopEventHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.applifit.bilifit.client.outils.Utils;
import com.applifit.bilifit.client.templatesUibinder.formulaires.BodyApercu;
import com.applifit.bilifit.client.templatesUibinder.formulaires.FormRecherche;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class FormulaireController implements DragStartEventHandler,
		DragStopEventHandler {
	private static final String FORMULAIRE_URL = GWT.getHostPageBaseURL()
			+ "formulaire/";
	public static int CHOICE = 0;
	public static int OPTION = -1;

	public static class Position {
		private int x;
		private int y;

		public Position(int column, int row) {
			x = column;
			y = row;
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof Position) || obj == null) {
				return false;
			}
			Position pos2 = (Position) obj;
			return pos2.x == x && pos2.y == y;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		@Override
		public int hashCode() {
			return x ^ y;
		}

		public boolean isValid() {
			return x >= 0 && x < FormulaireBoard.SQUARE_NUMBER_col && y >= 0
					&& y < FormulaireBoard.SQUARE_NUMBER_ligne;

		}

		public void setX(int x) {
			this.x = x;
		}

		public void setY(int y) {
			this.y = y;
		}
	}

	private static FormulaireController INSTANCE = new FormulaireController();

	public static FormulaireController getInstance() {
		return INSTANCE;
	}

	private Map<Position, Element> pieceByPosition;

	public void onDragStart(DragStartEvent event) {
	}

	public void onDragStop(DragStopEvent event) {

	}

	public void reset() {
		pieceByPosition = new HashMap<Position, Element>();
	}

	public FormulaireBoard getCheckerBoard() {
		return (FormulaireBoard) $(FORMBOARD_SELECTOR).widget();

	}

	/**
	 * Crée la boite à outils avec les images draggables
	 * 
	 * @return
	 */
	public static Widget createImageGrid() {

		Grid grid = new Grid(8, 1);
		CellFormatter cellFormatter = grid.getCellFormatter();

		int numColumns = grid.getColumnCount();
		Element piece = null;

		ArrayList<Element> pieces = (new FormulaireBoard()).fillForm();
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < numColumns; col++) {
				piece = pieces.get(row);
				piece.getElement().setClassName(piece.getType() + "Style");
				grid.setWidget(row, col, piece);

				cellFormatter.setHeight(row, col, "15px");
				cellFormatter.setWidth(row, col, "61px");
			}
		}
		grid.getElement().addClassName("floatLeft newScrollBar");
		grid.getElement().setId("choix_objet_formulaire");
		return grid;
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	public static void recupererElements(ArrayList choices, ArrayList options,
			ArrayList elements, ArrayList parametres) {
		choices.clear();
		options.clear();
		elements.clear();
		parametres.clear();
		/***
		 * Initialiser les listes choices et options avec le nombre d'elements
		 * crées
		 ***/
		/***
		 * Choix : les choix du checkbox et du radio , les options sont les
		 * options du champs combobox
		 ***/
		for (int i = 0; i < CHOICE; i++) {
			choices.add(i, "");
		}
		for (int i = 0; i < OPTION + 1; i++) {
			options.add(i, "");
		}

		FormulaireController gc = FormulaireController.getInstance();
		FormulaireBoard c = gc.getCheckerBoard();
		InputElement check;
		RadioButton rb;

		// Parcourir les cases de la gride
		for (int row = 0; row < SQUARE_NUMBER_ligne; row++) {
			for (int column = 0; column < SQUARE_NUMBER_col; column++) {
				DroppableElement cellule = (DroppableElement) c.getCell(row,
						column);
				HorizontalPanel hp = cellule.getWidgetElement();
				// Si la cellule n'est pa vide
				if (cellule.type != null) {
					InputElement label = hp.getWidget(0).getElement().cast();
					if (!cellule.choix) {
						ArrayList element = new ArrayList();
						ArrayList params = new ArrayList();
						// Remplir la liste element avec les données du champs
						// (type, position , name et option)
						element.add(cellule.type);
						element.add(cellule.position.getX());
						element.add(cellule.position.getY());
						// name est different de null , si le chamsp est de type
						// checkbox ou radio
						if (cellule.name != null)
							element.add(cellule.name);
						else
							element.add("");

						// option est diffrent de null si le champs est de type
						// combobox
						if (cellule.option != null)
							element.add(cellule.option);
						else
							element.add("");
						// Remplire la liste p par les parametres du champs
						// (titre pour le moment)
						ArrayList p = new ArrayList();
						p.add("titre");
						p.add(label.getValue());
						params.add(p);

						// Ajouter element à la liste des elements et p à la
						// liste des parametres
						elements.add(element);
						parametres.add(params);

					}

					/**
					 * Concatener les choix avec separateur / pour les champs de
					 * type checkbox et radio (les ajouter à la liste des choix
					 * avec comme index cellule.name) et les option pour le type
					 * combobox et les ajouter à la liste des options avec comme
					 * index cellule.option pour les reconnaitre au moment de
					 * l'ajout
					 */
					if (cellule.type.equals("checkbox")
							|| cellule.type.equals("radio")) {
						if (!cellule.choix) {
							label = hp.getWidget(2).getElement().cast();
							choices.set(Integer.parseInt(cellule.name),
									label.getValue());
						} else {
							label = hp.getWidget(1).getElement().cast();
							choices.set(Integer.parseInt(cellule.name),
									choices.get(Integer.parseInt(cellule.name))
											+ "*_*" + label.getValue());
						}
					} else if (cellule.type.equals("combobox")) {
						ListBox lb = (ListBox) hp.getWidget(1);
						String options_string = "";
						for (int i = 0; i < lb.getItemCount(); i++) {
							if (options_string.equals(""))
								options_string = lb.getItemText(i);
							else {
								options_string = options_string + "*_*"
										+ lb.getItemText(i);
							}
						}
						options.set(Integer.parseInt(cellule.option),
								options_string);
					}

				}
			}
		}
	}

	/**
	 * Crée le popup contenant la dual liste
	 * 
	 * @param dualListBox
	 * @return dialogbox
	 */

	public static DialogBox CreateDialogBoxAllocation(DualListBox dualListBox,
			final boolean kt, final Formulaire_applifit fa) {
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Allocation du formulaire:");
		dialogBox.setAnimationEnabled(true);
		Button validerButton = new Button("Allouer");
		validerButton.getElement().setId("valideButton");
		Label textToServerLabel = new Label();

		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.setWidth("300px");
		dialogVPanel.setHeight("200px");
		dialogVPanel.add(new HTML("<b>Liste des comptes CA et CU:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(dualListBox);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(validerButton);
		dialogBox.setWidget(dialogVPanel);
		dialogBox.setPopupPosition(450, 450);

		/*** Action de clique sur le button de valisation ***/
		validerButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (kt) {
					dialogBox.hide();
					fa.addFormulaire();
				} else {
					dialogBox.hide();
				}
			}
		});

		return dialogBox;

	}

	/**
	 * Associe le formulaire à la liste des comptes choisis pour allocation
	 * 
	 * @param dualListBox
	 * @param url
	 */
	public static void allouerFormulaire(DualListBox dualListBox, String url) {
		MouseListBox mlb = dualListBox.getRight();
		ArrayList<Widget> widgets = mlb.widgetList();
		for (int i = 0; i < widgets.size(); i++) {
			RequestBuilder builder = new RequestBuilder(RequestBuilder.PUT,
					FORMULAIRE_URL + "allouerFormulaire/compte/"
							+ widgets.get(i).getTitle() + url);
			builder.setHeader("Content-Type", "application/json");
			try {

				builder.sendRequest(null, new RequestCallback() {

					public void onError(Request request, Throwable exception) {
						Window.alert("erreur d'allocation du formulaire");
					}

					public void onResponseReceived(Request request,
							Response response) {
					}
				});
			} catch (RequestException e) {
				Window.alert("RequestException");
			}
		}

	}

	/** Récupérer la liste des comptes pour lesquels le formulaire est alloué ***/

	public static void allouerFormulaireBis(DualListBox dualListBox,
			JSONObject formAsJSONObjectBis) {
		MouseListBox mlb = dualListBox.getRight();
		ArrayList<Widget> widgets = mlb.widgetList();
		for (int i = 0; i < widgets.size(); i++) {
			formAsJSONObjectBis.put("user" + i, new JSONString(widgets.get(i)
					.getTitle()));
		}
	}

	/**
	 * Créer la page de l'aperçu
	 * 
	 * @param bodyapercu
	 * @param formAsJSONObject
	 * @param elements
	 * @param parametres
	 * @param choices
	 * @param options
	 */
	/*
	 * public static void pageApercu(BodyApercu bodyapercu,String nom , String
	 * comment, ArrayList elements, ArrayList parametres, ArrayList choices,
	 * ArrayList options) {
	 * 
	 * // Récuperer le nom et le commentaire
	 * 
	 * bodyapercu.getNom().setValue(nom);
	 * bodyapercu.getComment().setValue(comment); ArrayList element = null;
	 * if(elements.size() > 0) element = (ArrayList)
	 * elements.get(elements.size() - 1); ArrayList param; // Table qui va
	 * héberger les elements du formulaire FlexTable flexApercu = new
	 * FlexTable(); flexApercu.setCellPadding(1); flexApercu.setCellSpacing(0);
	 * flexApercu.setStyleName("style_grid_apercu");
	 * RootPanel.get("contenu_principal_formulaire").add(flexApercu);
	 * 
	 * // Parcourir les elements du formulaire for (int i = 0; i <
	 * elements.size(); i++) { element = (ArrayList) elements.get(i); // Créer
	 * le label de l'element et l'ajouter à la cellule au dessus // de l'element
	 * Label l = new Label(); param = (ArrayList) parametres.get(i);
	 * l.setText(((ArrayList) param.get(0)).get(1).toString());
	 * flexApercu.setWidget(2 * Integer.parseInt("" + element.get(2)),
	 * Integer.parseInt("" + element.get(1)), l);
	 * 
	 * Widget widget = null; if (element.get(0).toString().equals("text")) {
	 * widget = new TextBox(); widget.addStyleName("style_text_apercu");
	 * flexApercu.setWidget( 2 * Integer.parseInt("" + element.get(2)) + 1,
	 * Integer.parseInt("" + element.get(1)), widget); } else if
	 * (element.get(0).toString().equals("chiffre")) { widget = new
	 * HTML("<input type='number'/>");
	 * widget.addStyleName("style_number_apercu"); flexApercu.setWidget( 2 *
	 * Integer.parseInt("" + element.get(2)) + 1, Integer.parseInt("" +
	 * element.get(1)), widget); } else if
	 * (element.get(0).toString().equals("date")) { widget = new HTML(
	 * "<input type='text' onclick='this.datepicker()' class='gwt-TextBox style_text_apercu' />"
	 * ); widget.addStyleName("style_date_apercu"); flexApercu.setWidget( 2 *
	 * Integer.parseInt("" + element.get(2)) + 1, Integer.parseInt("" +
	 * element.get(1)), widget); } else if
	 * (element.get(0).toString().equals("time")) { widget = new
	 * HTML("<input type='time'/>"); widget.addStyleName("style_time_apercu");
	 * flexApercu.setWidget( 2 * Integer.parseInt("" + element.get(2)) + 1,
	 * Integer.parseInt("" + element.get(1)), widget); } else if
	 * (element.get(0).toString().equals("checkbox") ||
	 * element.get(0).toString().equals("radio")) { String name =
	 * element.get(3).toString(); String choix = (String)
	 * choices.get(Integer.parseInt(name)); String ch[] =
	 * choix.split("\\*_\\*");
	 * 
	 * if (element.get(0).toString().equals("checkbox")) { CheckBox c = new
	 * CheckBox(); c.setText(ch[0]); widget = c; flexApercu.setWidget( 2 *
	 * Integer.parseInt("" + element.get(2)) + 1, Integer.parseInt("" +
	 * element.get(1)), widget); c.addStyleName("style_choix_apercu"); for (int
	 * j = 1; j < ch.length; j++) { CheckBox check = new CheckBox();
	 * check.setText(ch[j]); check.addStyleName("style_choix_apercu");
	 * flexApercu .getWidget( 2 * Integer.parseInt("" + element.get(2)) + 1,
	 * Integer.parseInt("" + element.get(1)))
	 * .getElement().appendChild(check.getElement());
	 * 
	 * } } else { RadioButton r = new RadioButton(name); r.setText(ch[0]);
	 * widget = r; flexApercu.setWidget( 2 * Integer.parseInt("" +
	 * element.get(2)) + 1, Integer.parseInt("" + element.get(1)), widget);
	 * r.addStyleName("style_choix_apercu"); for (int j = 1; j < ch.length; j++)
	 * { RadioButton radio = new RadioButton(name); radio.setText(ch[j]);
	 * radio.addStyleName("style_choix_apercu"); flexApercu .getWidget( 2 *
	 * Integer.parseInt("" + element.get(2)) + 1, Integer.parseInt("" +
	 * element.get(1))) .getElement().appendChild(radio.getElement()); } } }
	 * else if (element.get(0).toString().equals("combobox")) { ListBox lb = new
	 * ListBox(); String option = element.get(4).toString(); String opt =
	 * (String) options.get(Integer.parseInt(option)); String o[] =
	 * opt.split("\\*_\\*"); for (int j = 0; j < o.length; j++) {
	 * lb.addItem(o[j], o[j]); } lb.addStyleName("style_select_apercu"); widget
	 * = lb; flexApercu.setWidget( 2 * Integer.parseInt("" + element.get(2)) +
	 * 1, Integer.parseInt("" + element.get(1)), widget); } } }
	 */

	@SuppressWarnings("rawtypes")
	public static void pageApercu(BodyApercu bodyapercu, String nom,
			String comment, ArrayList elements, ArrayList parametres,
			ArrayList choices, ArrayList options) {

		// Récuperer le nom et le commentaire

		bodyapercu.getNom().setValue(nom);
		bodyapercu.getComment().setValue(comment);
		ArrayList element = null;
		if (elements.size() > 0)
			element = (ArrayList) elements.get(elements.size() - 1);
		ArrayList param;
		// Table qui va héberger les elements du formulaire
		FlexTable flexApercu1 = new FlexTable();
		flexApercu1.setCellPadding(1);
		flexApercu1.setCellSpacing(0);
		flexApercu1.setStyleName("style_grid_apercu");

		FlexTable flexApercu2 = new FlexTable();
		flexApercu1.setCellPadding(1);
		flexApercu1.setCellSpacing(0);
		flexApercu1.setStyleName("style_grid_apercu");

		FlexTable flexApercu3 = new FlexTable();
		flexApercu1.setCellPadding(1);
		flexApercu1.setCellSpacing(0);
		flexApercu1.setStyleName("style_grid_apercu");

		bodyapercu.getPart1().appendChild(flexApercu1.getElement());
		bodyapercu.getPart2().appendChild(flexApercu2.getElement());
		bodyapercu.getPart3().appendChild(flexApercu3.getElement());

		ArrayList<FlexTable> tables = new ArrayList<FlexTable>();
		tables.add(flexApercu1);
		tables.add(flexApercu2);
		tables.add(flexApercu3);

		// Parcourir les elements du formulaire
		for (int i = 0; i < elements.size(); i++) {
			element = (ArrayList) elements.get(i);

			String id = "";
			if (element.get(0).toString().equals("date"))
				id = "date" + Utils.generateId();
			if (element.get(0).toString().equals("time"))
				id = "time" + Utils.generateId();

			// Créer le label de l'element et l'ajouter à la cellule au dessus
			// de l'element
			Label l = new Label();
			param = (ArrayList) parametres.get(i);
			l.setText(((ArrayList) param.get(0)).get(1).toString());
			tables.get(Integer.parseInt("" + element.get(1))).setWidget(
					2 * Integer.parseInt("" + element.get(2)), 0, l);
			Widget widget = null;
			if (element.get(0).toString().equals("image")) {
				widget = new Image("../images/formulaire/Photo.png");
				// widget.addStyleName("style_text_apercu");
				tables.get(Integer.parseInt("" + element.get(1))).setWidget(
						(2 * Integer.parseInt("" + element.get(2))) + 1, 0,
						widget);

			} else if (element.get(0).toString().equals("text")) {
				widget = new TextBox();
				widget.addStyleName("style_text_apercu");
				tables.get(Integer.parseInt("" + element.get(1))).setWidget(
						(2 * Integer.parseInt("" + element.get(2))) + 1, 0,
						widget);

			} else if (element.get(0).toString().equals("chiffre")) {
				widget = new HTML(
						"<input type='text' class='gwt-TextBox style_text_apercu' onkeypress=\"if((event.keyCode < 48 || event.keyCode > 57) && event.keyCode != 46){ event.returnValue=false;}else if(event.keyCode == 46 && (this.value == '' || this.value.indexOf('.') != -1)){ event.returnValue =false;} \" />");
				widget.addStyleName("style_number_apercu");
				tables.get(Integer.parseInt("" + element.get(1))).setWidget(
						2 * Integer.parseInt("" + element.get(2)) + 1, 0,
						widget);
			} else if (element.get(0).toString().equals("date")) {
				widget = new HTML(
						"<input type='text' class='gwt-TextBox style_text_apercu'  id='"
								+ id + "' placeholder='JJ/MM/AAAA' />");
				widget.addStyleName("style_date_apercu");
				tables.get(Integer.parseInt("" + element.get(1))).setWidget(
						2 * Integer.parseInt("" + element.get(2)) + 1, 0,
						widget);
				FormulaireController.nativeMethod(id, "date");
			} else if (element.get(0).toString().equals("time")) {
				widget = new HTML(
						"<input type='text' class='gwt-TextBox style_text_apercu'  id='"
								+ id + "' /> ");
				widget.addStyleName("style_time_apercu");
				tables.get(Integer.parseInt("" + element.get(1))).setWidget(
						2 * Integer.parseInt("" + element.get(2)) + 1, 0,
						widget);
				FormulaireController.nativeMethod(id, "time");
			} else if (element.get(0).toString().equals("checkbox")
					|| element.get(0).toString().equals("radio")) {
				String name = element.get(3).toString();
				String choix = (String) choices.get(Integer.parseInt(name));
				String ch[] = choix.split("\\*_\\*");

				if (element.get(0).toString().equals("checkbox")) {
					CheckBox c = new CheckBox();
					c.setText(ch[0]);
					widget = c;
					tables.get(Integer.parseInt("" + element.get(1)))
							.setWidget(
									2 * Integer.parseInt("" + element.get(2)) + 1,
									0, widget);
					c.addStyleName("style_choix_apercu");
					for (int j = 1; j < ch.length; j++) {
						CheckBox check = new CheckBox();
						check.setText(ch[j]);
						check.addStyleName("style_choix_apercu");
						if (j == ch.length - 1)
							check.addStyleName("lastChoix");
						tables.get(Integer.parseInt("" + element.get(1)))
								.getWidget(
										2 * Integer.parseInt(""
												+ element.get(2)) + 1, 0)
								.getElement().appendChild(check.getElement());
					}
					for (int j = 0; j < 2 * (ch.length - 1); j++) {
						tables.get(Integer.parseInt("" + element.get(1)))
								.setWidget(
										2
												* Integer.parseInt(""
														+ element.get(2)) + 2
												+ j, 0, new HTML(""));
						tables.get(Integer.parseInt("" + element.get(1)))
								.getElement()
								.getElementsByTagName("tr")
								.getItem(
										2
												* Integer.parseInt(""
														+ element.get(2)) + 2
												+ j)
								.setClassName("trHeightNull");
					}

				} else {
					RadioButton r = new RadioButton(name);
					r.setText(ch[0]);
					widget = r;
					tables.get(Integer.parseInt("" + element.get(1)))
							.setWidget(
									2 * Integer.parseInt("" + element.get(2)) + 1,
									0, widget);

					r.addStyleName("style_choix_apercu");
					for (int j = 1; j < ch.length; j++) {
						RadioButton radio = new RadioButton(name);
						radio.setText(ch[j]);
						radio.addStyleName("style_choix_apercu");
						if (j == ch.length - 1)
							radio.addStyleName("lastChoix");
						tables.get(Integer.parseInt("" + element.get(1)))
								.getWidget(
										2 * Integer.parseInt(""
												+ element.get(2)) + 1, 0)
								.getElement().appendChild(radio.getElement());

					}
					for (int j = 0; j < 2 * (ch.length - 1); j++) {
						tables.get(Integer.parseInt("" + element.get(1)))
								.setWidget(
										2
												* Integer.parseInt(""
														+ element.get(2)) + 2
												+ j, 0, new HTML(""));
						tables.get(Integer.parseInt("" + element.get(1)))
								.getElement()
								.getElementsByTagName("tr")
								.getItem(
										2
												* Integer.parseInt(""
														+ element.get(2)) + 2
												+ j)
								.setClassName("trHeightNull");
					}
				}
			} else if (element.get(0).toString().equals("combobox")) {
				ListBox lb = new ListBox();
				String option = element.get(4).toString();
				String opt = (String) options.get(Integer.parseInt(option));
				String o[] = opt.split("\\*_\\*");
				for (int j = 0; j < o.length; j++) {
					lb.addItem(o[j], o[j]);
				}
				lb.addStyleName("style_select_apercu");
				widget = lb;
				tables.get(Integer.parseInt("" + element.get(1))).setWidget(
						2 * Integer.parseInt("" + element.get(2)) + 1, 0,
						widget);
			}
		}
	}

	// Alimenter la lise des formulaires par le resultat de la recherche
	public static void alimenterListeForm(String url, FormRecherche recherche) {
		final ListBox lb = ListBox.wrap(recherche.getFormulaire());

		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
		builder.setHeader("Content-Type", "application/json");

		try {
			builder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {

				}

				@SuppressWarnings("deprecation")
				public void onResponseReceived(Request request,
						Response response) {
					if (response.getStatusCode() == Response.SC_OK) {
						String formulairesAsJSONString = response.getText();

						JSONValue formulairesAsJSONValue = JSONParser
								.parse(formulairesAsJSONString);
						JSONArray formulairesAsJSONArray = formulairesAsJSONValue
								.isArray();

						// Vider la liste
						lb.clear();
						lb.addItem("Choisir un formulaire...", "");
						// Alimenter la liste
						for (int i = 0; i < formulairesAsJSONArray.size(); i++) {
							JSONValue formulaireAsJSONValue = formulairesAsJSONArray
									.get(i);
							JSONObject formulaireAsJSONObject = formulaireAsJSONValue
									.isObject();
							lb.addItem(
									formulaireAsJSONObject.get("nom")
											.isString().stringValue()
											+ " V"
											+ formulaireAsJSONObject
													.get("version").isNumber()
													.toString(),
									formulaireAsJSONObject.get("id").isNumber()
											.toString());
						}
					}
				}
			});
		} catch (RequestException e) {
			System.out.println("RequestException");
		}
	}

	// Recherche d'un formulaire par nom et date
	public static void rechercheForm(FormRecherche recherche) {

		String nom;
		String date;

		// Récuperer la date et le nom depuis le formulaire
		if (recherche.getSearch_nom().getValue().trim().equals("")) {
			nom = "-";
		} else
			nom = recherche.getSearch_nom().getValue().trim();

		if (recherche.getSearch_date().getValue().trim().equals("")) {
			date = "-";
		} else {
			date = recherche.getSearch_date().getValue().trim();
			String d[] = date.split("/");
			date = d[2] + "-" + d[1] + "-" + d[0];
		}

		// alimenter la liste des formulaires par le resultat de la recherche
		if (Cookies.getCookie("profil").equals("SA")
				|| Cookies.getCookie("profil").equals("CA"))
			alimenterListeForm(FORMULAIRE_URL + "listerFormulaire/entreprise/"
					+ Cookies.getCookie("client") + "/nom/" + nom + "/date/"
					+ date, recherche);
		else
			alimenterListeForm(
					FORMULAIRE_URL + "listerFormulaire/compte/"
							+ Cookies.getCookie("id") + "/nom/" + nom
							+ "/date/" + date, recherche);

	}

	/***
	 * une méthode native javascript pour initialiser le champs de date en
	 * datepicker
	 ***/
	public static native void nativeMethod(String id, String type)
	/*-{
		if (type == "date")
			$wnd.$('#' + id).datepicker({
				dateFormat : 'dd/mm/yy'
			});
		else
			$wnd.$('#' + id).timepicker();

	}-*/;

}
