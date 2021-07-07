package org.acme.beans;

import org.acme.beans.qualifiers.Rare;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Produces;

// I’m using a different scope here, a narrower one –
// the request scope.
@RequestScoped
public class VanillaCake {
    // Then I inject my VanillaBean into this class, using
    //the bean’s name as a qualifier.
    @Inject
    @Named("deliciousVanilla")
    public VanillaBean vanillaBean;

    // can also just inject the same bean, without specifying a name
    // or any kind of qualifier. Because VanillaBean is annotated with
    // @ApplicationScoped, there’s only ever going to be one instance
    // of it in the entire context. So, aFlavor == vanillaBean is true.
    @Inject
    public FlavorTown secondVanillaBean;

    @Inject
    @Rare
    public FlavorTown rareBeaverVanilla;

    public VanillaCake(VanillaBean vanillaBean,
                       FlavorTown secondVanillaBean) {
        this.vanillaBean = vanillaBean;
        this.secondVanillaBean = secondVanillaBean;
    }

    public Integer getSecondVanillaBeanInt() {
        return secondVanillaBean.getRandomInt();
    }

    public Integer getRareBeaverVanillaInt() {
        return rareBeaverVanilla.getRandomInt();
    }


    // @Inject can also supply instances to methods
    @Inject
    public int gimmeSomeFlavor(FlavorTown flavor){
        return flavor.getRandomInt();
    }

    public int gimmeSomeDeliciousVanilla(){
        return vanillaBean.getRandomInt();
    }


    // With that method defined in any class, managed bean or not, the
    // following injection will obtain its bean instance from producer
    // method. The optional @Named qualifier and @RequestScoped I’ve supplied there will
    //also kick in at the injection site. With those two annotations, I’ve defined the
    //default name and scope of the VanillaBean. I can override the name the bean
    //at the point of injection, as well as set my desired scope before injection:
}
