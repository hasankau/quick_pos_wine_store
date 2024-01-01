/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Data;

/**
 *
 * @author User
 */
public class Account {
    private String id;
    private String DATE;
    private String SHIFT;
    private String NAME;
    private String AMOUNT;
    private String METHOD;
    private String CHEQUE;
    private String customer_id;
    private String CHEQUE_DATE;
    private String CHEQUE_STATE;
    private String STATE;
    private String BILLNO;
    private String REF;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the DATE
     */
    public String getDATE() {
        return DATE;
    }

    /**
     * @param DATE the DATE to set
     */
    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    /**
     * @return the NAME
     */
    public String getNAME() {
        return NAME;
    }

    /**
     * @param NAME the NAME to set
     */
    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    /**
     * @return the AMOUNT
     */
    public String getAMOUNT() {
        return AMOUNT;
    }

    /**
     * @param AMOUNT the AMOUNT to set
     */
    public void setAMOUNT(String AMOUNT) {
        this.AMOUNT = AMOUNT;
    }

    /**
     * @return the METHOD
     */
    public String getMETHOD() {
        return METHOD;
    }

    /**
     * @param METHOD the METHOD to set
     */
    public void setMETHOD(String METHOD) {
        this.METHOD = METHOD;
    }

    /**
     * @return the CHEQUE
     */
    public String getCHEQUE() {
        return CHEQUE;
    }

    /**
     * @param CHEQUE the CHEQUE to set
     */
    public void setCHEQUE(String CHEQUE) {
        this.CHEQUE = CHEQUE;
    }

    /**
     * @return the customer_id
     */
    public String getCustomer_id() {
        return customer_id;
    }

    /**
     * @param customer_id the customer_id to set
     */
    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    /**
     * @return the CHEQUE_DATE
     */
    public String getCHEQUE_DATE() {
        return CHEQUE_DATE;
    }

    /**
     * @param CHEQUE_DATE the CHEQUE_DATE to set
     */
    public void setCHEQUE_DATE(String CHEQUE_DATE) {
        this.CHEQUE_DATE = CHEQUE_DATE;
    }

    /**
     * @return the BILLNO
     */
    public String getBILLNO() {
        return BILLNO;
    }

    /**
     * @param BILLNO the BILLNO to set
     */
    public void setBILLNO(String BILLNO) {
        this.BILLNO = BILLNO;
    }

    /**
     * @return the CHEQUE_STATE
     */
    public String getCHEQUE_STATE() {
        return CHEQUE_STATE;
    }

    /**
     * @param CHEQUE_STATE the CHEQUE_STATE to set
     */
    public void setCHEQUE_STATE(String CHEQUE_STATE) {
        this.CHEQUE_STATE = CHEQUE_STATE;
    }

    /**
     * @return the STATE
     */
    public String getSTATE() {
        return STATE;
    }

    /**
     * @param STATE the STATE to set
     */
    public void setSTATE(String STATE) {
        this.STATE = STATE;
    }

    /**
     * @return the REF
     */
    public String getREF() {
        return REF;
    }

    /**
     * @param REF the REF to set
     */
    public void setREF(String REF) {
        this.REF = REF;
    }

    /**
     * @return the SHIFT
     */
    public String getSHIFT() {
        return SHIFT;
    }

    /**
     * @param SHIFT the SHIFT to set
     */
    public void setSHIFT(String SHIFT) {
        this.SHIFT = SHIFT;
    }
}
