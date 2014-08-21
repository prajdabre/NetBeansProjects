/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author RAJ
 */
public class Alliteration {

    /**
     * @param args the command line arguments
     */

  public static int count(String[] words)
  {
    int x=0;
    int y=0;
    for(int i=0;i<words.length-1;i++)
    {
    if(words[i].toUpperCase().charAt(0)==words[i+1].toUpperCase().charAt(0))
    x++;
    else
    {
    if(x>0)
    y++;
    x=0;
    }
    }

    if(x>0)
    y++;
    return y;
    }


    public static void main(String[] args) {
        // TODO code application logic here
        int i=count("she sells sea shells on the sea shore".split(" "));
        System.out.println(i);
    }

}
