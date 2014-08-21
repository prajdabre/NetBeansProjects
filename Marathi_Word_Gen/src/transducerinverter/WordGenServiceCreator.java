/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package transducerinverter;

/**
 *
 * @author raj
 */
import javax.xml.ws.Endpoint;

//Endpoint publisher
public class WordGenServiceCreator {

    public static void main(String[] args) {
        
        Endpoint.publish("http://localhost:9999/ws/hello", new WordGenService());
        System.out.println("Service chalu ho gaya hai bhai logon!");
    }
}
