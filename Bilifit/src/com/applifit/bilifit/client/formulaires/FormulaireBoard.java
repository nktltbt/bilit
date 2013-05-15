package com.applifit.bilifit.client.formulaires;

import static com.google.gwt.query.client.GQuery.$;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.query.client.Function;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.applifit.bilifit.client.formulaires.Element;
import com.applifit.bilifit.client.formulaires.FormulaireController.Position;
import com.applifit.bilifit.client.outils.Constantes;

public class FormulaireBoard extends Grid {

  private static final String FORMBOARD_ID = "formboard";
  public static final String FORMBOARD_SELECTOR = "#formboard";
  public static final int PIECE_NUMBER = 6;
  public static int SQUARE_NUMBER_ligne = Constantes.nbrLigne;
  public static final int SQUARE_NUMBER_col = Constantes.nbrColonne;
  
  private List<DroppableElement> droppableSquareList = new ArrayList<DroppableElement>();

  public FormulaireBoard() {
    super(SQUARE_NUMBER_ligne, SQUARE_NUMBER_col);
    initBoardForm();
  }

  public void authorizeMove(final Element currentPiece, Position p) {
    Widget w = getWidget(p.getY(), p.getX());

    assert w instanceof DroppableElement;

  }

  @Override
  public void clear() {
    for (DroppableElement square : droppableSquareList) {
      square.clear();
    }
  }

  public ArrayList<Element> fillForm() {
	    // start with pieces of Red player
	    final ArrayList<Element> pieces = new ArrayList<Element>();
	    
	        Element piece = new Element(new Position(0,1), "texte");
	        // we will fade in the piece after
	        $(piece).hide();
	        pieces.add(piece);
	        
	        piece = new Element(new Position(0,2), "checkbox");
	        // we will fade in the piece after
	        $(piece).hide();
	        pieces.add(piece);
	        
	        piece = new Element(new Position(0,3), "radio");
	        // we will fade in the piece after
	        $(piece).hide();
	        pieces.add(piece);
	        
	        piece = new Element(new Position(0,4), "combobox");
	        // we will fade in the piece after
	        $(piece).hide();
	        pieces.add(piece);
	        
	        piece = new Element(new Position(0,5), "chiffre");
	        // we will fade in the piece after
	        $(piece).hide();
	        pieces.add(piece);
	        
	        piece = new Element(new Position(0,6), "date");
	        // we will fade in the piece after
	        $(piece).hide();
	        pieces.add(piece);
	        
	        piece = new Element(new Position(0,7), "time");
	        // we will fade in the piece after
	        $(piece).hide();
	        pieces.add(piece);
	        
	        piece = new Element(new Position(0,8), "image");
	        // we will fade in the piece after
	        $(piece).hide();
	        pieces.add(piece);
	        
	        //GameController.getInstance().pieceMove(piece, null,new Position(1, row));
	        Element first = pieces.get(0);
	        $(first).fadeIn(100, new Function() {
	          @Override
	          public void f() {
	            fadeIn(pieces);
	          }
	        });
	 
        return pieces;
	  }

  public HasWidgets getCell(int row, int column) {
    return (HasWidgets) getWidget(row, column);
  }

  private void fadeIn(final List<Element> pieces) {
    if (pieces.isEmpty()) {
      return;
    }

    Element first = pieces.remove(0);
    $(first).fadeIn(100, new Function() {
      @Override
      public void f() {
        fadeIn(pieces);
      }
    });
  }

  private void initBoardForm() {
	    setCellPadding(1);
	    setCellSpacing(0);

	    for (int row = 0; row < SQUARE_NUMBER_ligne; row++) {
	      for (int column = 0; column < SQUARE_NUMBER_col; column++) {
	        Widget cell = null;
	        
	          cell = new DroppableElement(new Position(column, row));
	          cell.addStyleName("container_formulaire_type");
	          
	          droppableSquareList.add((DroppableElement) cell);
	        
	        setWidget(row, column, cell);
	      }
	    }
	    //set and id to retrieve it later thanks to GwtQuery
	    getElement().setId(FORMBOARD_ID);

	  }
  
  
  


}
