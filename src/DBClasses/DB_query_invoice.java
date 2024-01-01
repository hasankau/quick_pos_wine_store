/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBClasses;


import Data.Account;
import Data.Customer;
import Data.Item;
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
public class DB_query_invoice {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;

    public DB_query_invoice() {
        try {
            java_connect c = java_connect.getInstance();
            conn = c.DBConnection();
            conn.setAutoCommit(true);
        } catch (SQLException ex) {
            Logger.getLogger(DB_query_subtypes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Vector loadUnitPrices(String code){
        String sql = "SELECT credit_price from stock where item_item_code=(select item_code from item where barcode='"+code+"') group by cash_price";
        Vector v = new Vector();
        try {
            
            pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
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
    
    public Vector loadStocks(String code){
        String sql = "SELECT stock_id, credit_price from stock where item_item_code=(select item_code from item where barcode='"+code+"') and qty > 0 group by cash_price";
        Vector v = new Vector();
        try {
            
            pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) { 
                v.add("Stock id-"+rs.getString(1)+" Rs. "+rs.getString(2));
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
    
    public String loadDiscountAmounts(String code){
        String sql = "SELECT round(discount_margin) from stock where item_item_code=(select item_code from item where barcode='"+code+"')";
        String discount_margin = "0";
        try {
            
            pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) { 
                discount_margin = rs.getString(1);
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

        return discount_margin;
    }
    
    public String loadCustmer(){
        String sql = "SELECT max(id)+1 from customer";
        String i = "";
        try {
            
            pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.first()&&rs.getString(1)!=null) { 
                i =  rs.getString(1);
            }else{
                return "1";
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
    
    public boolean isCustomerExists(String id){
        String sql = "SELECT id from customer where id='"+id+"'";
        
        try {
            
            pst = conn.prepareStatement(sql);
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
    
    public Customer getCustomer(String cid){
        String sql = "SELECT * from item where barcode='"+cid+"'";
        Customer c = new Customer();
        try {
            
            pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.first()) { 
                c.setId(rs.getString(1));
                c.setFirst_name(rs.getString(2));
                c.setLast_name(rs.getString(3));
                c.setTel(rs.getString(4));
            }else{
                c = null;
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

        return c;
    }
    
    public boolean saveCustomer(String fname, String lname, String tel) {
        String sql = "insert into customer (first_name, last_name, tel) value('" + fname + "', '" + lname + "', '" + tel + "')";
        try {

            pst = conn.prepareStatement(sql);
            pst.execute();

            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occured! yrdr yd5e" + e, "Error", JOptionPane.ERROR_MESSAGE);
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
    
    public String getMaxCusId() {
        String sql = "SELECT max(id) from customer";

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
    
    public String getInno(){
        String sql = "SELECT max(invoice_id)+1 from invoice";
        
        
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
    
    public Item getItem(String barc){
        String sql = "SELECT * from item where barcode='"+barc+"'";
        Item item = new Item();
        try {
            
            pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.first()) { 
                item.setItem_item_code(rs.getString(1));
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
    
    
    
    public Vector getItems(){
        String sql = "SELECT * from item order by id";
        Vector v = new Vector();
        try {
            
            pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while(rs.next()) { 
                Item item = new Item();
                item.setItem_item_code(rs.getString(1));
                item.setBarcode(rs.getString(2));
                item.setName(rs.getString(3));
                item.setCash_price(rs.getString(7));
                item.setQty(rs.getString(9));
                item.setType(rs.getString(10));
                item.setImg(rs.getBytes(11));
                item.setDesc(rs.getString(4));
                
                v.add(item);
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
    
    
    public boolean insertIntoEmpty(String barcode, String qty, String date) {
        String sql = "insert into empty(qty, date, item_id) values('" + qty + "', '" + date + "', (select id from item where barcode = '"+barcode+"'))";
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
    
    
    public boolean updateSale(String up) {
        String sql = "update invoice_details set unit_price = unit_price-'"+up+"', sub_total = sub_total-'"+up+"' order by id desc limit 1";
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
    
    
    public boolean getStockav(String stock_id, String qty){
        String sql = "SELECT qty from stock where stock_id='"+stock_id+"' and qty>='"+qty+"'";
        
        try {
            
            pst = conn.prepareStatement(sql);
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
    
    public String getStockQty(String stock_id, String qty){
        String sql = "SELECT qty from stock where stock_id='"+stock_id+"'";
        
        try {
            
            pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.first()) { 
                
                return rs.getString(1);
                
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

        return "";
    }
    
    public Vector getStock(String barc){
        String sql = "select barcode, name, item.desc, manufacturer, unit_fixed_id, exp_date, qty, cash_price, credit_price, discount_margin\n" +
"from item inner join stock on item.item_code=stock.item_item_code\n" +
"where barcode= '" + barc +"'";
        
        Vector<Item> v = new Vector();
        try {
            
            pst = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) { 
                Item item = new Item();
                item.setBarcode(rs.getString(1));
                item.setName(rs.getString(2));
                item.setDesc(rs.getString(3));
                item.setManufacturer(rs.getString(4));
                item.setUnit_fixed_id(rs.getString(5));
                
                item.setExp_date(rs.getString(6));
                item.setQty(rs.getString(7));
                item.setCash_price(rs.getString(8));
                item.setCredit_price(rs.getString(9));
                item.setDiscount_margin(rs.getString(10));
                v.add(item);
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
    
 

    
    
    

public boolean updateAccountC(Account a) {
        String sql = "update customer set current_credit_amount=GREATEST(current_credit_amount+'"+a.getAMOUNT()+"', 0)  where id = '"+a.getCustomer_id()+"'";
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

}


