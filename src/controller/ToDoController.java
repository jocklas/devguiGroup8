package controller;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Locale;

import javax.swing.ImageIcon;

import model.Category;
import model.LocaliziedTexts;
import model.TableToDoItemModel;
import model.ToDoItem;
import model.ToDoItemModel;
import view.MainView;
import exceptions.ToDoItemExistsException;

/**
 * Created with IntelliJ IDEA.
 * User: Joakim
 * Date: 2013-02-14
 * Time: 22:12
 */
public class ToDoController extends ComponentAdapter {
    private ToDoItemModel model;
    private AddAction add;
    private EditAction edit;
    private OkAction ok;
    private DeleteAction delete;
    private CancelAction cancel;
    private ChangeLanguageAction language;
    private AboutAction about;
    private Config conf;
    private LocaliziedTexts lang;
    private MainView view;


    /**
     * Constructor for the controller. Creates all actions and sets object fields from input parameters.
     * @param newModel The model of the current application
     * @param newLang The language object of the application
     */
    public ToDoController(ToDoItemModel newModel, LocaliziedTexts newLang){
        this.model = newModel;
        this.lang = newLang;
        this.add =
                new AddAction(
                        lang.getText("ui.mainview.menu.edit.add"),
                        createNavigationIcon("/Add16"),
                        lang.getText("ui.mainview.addAction"),
                        KeyEvent.VK_A, this);
        this.edit =
                new EditAction(
                        lang.getText("ui.mainview.menu.edit.edit"),
                        createNavigationIcon("/Edit16"),
                        lang.getText("ui.mainview.editAction"),
                        KeyEvent.VK_E, this);
        this.delete =
                new DeleteAction(
                        lang.getText("ui.mainview.menu.edit.delete"),
                        createNavigationIcon("/Delete16"),
                        lang.getText("ui.mainview.deleteAction"),
                        KeyEvent.VK_D, this);
        this.ok =
                new OkAction(
                		lang.getText("ui.editview.button.ok"),
                        createNavigationIcon("uknownicon"),
                        lang.getText("ui.mainview.okAction"),
                        KeyEvent.VK_O, this);
        this.cancel =
                new CancelAction(
                        "Cancel",
                        createNavigationIcon("unkownicon"),
                        lang.getText("ui.mainview.cancelAction"),
                        KeyEvent.VK_C, this);
        this.language =
                new ChangeLanguageAction(
                        lang.getText("ui.mainview.menu.file.changeLanguage"),
                        createNavigationIcon("/Information16"),
                        lang.getText("ui.mainview.languageAction"),
                        KeyEvent.VK_L, this);
        this.about =
                new AboutAction(
                        lang.getText("ui.mainview.menu.help.about"),
                        createNavigationIcon("/About16"),
                        lang.getText("ui.mainview.aboutAction"),
                        KeyEvent.VK_F, this);
    }

    /**
     * Method to add a new item to the model.
     * @param title title string input by the user
     * @return null or the new item
     */
    public ToDoItem addItem(String title) {
        ToDoItem item = null;

        try{
        item = (ToDoItem)model.createToDoItem(title);
        } catch (ToDoItemExistsException e) {
            item = null;
        }

        return item;
    }

    /**
     * Fetches all the categories.
     * @return list with categories
     */
    public List<Category> getCategories(){
    	return model.getAllCategories();
    }

    /**
     * Method to get item to delete or inject to the editframe
     * @param index The place in the table where the item resides.
     * @return the requested item or null.
     */
    public ToDoItem getEditItem(int index) {
        //model.getItem(index);
        return null;
    }

    /**
     * Update the corresponding item in the model.
     * @param newItem Item supplied by the edit frame.
     */
    public void updateEditItem(ToDoItem newItem) {
        model.updateToDoItem(model.getIndexOfToItem(newItem), newItem);
    }

    /**
     * Get method for the Add action.
     * @return
     */
    public AddAction getAddAction() {
        return add;
    }

