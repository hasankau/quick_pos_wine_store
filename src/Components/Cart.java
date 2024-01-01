/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components;

import java.util.HashMap;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author HASANKA
 */
public class Cart {

    private static HashMap<String, Cart_Item> cartlist = new HashMap();
    private int itemid = 0;
    Vector v = new Vector();
    private static Cart instance = null;

    private void Cart() {

    }

    public static Cart getInstance() {
        if (instance == null) {
            instance = new Cart();
        }
        return instance;
    }

    /**
     * @return the cartlist
     */
    public HashMap<String, Cart_Item> getCartlist() {
        return cartlist;
    }

    public void addToCart(Cart_Item item, double qty, ImageIcon img) {
        if (!cartlist.containsKey(item.getItemCode())) {
            item.setQty(qty);
            cartlist.put(item.getItemCode(), item);
            itemid = cartlist.size() + 1;
        } else {

            Cart_Item update = cartlist.get(item.getItemCode());
            update.setQty(qty);
            cartlist.replace(item.getItemCode(), update);

        }

    }

    public void removeFromCart(String code) {
        if (cartlist.containsKey(code)) {
            cartlist.remove(code);
        }
        itemid = cartlist.size() + 1;

    }

    /**
     * @param cartlist the cartlist to set
     */
    public void setCartlist(HashMap<String, Cart_Item> cartlist) {
        this.cartlist = cartlist;
    }

    /**
     * @return the itemid
     */
    public int getItemid() {
        return itemid;
    }

    /**
     * @param itemid the itemid to set
     */
    public void setItemid(int itemid) {
        this.itemid = itemid;
    }

}
