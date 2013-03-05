package controller;

import model.LocalizedTexts;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Joakim
 * Date: 2013-03-05
 * Time: 16:10
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("serial")
public class SetDoneAction extends AbstractAction {
    private ToDoController parent;
    private JTable table;

    /**
     * Constructor, nothing fancy!
     * @param text The title for the component using this action.
     * @param icon The icon for the component using this action.
     * @param desc The hovertext of the component using this action.
     * @param mnemonic The mnemonic of the component using this action.
     * @param controller This actions controller parent.
     */
    public SetDoneAction(String text, ImageIcon icon,
                        String desc, Integer mnemonic,
                        ToDoController controller) {
        super(text, icon);
        putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, mnemonic);
        this.parent = controller;
    }

    /**
     * Setmethod for the actions table reference.
     * @param newTable the current table of the mainview.
     */
    public void setTable(JTable newTable) {
        this.table = newTable;
    }

    /**
     * Override method to handle the event that the user want's to delete an item.
     * @param event the event fired from the user.
     */
    public void actionPerformed(ActionEvent event){
        int index = table.getSelectedRow();
        parent.setSelected(index);
    }

    /**
     * Language set method. Sets the strings of this object according to the input language localization object.
     * @param lang Language localization class to get correct textstrings.
     */
    public void updateLanguage(LocalizedTexts lang) {
        putValue(NAME, lang.getText("ui.mainview.menu.edit.delete"));
        putValue(SHORT_DESCRIPTION,lang.getText("ui.mainview.deleteAction"));
    }

}
