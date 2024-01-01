/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author User
 */
public class CardReader {

    static TerminalFactory factory = TerminalFactory.getDefault();

    public static void main(String[] args) {
        
    }
    
    public String read(){
        String ref = "";
        try {
            List<CardTerminal> terminals = factory.terminals().list();

            System.out.println("Terminals: " + terminals);
            // get the first terminal
            CardTerminal terminal = terminals.get(0);
            // establish a connection with the card
            Card card = terminal.connect("T=0");
            System.out.println("card: " + card);
            CardChannel channel = card.getBasicChannel();
            ResponseAPDU r = channel.transmit(new CommandAPDU(0x00, 0x84, 0x00, 0x00, 0x08));
            System.out.println("response: " + DatatypeConverter.printHexBinary(r.getBytes()));
            ref = DatatypeConverter.printHexBinary(r.getBytes());
            // disconnect
            card.disconnect(false);
        } catch (CardException ex) {
            Logger.getLogger(CardReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ref;
    }
}
