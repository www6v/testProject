package spring.proxy.factory.bean;

public class AServiceImpl implements AService{	 
    public void fooA(String _msg) {

         System.out.println("AServiceImpl.fooA(msg:"+_msg+")");

    }
 
    public void barA() {

         System.out.println("AServiceImpl.barA()");  

    }
 
}