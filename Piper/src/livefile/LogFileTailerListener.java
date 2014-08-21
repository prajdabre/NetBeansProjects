/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package livefile;

/**
 *
 * @author RAJ
 */
public interface LogFileTailerListener
{
  /**
   * A new line has been added to the tailed log file
   *
   * @param line   The new line that has been added to the tailed log file
   */
  public void newLogFileLine( String line );
}
