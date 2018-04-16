package com.bbva.hancock.sdk;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

class BeanUtils{
    //merge two bean by discovering differences
    public static <T> void merge(T target, T origin) throws Exception {
        BeanInfo beanInfo = Introspector.getBeanInfo(target.getClass());

        // Iterate over all the attributes
        for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {

            // Only copy writable attributes
            if (descriptor.getWriteMethod() != null) {
                Object originalValue = descriptor.getReadMethod()
                        .invoke(origin);

                // Only copy values values where the origin values is null
                if (originalValue != null) {
                    Object defaultValue = descriptor.getReadMethod().invoke(
                            origin);
                    descriptor.getWriteMethod().invoke(target, defaultValue);
                }

            }
        }
    }

}

