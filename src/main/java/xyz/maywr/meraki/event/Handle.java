package xyz.maywr.meraki.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author maywr
 * 28.06.2022 22:11
 * put this annotation on the method that should execute the event
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.METHOD )
public @interface Handle
{
}
