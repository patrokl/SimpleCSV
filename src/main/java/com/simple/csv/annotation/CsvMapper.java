package com.simple.csv.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Identify field for mapping with CSV file header
 */
@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CsvMapper {

    /**
     * Header name in CSV file. Header name must be unique
     */
    String headerName() default "";

    /**
     * Optional. Used only for mapping nested objects
     */
    String mappingField() default "";
}
