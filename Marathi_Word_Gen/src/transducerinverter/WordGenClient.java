/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package transducerinverter;

/**
 *
 * @author raj
 */
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

public class WordGenClient {

    public static void main(String[] args) throws Exception {

        URL url = new URL("http://localhost:9999/ws/hello?wsdl");

        //1st argument service URI, refer to wsdl document above
        //2nd argument is service name, refer to wsdl document above
        QName qname = new QName("http://transducerinverter/", "WordGenServiceService");

        Service service = Service.create(url, qname);

        WordGenServiceInterface hello = service.getPort(WordGenServiceInterface.class);

        String surface_words=hello.getSurfaceFormMod("कर");
        for(String s:surface_words.split("#")){
            System.out.println(s);
        }

    }
}
