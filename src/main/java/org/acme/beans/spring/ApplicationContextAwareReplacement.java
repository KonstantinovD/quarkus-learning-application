package org.acme.beans.spring;

import org.springframework.util.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import java.util.HashSet;

@ApplicationScoped
public class ApplicationContextAwareReplacement {

    // obtain an injection of the BeanManager object.
    //There’s only one, just like the ApplicationContext
    //in the Spring Framework.
    @Inject
    BeanManager beanManager;

    public ASpringOrCdiBean getBean() {
        Bean namedCdiBean = null;
        // use the BeanManager to search for the CDI bean by name.
        HashSet<Bean<?>> beans = (HashSet<Bean<?>>)
//          beanManager.getBeans(Object.class, new AnnotationLiteral<Any>() {});
//          beanManager.getBeans("aSpringOrCdiBean");
          beanManager.getBeans(ASpringOrCdiBean.class, new AnnotationLiteral<Any>() {});
        if (!beans.isEmpty()) {
            namedCdiBean = beans.iterator().next();
            // Each CDI bean is associated with a context, but here’s
            //where things differ from Spring. In the CDI world,
            //beans and the context are somewhat disconnected.
            //In Spring, the context is tightly coupled with the
            //beans. Way more flexibility in CDI is what I’m
            //saying. For that reason, I need to manufacture a
            //CreationalContext object, to obtain a contextually
            //valid reference to the bean I’m interested in.
            CreationalContext creationalContext = beanManager
              .createCreationalContext(namedCdiBean);
            // Finally, use the BeanManager and the CreationalContext
            // to get the bean from the generic Bean container object.
            ASpringOrCdiBean bean =
              (ASpringOrCdiBean) beanManager.getReference(namedCdiBean,
                namedCdiBean.getBeanClass(), creationalContext);
            if (StringUtils.isEmpty(bean.getValue())) {
                bean.setValue("received from BeanManager");
            }
            return bean;
        } else {
            return null;
        }
    }
}
