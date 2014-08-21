/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author RAJ
 */
public class zeroonetwosort {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        int a[] = {1,2,1,2,0,1,0,1,0,1,2,1,2};
        int low = 0;
        int hi = a.length-1;
        
        for(int i:a){
            System.out.print(i+" ");
        }
        for(int i=0;i<a.length;){
            if(hi<i){
                break;
            }
            if(a[i]==0){
                a[i]=a[low];
                a[low]=0;
                
                while(a[low]==0){
                    low++;
                }
            } else if(a[i]==2){
                a[i]=a[hi];
                a[hi]=2;
                
                
                while(a[hi]==2){
                    hi--;
                }
                
            }
            
            i++;
            System.out.println();
        for(int j:a){
            System.out.print(j+" ");
        }
        }
        System.out.println();
        for(int i:a){
            System.out.print(i+" ");
        }
    }

}
