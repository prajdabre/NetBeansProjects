/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author RAJ
 */
public class recurmaxmin {
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

test w=new test();
int mm[]=        w.maxmin(0,9);
        System.out.println(mm[0]+" "+mm[1]);
    }

    

}

class test

{
   int a[]={-1,6,2,1,4,86,10,30,2,30};
    
 int[] maxmin(int i,int j)
    {
int mm1[]=new int[2];
int mm2[]=new int[2];
int mid;
    if(i==j)
        {
            mm1[0]=mm1[1]=a[i];
            return mm1;
        }
 else
    {
        if(i==j-1)
        {
        if(a[i]<a[j])
        {
        mm1[0]=a[j];
        mm1[1]=a[i];
        return mm1;
        }
 else
        {
        mm1[1]=a[j];
        mm1[0]=a[i];
        return mm1;
        }

        }
 else
        {
            
        mid=    (int) Math.floor((i+j)/2);
        mm1=maxmin(i,mid);
        mm2=maxmin(mid+1,j);
        if(mm1[0]<mm2[0]) mm1[0]=mm2[0];
        if(mm1[1]>mm2[1]) mm1[1]=mm2[1];

        }
    }


return mm1;
    }

}