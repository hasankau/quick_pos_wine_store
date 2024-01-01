/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBClasses;

import DBClasses.java_connect;
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
public class DB_invoice {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;

    public DB_invoice() {
        try {
            java_connect c = java_connect.getInstance();
            conn = c.DBConnection();
            conn.setAutoCommit(true);
        } catch (SQLException ex) {
            Logger.getLogger(DB_query_subtypes.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean insertIntoInvoice(String invoice_id, String date, String total, String item_count, String balance, String user_id, String paid_amount, String payment_type_id, String cus, String ref, String time) {
        String sql = "insert into invoice(invoice_id, date, total, item_count, payed_amount, balance, user_id, payment_type_id, customer_id, ref, time) values('" + invoice_id + "', '" + date + "', '" + total + "', '" + item_count + "', '" + paid_amount + "', '" + balance + "', '" + user_id + "',  '" + payment_type_id + "', '" + cus + "', '" + ref + "', '"+time+"')";
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

    public boolean insertIntoInvoiceDetails(String barcode, String inv_id, String unit_price, String qty, String discount, String sub_total, String time) {
        String sql = "insert into invoice_details(invoice_invoice_id, Item_id, purchase_price, unit_price, qty, discount, sub_total, time) values('" + inv_id + "', (select id from item where barcode='" + barcode + "'), (select purchase_price from item where barcode='" + barcode + "'), '" + unit_price + "', '" + qty + "', '" + discount + "', '" + sub_total + "', '"+time+"')";

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

    
    
    public boolean deleteInvoiceDetails(String iid) {
        String sql = "update invoice_details set status = 0 where invoice_invoice_id = '" + iid + "'";

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
    
    public boolean updateSafe(String am) {
        String sql = "update safe set amount = amount+'"+am+"'";

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
    
    public boolean updateInvoiceDetails(String iid, String qty, String price) {
        String sql = "update invoice_details set qty = '"+qty+"', unit_price = '"+price+"', sub_total = "+qty+"*"+price+" where invoice_invoice_id = '" + iid + "'";

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
    
    
    public boolean deleteInvoice(String iid) {
        String sql = "update invoice set status = 0 where invoice_id = '" + iid + "'";

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
    
    
    public boolean updateStock(String barcode, String qty) {
        String sql = "update item set qty=GREATEST(qty-'" + qty + "', 0) where barcode ='" + barcode + "';";
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

    public boolean insertIntoAdvanceDetails(String barcode, String ad_id, String unit_price, String qty, String discount, String sub_total) {
        String sql = "insert into advanced_items(advance_id, item_id, qty, sub_total) values('" + ad_id + "', (select id from item where barcode='" + barcode + "'), '" + qty + "', '" + sub_total + "')";

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

    public boolean insertIntoAdvance(String date, String paid, String balance, String user_id, String cus, String code) {
        String sql = "insert into advance(date, paid, total, customer_id, user_id, status, code) values('" + date + "', '" + paid + "', '" + balance + "', '" + cus + "', '" + user_id + "', '" + 0 + "', '" + code + "')";
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

    public String getAdno() {
        String sql = "SELECT max(id) from advance";

        String item = null;
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
    
    
    
    public String getCash() {
        String sql = "SELECT sum(invoice_details.sub_total) from invoice_details inner join invoice on invoice.invoice_id = invoice_details.invoice_invoice_id";

        String item = "0";
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

    
    public String getCash1() {
        String sql = "SELECT sum(invoice_details.sub_total)-(select sum(amount) from safe) from invoice_details inner join invoice on invoice.invoice_id = invoice_details.invoice_invoice_id";

        String item = "0";
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
    
    
    public String getSafe() {
        String sql = "SELECT sum(amount) from safe";

        String item = "0";
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
    
    
    public Vector getItem(String adid) {
        String sql = "SELECT * from item inner join advanced_items on advanced_items.item_id=item.id where advance_id= '" + adid + "'";
        Vector v = new Vector();

        try {

            pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString(1));
                Item item = new Item();
                item.setBarcode(rs.getString(2));
                item.setName(rs.getString(3));
                item.setCash_price(rs.getString(7));
                item.setQty(rs.getString(9));
                item.setType(rs.getString(10));
                item.setAqty(rs.getString(12));
                item.setAdis(rs.getString(13));
                v.add(item);

            }
        } catch (Exception e) {
            e.printStackTrace();
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

    
    public Vector getAdvance(String adid) {
        String sql = "SELECT * from advance where id = '" + adid + "'";
        Vector v = new Vector();

        try {

            pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                
                v.add(rs.getString(3));
                v.add(rs.getString(4));
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
    
    
    public boolean updateAdvance(String adid) {
        String sql = "update advance set status = 1 where id = '" + adid + "'";
        Vector v = new Vector();

        try {

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
            } catch (Exception e) {
            }   
        }

        return false;
    }
    
    
    
    public Vector getCart(){
        String sql = "SELECT *, invoice_details.time as ti from invoice_details inner join item on invoice_details.item_id = item.id where invoice_details.status = 1 order by invoice_details.id desc limit 20";
        Vector v = new Vector();
        
        try {
            
            pst = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = pst.executeQuery();
            while(rs.next()) { 
                
                Item i = new Item();
                i.setItem_item_code(rs.getString("id"));
                i.setBarcode(rs.getString("barcode"));
                i.setName(rs.getString("name"));
                i.setCash_price(rs.getString("invoice_details.unit_price"));
                i.setQty(rs.getString("invoice_details.qty"));
                i.setCredit_price(rs.getString("invoice_details.sub_total"));
                i.setIid(rs.getString("invoice_invoice_id"));
                i.setTime(rs.getString("ti"));
                
                v.add(i);
                
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
    
}
