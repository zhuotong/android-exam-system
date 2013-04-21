import java.util.Scanner; 

class NoOprandException extends Exception{
    NoOprandException(){
     System.out.println("没有输入参数");
    }
}
    
 class OnlyOneException extends Exception{
    OnlyOneException() {
	System.out.println("只有一个数字");
    }
}

public class Application {

     public static void main(String args[]) throws NoOprandException,OnlyOneException {
         System.out.println("请输入两个数字");
         Scanner s = new Scanner(System.in);
	     String str = s.next();
	     s.close();
	  
	     String[] arr = str.split(",");
	     if(arr.length==0)
	    	 throw new NoOprandException();
         if(arr.length==1)
             throw new OnlyOneException();
         double x=Double.parseDouble(arr[0]);
         double y=Double.parseDouble(arr[1]);
         System.out.println("和"+(x+y));
      }
        
}
