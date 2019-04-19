package com.craftstone.stone.event;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Documented;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Event handler annotation that an event handler method forced to have
 * @author kmate
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface EventHandler {
	/**
	 * The priority of the event defaults to NORMAL
	 * @return the priority
	 */
	EventPriority priority() default EventPriority.NORMAL;
}
