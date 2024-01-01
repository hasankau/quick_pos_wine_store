/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Data;

import javax.swing.ImageIcon;

/**
 *
 * @author HASANKA
 */
public class Item {
    private String barcode;
    private String name;
    private String desc;
    private String manufacturer;
    private String threshold_value;
    private String unit_fixed_id;
    private String date_in;
    private String exp_date;
    private String qty;
    private String cash_price;
    private String credit_price;
    private String Item_item_code;
    private String discount_margin;
    private String type;
    
    private String aqty;
    private String adis;
    
    private byte[] img;
    private ImageIcon icon;
    private String iid;
    
    private String time;

    /**
     * @return the barcode
     */
    public String getBarcode() {
        return barcode;
    }

    /**
     * @param barcode the barcode to set
     */
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @param desc the desc to set
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * @return the manufacturer
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * @param manufacturer the manufacturer to set
     */
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    /**
     * @return the threshold_value
     */
    public String getThreshold_value() {
        return threshold_value;
    }

    /**
     * @param threshold_value the threshold_value to set
     */
    public void setThreshold_value(String threshold_value) {
        this.threshold_value = threshold_value;
    }

    /**
     * @return the unit_fixed_id
     */
    public String getUnit_fixed_id() {
        return unit_fixed_id;
    }

    /**
     * @param unit_fixed_id the unit_fixed_id to set
     */
    public void setUnit_fixed_id(String unit_fixed_id) {
        this.unit_fixed_id = unit_fixed_id;
    }

    /**
     * @return the date_in
     */
    public String getDate_in() {
        return date_in;
    }

    /**
     * @param date_in the date_in to set
     */
    public void setDate_in(String date_in) {
        this.date_in = date_in;
    }

    /**
     * @return the exp_date
     */
    public String getExp_date() {
        return exp_date;
    }

    /**
     * @param exp_date the exp_date to set
     */
    public void setExp_date(String exp_date) {
        this.exp_date = exp_date;
    }

    /**
     * @return the qty
     */
    public String getQty() {
        return qty;
    }

    /**
     * @param qty the qty to set
     */
    public void setQty(String qty) {
        this.qty = qty;
    }

    /**
     * @return the cash_price
     */
    public String getCash_price() {
        return cash_price;
    }

    /**
     * @param cash_price the cash_price to set
     */
    public void setCash_price(String cash_price) {
        this.cash_price = cash_price;
    }

    /**
     * @return the credit_price
     */
    public String getCredit_price() {
        return credit_price;
    }

    /**
     * @param credit_price the credit_price to set
     */
    public void setCredit_price(String credit_price) {
        this.credit_price = credit_price;
    }

    /**
     * @return the Item_item_code
     */
    public String getItem_item_code() {
        return Item_item_code;
    }

    /**
     * @param Item_item_code the Item_item_code to set
     */
    public void setItem_item_code(String Item_item_code) {
        this.Item_item_code = Item_item_code;
    }

    /**
     * @return the discount_margin
     */
    public String getDiscount_margin() {
        return discount_margin;
    }

    /**
     * @param discount_margin the discount_margin to set
     */
    public void setDiscount_margin(String discount_margin) {
        this.discount_margin = discount_margin;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the aqty
     */
    public String getAqty() {
        return aqty;
    }

    /**
     * @param aqty the aqty to set
     */
    public void setAqty(String aqty) {
        this.aqty = aqty;
    }

    /**
     * @return the adis
     */
    public String getAdis() {
        return adis;
    }

    /**
     * @param adis the adis to set
     */
    public void setAdis(String adis) {
        this.adis = adis;
    }

    /**
     * @return the img
     */
    public byte[] getImg() {
        return img;
    }

    /**
     * @param img the img to set
     */
    public void setImg(byte[] img) {
        this.img = img;
    }

    /**
     * @return the icon
     */
    public ImageIcon getIcon() {
        return icon;
    }

    /**
     * @param icon the icon to set
     */
    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    /**
     * @return the iid
     */
    public String getIid() {
        return iid;
    }

    /**
     * @param iid the iid to set
     */
    public void setIid(String iid) {
        this.iid = iid;
    }

    /**
     * @return the time
     */
    public String getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(String time) {
        this.time = time;
    }
}
