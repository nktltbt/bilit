package com.applifit.bilifit.client.formulaires;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


/*** Classe qui gére les listes de l'allocation ***/

public class DualListBox extends AbsolutePanel{
	
	private static final String CSS_DEMO_DUAL_LIST_EXAMPLE_CENTER = "demo-DualListExample-center";

	  private int LIST_SIZE;

	  private Button allLeft;

	  private Button allRight;

	  private ListBoxDragController dragController;

	  private MouseListBox left;

	  private Button oneLeft;

	  private Button oneRight;

	  private MouseListBox right;

	  public DualListBox(){
		  
	  }
	  
	  
	  public void construire( String width) {
	    HorizontalPanel horizontalPanel = new HorizontalPanel();
	    add(horizontalPanel);
	    horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

	    VerticalPanel verticalPanel = new VerticalPanel();
	    verticalPanel.addStyleName(CSS_DEMO_DUAL_LIST_EXAMPLE_CENTER);
	    verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

	    dragController = new ListBoxDragController(this);
	    left = new MouseListBox(dragController, LIST_SIZE);
	    right = new MouseListBox(dragController, LIST_SIZE);

	    left.setWidth(width);
	    right.setWidth(width);
	    
	    horizontalPanel.add(left);
	    horizontalPanel.add(verticalPanel);
	    horizontalPanel.add(right);

	    oneRight = new Button("&gt;");
	    oneLeft = new Button("&lt;");
	    allRight = new Button("&gt;&gt;");
	    allLeft = new Button("&lt;&lt;");
	    verticalPanel.add(oneRight);
	    verticalPanel.add(oneLeft);
	    verticalPanel.add(new HTML("&nbsp;"));
	    verticalPanel.add(allRight);
	    verticalPanel.add(allLeft);

	    allRight.addClickHandler(new ClickHandler() {
	      @Override
	      public void onClick(ClickEvent event) {
	        moveItems(left, right, false);
	      }
	    });

	    allLeft.addClickHandler(new ClickHandler() {
	      @Override
	      public void onClick(ClickEvent event) {
	        moveItems(right, left, false);
	      }
	    });

	    oneRight.addClickHandler(new ClickHandler() {
	      @Override
	      public void onClick(ClickEvent event) {
	        moveItems(left, right, true);
	      }
	    });

	    oneLeft.addClickHandler(new ClickHandler() {
	      @Override
	      public void onClick(ClickEvent event) {
	        moveItems(right, left, true);
	      }
	    });

	    ListBoxDropController leftDropController = new ListBoxDropController(left);
	    ListBoxDropController rightDropController = new ListBoxDropController(right);
	    dragController.registerDropController(leftDropController);
	    dragController.registerDropController(rightDropController);
	  }

	  /*** Ajouter la liste à gauche ***/
	  public void addLeft(String string , String id) {
	    left.add(string , id);
	  }
	  /*** Ajouter la liste à droite ***/
	  public void addRight(String string , String id) {
		    right.add(string , id);
	  }

	  /*** Déplacer un compte de la liste à gauche vers la liste à droite ***/
	  
	  protected void moveItems(MouseListBox from, MouseListBox to, boolean justSelectedItems) {
	    ArrayList<Widget> widgetList = justSelectedItems ? dragController.getSelectedWidgets(from)
	        : from.widgetList();
	    for (Widget widget : widgetList) {
	      // TODO let widget.removeFromParent() take care of from.remove()
	      from.remove(widget);
	      to.add(widget);
	    }
	  }
	  
	public int getLIST_SIZE() {
		return LIST_SIZE;
	}
	public void setLIST_SIZE(int lIST_SIZE) {
		LIST_SIZE = lIST_SIZE;
	}
	
	
	public MouseListBox getRight() {
		return right;
	}
	public void setRight(MouseListBox right) {
		this.left = right;
	}
	  
	  

}
