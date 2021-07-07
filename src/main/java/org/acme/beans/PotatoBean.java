package org.acme.beans;

import lombok.Getter;
import lombok.Setter;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.inject.Named;
import javax.ws.rs.Produces;

@Setter
@Getter
public class PotatoBean {
    private Float size;
    String countryOfOrigin;

    // A CDI producer method lets you take control of the process of
    // instantiating a CDI bean
    // данный метод может быть определен в любом классе,
    @Produces
    @Named("boldPotato")
    @RequestScoped
    public PotatoBean potatoBean(){
        PotatoBean potatoBean = new PotatoBean();
        potatoBean.setSize(12.4f);
        potatoBean.setCountryOfOrigin("Madagascar");
        return potatoBean;
    }
    // Producer methods are particularly useful when you want to supply
    // non-managed beans, using CDI and managed bean components. Say you
    // need to provide a third-party class with some PRECONFIGURED vars,
    // you use a producer method.

    //The “destructive” counterpart to a producer method is the disposer
    // method. This method will be called by the CDI runtime whenever
    // instances of VanillaBean need to be destroyed. I get to do my
    // cleanup here before the bean goes kaput.
    public void compostPotatoBean(@Disposes PotatoBean potatoBean){
        System.out.println("potato from " + potatoBean.countryOfOrigin + " is composted");
    }
    // A disposer method must be matched by a corresponding
    //producer method returning the same class. Without a matching
    //producer method, you will get a deployment exception. A producer
    //method on the other hand doesn’t require a disposer method.


    public String getCountry() {
        return countryOfOrigin;
    }
}
