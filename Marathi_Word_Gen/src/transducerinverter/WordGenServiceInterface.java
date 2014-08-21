/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package transducerinverter;
import java.util.HashSet;
import java.util.Set;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
/**
 *
 * @author raj
 */
//Service Endpoint Interface
@WebService
@SOAPBinding(style = Style.RPC)
public interface WordGenServiceInterface{
 
	@WebMethod String getSurfaceForm(String name);
        @WebMethod String getSurfaceFormMod(String name);
 
}