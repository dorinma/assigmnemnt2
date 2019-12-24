package bgu.spl.mics.application.passiveObjects;

import java.util.LinkedList;
import java.util.List;

/**
 *  That's where Q holds his gadget (e.g. an explosive pen was used in GoldenEye, a geiger counter in Dr. No, etc).
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Inventory {
	private List<String> gadgets;
	private static Inventory instance = null;
	/**
     * Retrieves the single instance of this class.
     */

	private Inventory() {
		gadgets = new LinkedList<>();
	}

	public static synchronized Inventory getInstance() {
		if (instance == null)
			instance = new Inventory();
		return instance;	}


    //ata structure containing all data necessary for initialization of the inventory.
	public void load (String[] inventory) {
		for (int i = 0; i < inventory.length; i++) {
			gadgets.add(inventory[i]);
		}
	}
	

	//‘false’ if the gadget is missing, and ‘true’ otherwise
	public boolean getItem(String gadget){
		for (String g: gadgets)
		{
			if(g.equals(gadget))
			{
				gadgets.remove(g);
				return true;
			}
		}
		return false;
	}

	public void printToFile(String filename){
		//TODO: Implement this
	}
}
