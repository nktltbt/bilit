package com.applifit.bilifit.client.formulaires;

import gwtquery.plugins.draggable.client.DraggableOptions.CursorAt;
import gwtquery.plugins.draggable.client.DraggableOptions.RevertOption;
import gwtquery.plugins.draggable.client.gwt.DraggableWidget;



import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.user.client.ui.HTML;
import com.applifit.bilifit.client.formulaires.FormulaireController.Position;

//Création des images draggable dans la boite à outils
public class Element extends DraggableWidget<HTML> {

  //position de l'image
  private Position position;
  
  //type attribué à l'image
  private String type;

  //constructeur
  public Element(Position initialPosition , String type) {
	    this.position = initialPosition;
	    this.type=type;
	    initForm();
	  }

  
  
  public void setPosition(Position position) {
    this.position = position;
  }

  protected void initForm() {
	    initWidget(new HTML());
	    getElement().setId("piece_" + getElement().hashCode());
	    setupDraggable();
	  }

  private void setupDraggable() {
    // revert the piece if this one is not dropped
    setRevert(RevertOption.ON_INVALID_DROP);
    // be sure that when the piece is dragging, it is in front
    setDraggingZIndex(100);
    // set the opacity of the piece during the drag
    setDraggingOpacity((float) 0.8);
    // set cursor in the middle of the piece
    setCursorAt(new CursorAt(5, 5, null, null));
    // set the cursor to use during the drag
    setDraggingCursor(Cursor.MOVE);
    // start the drag operation on mousedown
    setDistance(0);
    // register the GameController for dragStart and drag stop event
    FormulaireController gc = FormulaireController.getInstance();
    addDragStartHandler(gc);
    addDragStopHandler(gc);
  }

public String getType() {
	return type;
}

public void setType(String type) {
	this.type = type;
}



public Position getPosition() {
	return position;
}
  
  

}
