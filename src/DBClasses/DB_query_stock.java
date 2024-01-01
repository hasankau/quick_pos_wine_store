/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBClasses;

import Data.Item;
import Data.StockItem;
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
public class DB_query_stock {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;

    public DB_query_stock() {
        java_connect c = java_connect.getInstance();
        conn = c.DBConnection();
    }

    public Vector loadUnitPrices(String code) {
        String sql = "SELECT sprice from stock_in";
        Vector v = new Vector();
        try {

            pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            v.add("0.0");
            while (rs.next()) {
                v.add(rs.getString(1));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occured!" + e, "Error", JOptionPane.ERROR_MESSAGE);
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

    public Vector loadSellPrices(String code) {
        String sql = "SELECT sprice from stock_in";
        Vector v = new Vector();
        try {

            pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            v.add("0.0");
            while (rs.next()) {
                v.add(rs.getString(1));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occured!" + e, "Error", JOptionPane.ERROR_MESSAGE);
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

    public Vector loadSuppliers() {
        String sql = "SELECT name from supplier";
        Vector v = new Vector();
        try {

            pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            v.add("");
            while (rs.next()) {
                v.add(rs.getString(1));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occured!" + e, "Error", JOptionPane.ERROR_MESSAGE);
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

    public String getItemCode() {
        String sql = "SELECT max(item_code)+1 from item";

        String item = "1";
        try {

            pst = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = pst.executeQuery();
            if (rs.first()) {
                item = rs.getString(1);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occured!" + e, "Error", JOptionPane.ERROR_MESSAGE);
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

    public Vector getPaymentMethods() {
        String sql = "SELECT * from payment_type";
        Vector<String> v = new Vector<>();

        try {

            pst = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                v.add(rs.getString(2));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occured!" + e, "Error", JOptionPane.ERROR_MESSAGE);
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

    public boolean insertIntoStockIn(StockItem item) {
        String sql = "update stock_in set qty = '"+item.getQty()+"' where barcode = '"+item.getItem_item_code()+"'";
        try {

            pst = conn.prepareStatement(sql);
            pst.execute();
            //JOptionPane.showMessageDialog(null, "Stock saved", "Successful", JOptionPane.ERROR_MESSAGE);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occured!" + e, "Error", JOptionPane.ERROR_MESSAGE);
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

    
    
    
    
    
    
        public boolean insertIntoStockIn(Item item) {
        String sql = "update stock_in set qty = '"+item.getQty()+"' where barcode = '"+item.getItem_item_code()+"'";
        try {

            pst = conn.prepareStatement(sql);
            pst.execute();
            //JOptionPane.showMessageDialog(null, "Stock saved", "Successful", JOptionPane.ERROR_MESSAGE);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occured!" + e, "Error", JOptionPane.ERROR_MESSAGE);
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
        
        
        
        
        public String getInno(){
        String sql = "SELECT max(grn.in)+1 from grn";
        
        
        String item = null;
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
        
        
        
        
        public boolean insertIntoGrnd(Item item) {
        String sql = "insert into gd (item_id, qty, cost, grn_in) values ((select id from item where barcode = '"+item.getBarcode()+"'), '"+item.getQty()+"', '"+item.getCredit_price()+"', '"+item.getIid()+"')";
        try {

            pst = conn.prepareStatement(sql);
            pst.execute();
            //JOptionPane.showMessageDialog(null, "Stock saved", "Successful", JOptionPane.ERROR_MESSAGE);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occured!" + e, "Error", JOptionPane.ERROR_MESSAGE);
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
        
        
        
        public boolean updateStock(String barcode, String qty) {
        String sql = "update item set qty=GREATEST(qty+'" + qty + "', 0) where barcode ='" + barcode + "';";
        try {
            java_connect c = java_connect.getInstance();
            conn = c.DBConnection();
            conn.setAutoCommit(false);

            pst = conn.prepareStatement(sql);
            pst.execute();

            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occured!" + e, "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                conn.commit();
            } catch (Exception e) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    Logger.getLogger(DB_invoice.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return false;
    }
        
        
        
        public boolean insertIntoGrn(Item item) {
        String sql = "insert into grn (date, total, supplier_supplier_id) values ('"+item.getDate_in()+"', '"+item.getThreshold_value()+"', '"+item.getIid()+"')";
        try {

            pst = conn.prepareStatement(sql);
            pst.execute();
            //JOptionPane.showMessageDialog(null, "Stock saved", "Successful", JOptionPane.ERROR_MESSAGE);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occured!" + e, "Error", JOptionPane.ERROR_MESSAGE);
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
    
    
}
