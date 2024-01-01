/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBClasses;


import Data.Item;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author HASANKA
 */
public class DB_query {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;

    public DB_query() {
        try {
            java_connect c = java_connect.getInstance();
            conn = c.DBConnection();
            conn.setAutoCommit(true);
        } catch (SQLException ex) {
            Logger.getLogger(DB_query_subtypes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

  
    public Vector loadBrands(){
        String sql = "SELECT brand from item";
        Vector v = new Vector();
        try {
            
            pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            v.add("");
            while (rs.next()) { 
                v.add(rs.getString(1));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occured!"+e, "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (Exception e) {
            }
        }

        return v;
    }
    
    public Vector loadItem(String barc){
        String sql = "SELECT barcode, name, item.desc, manufacturer, threshold_value, unit from item inner join unit_fixed on item.unit_fixed_id = unit_fixed.id where barcode = '"+barc+"'";
        Vector v = new Vector();
        try {
            
            pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) { 
                v.add(rs.getString(1));
                v.add(rs.getString(2));
                v.add(rs.getString(3));
                v.add(rs.getString(4));
                v.add(rs.getString(5));
                v.add(rs.getString(6));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occured!"+e, "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (Exception e) {
            }
        }

        return v;
    }
    
    public Vector loadManufacturers(){
        String sql = "SELECT distinct(manufacturer) from item";
        Vector v = new Vector();
        try {
            
            pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            v.add("");
            while (rs.next()) { 
                v.add(rs.getString(1));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occured!"+e, "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (Exception e) {
            }
        }

        return v;
    }
    
   
    public String getItemCode(){
        String sql = "SELECT max(item_code)+1 from item";
        
        
        String item = "1";
        try {
            
            pst = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = pst.executeQuery();
            if (rs.first()) { 
                item = rs.getString(1);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occured!"+e, "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (Exception e) {
            }
        }

        return item;
    }
    
    public Item getItem(String barc){
        String sql = "SELECT * from item where barcode='"+barc+"'";
        Item item = new Item();
        try {
            
            pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.first()) { 
                item.setBarcode(rs.getString(2));
                item.setName(rs.getString(3));
                item.setCash_price(rs.getString(7));
                item.setQty(rs.getString(9));
                item.setType(rs.getString(10));
            }else{
                item = null;
            }
        } catch (Exception e) {
               JOptionPane.showMessageDialog(null, "An error occured!"+e, "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (Exception e) {
            }
        }

        return item;
    }
    
    public boolean itemExists(String barcode){
        String sql = "SELECT * from item where barcode ='"+barcode+"'";
        
        
        try {
            
            pst = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = pst.executeQuery();
            if (rs.first()) { 
                return true;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occured!"+e, "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (Exception e) {
            }
        }

        return false;
    }
    
 
    public boolean insertIntoItem(String barcode, String name, String desc, String manufacturer, String pprice, String sprice, String thresh, String qty, String type, String path) {
        String sql = "insert into item(barcode, name, description, manufacturer, purchase_price, selling_price, threshold_value, qty, type, image) values('" + barcode + "', '" + name + "', '" + desc + "', '" + manufacturer + "', '" + pprice + "', '"+sprice+"', '"+thresh+"', '"+qty+"', '"+type+"', LOAD_FILE('" + path.replace("\\", "\\\\") + "'))";
        try {

            pst = conn.prepareStatement(sql);
            pst.execute();
            
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occured!"+e, "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "An error occured!"+e, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        return false;
    }
    
    
    
    
    
    
    
    public boolean UpdateItem(String id, String barcode, String name, String desc, String manufacturer, String pprice, String sprice, String thresh, String qty, String type) {
        String sql = "update item set barcode = '"+barcode+"', name = '" + name + "', description = '" + desc + "', manufacturer = '" + manufacturer + "', threshold_value = '" + thresh + "', purchase_price='"+pprice
                +"', selling_price='"+sprice+"', threshold_value = '"+thresh+"', qty = '"+qty+"', type = '"+type+"' where id='"+id+"'";
        try {
            pst = conn.prepareStatement(sql);
            pst.execute();
            
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occured!"+e, "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (Exception e) {
            }
        }

        return false;
    }
    
    public boolean UpdateItemImage(String ic, String barcode, String name, String desc, String manufacturer, String pprice, String sprice, String thresh, String qty, String type, String path) {
        String sql = "update item set barcode = '"+barcode+"', name = '" + name + "', description = '" + desc + "', manufacturer = '" + manufacturer + "', threshold_value = '" + thresh + "', purchase_price='"+pprice
                +"', selling_price='"+sprice+"', threshold_value = '"+thresh+"', qty = '"+qty+"', type = '"+type+"', image = LOAD_FILE('" + path.replace("\\", "\\\\") + "') where id='"+ic+"'";
        try {
            pst = conn.prepareStatement(sql);
            pst.execute();
            
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occured!"+e, "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (Exception e) {
            }
        }

        return false;
    }
    
    
    public boolean UpdateQty(String barcode, String qty) {
        String sql = "update item set qty = GREATEST(qty+'"+qty+"', 0) where barcode='"+barcode+"'";
        try {
            pst = conn.prepareStatement(sql);
            pst.execute();
            
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occured!"+e, "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (Exception e) {
            }
        }

        return false;
    }
    
    public boolean deleteFromItem(String id) {
        String sql = "delete from item where barcode='"+id+"'";
        try {

            pst = conn.prepareStatement(sql);
            pst.execute();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occured!"+e, "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (Exception e) {
            }
        }

        return false;
    }
    
    public int itemRowCount(){
        String sql = "SELECT count(id) from item";
        int i = 0;
        try {
            
            pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            
            if (rs.first()&&rs.getString(1)!=null) { 
                i = Integer.parseInt(rs.getString(1).trim());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occured!"+e, "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (Exception e) {
            }
        }

        return i;
    }

    
}
