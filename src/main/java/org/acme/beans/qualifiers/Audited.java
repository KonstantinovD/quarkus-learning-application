package org.acme.beans.qualifiers;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.*;

@Inherited // this annotation can be inherited by child classes.
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Audited { }