    /**
     * Set method for the config object.
     * @param newConf
     */
    public void setConfig(Config newConf) {
        this.conf = newConf;
    }

    /**
     * Get method for the Edit action.
     * @return
     */
    public EditAction getEditAction() {
        return edit;
    }

    /**
     * Get method for the Delete action.
     * @return
     */
    public DeleteAction getDeleteAction() {
        return delete;
    }

    /**
     * Get method for the about action
     * @return
     */
    public AboutAction getAboutAction() {
        return about;
    }

    /**
     * Get method for the Cancel action.
     * @return
     */
    public CancelAction getCancelAction() {
        return cancel;
    }

    /**
     * Get method for the Ok action.
     * @return
     */
    public OkAction getOkAction() {
        return ok;
    }

    /**
     * Get method for the change language action.
     * @return
     */
    public ChangeLanguageAction getLanguageAction() {
        return language;
    }

    /**
     * This method is used to add the observer to the observable model.
     * @param arg
     */
    public void addObserver(MainView arg) {
        this.view = arg;
        model.addObserver(view);
    }

    /**
     * This method is used by the controller to update the language of all the actions it has control over.
     * @param lang
     */
    public void updateLanguage(Locale arg) {
        Locale.setDefault(arg);
        conf.setProp("locale",arg.getLanguage());
        lang.refreshTexts();
        setLanguage();
    }

    /**
     * Method to get the image from the datapath.
     * @param imageName string name of the image file
     * @return null or the image file.
     */
    protected static ImageIcon createNavigationIcon(String imageName) {
        String imgLocation = ""
                + imageName
                + ".gif";
        java.net.URL imageURL = ToDoController.class.getResource(imgLocation);

        if (imageURL == null) {
            System.err.println("Resource not found: "
                    + imgLocation);
            return null;
        } else {
            return new ImageIcon(imageURL);
        }
    }

    /**
     * Method to set the language of the application. Will call all update methods in all the classes
     * the controller have control over.
     */
    private void setLanguage() {
        this.edit.updateLanguage(lang);
        this.add.updateLanguage(lang);
        this.delete.updateLanguage(lang);
        this.language.updateLanguage(lang);
        this.cancel.updateLanguage(lang);
        this.about.updateLanguage(lang);
        this.ok.updateLanguage(lang); 
        view.table.setModel(new TableToDoItemModel(model, lang));
        this.view.updateLanguage(lang);
    }
    
    /**
     * This method detects all movements of observed windows and saves the new position to the config,
     * so that we can show them at the same position at next start.
     * @see ComponentAdapter#componentMoved(ComponentEvent)
     */
    @Override
    public void componentMoved(ComponentEvent e) {
		System.out.println("DEBUG: any component moved");
    	//save position of MainWindow
//    	if(e.getComponent().getClass().equals(MainView.class)){ //as we have a local jframe *inside* the mainview, that doesn't work
    		System.out.println("DEBUG: mainview moved");
    		this.conf.setProp("windowXPos",Integer.toString(e.getComponent().getX()));
    		this.conf.setProp("windowYPos",Integer.toString(e.getComponent().getY()));
//    	}
    }
    
    /**
     * This method detects if a observed window is resized and saves the new position to the config,
     * so that we can show them with the same size at next start.
     * @see ComponentAdapter#componentResized(ComponentEvent)
     */
    @Override
    public void componentResized(ComponentEvent e) {
		System.out.println("DEBUG any component got resized");
    	//save position of MainWindow
//    	if(e.getComponent().getClass().equals(MainView.class)){ //as we have a local jframe *inside* the mainview, that doesn't work
    		System.out.println("DEBUG mainview got resized");
    		this.conf.setProp("windowHeight",Integer.toString(e.getComponent().getHeight()));
    		this.conf.setProp("windowWidth",Integer.toString(e.getComponent().getWidth()));
//    	}
    }

    /**
     * Method to supplie the actions with the text load file
     * @return the objects language object.
     */
	public LocaliziedTexts getLanguage() {
		// TODO Auto-generated method stub
		return this.lang;
	}
}

