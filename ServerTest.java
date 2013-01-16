/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package instantmessenger;

import javax.swing.JFrame;

/**
 *
 * @author Paul
 */
public class ServerTest {
    
    public static void main(String[] args){
        
        Server srv = new Server();
        srv.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        srv.startRunning();
        
    }
    
}
