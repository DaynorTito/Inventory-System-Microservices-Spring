package com.tienda.compraservice.infraestructure.adapters.adapterimpl;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;

public class UpdateHelper {

    /**
     * Updates the non-null fields of the target object with values from the source object
     *
     * @param source The source object
     * @param target The target object
     */
    public static void updateNonNullFields(Object source, Object target) {
        BeanWrapper sourceWrapper = new BeanWrapperImpl(source);
        BeanWrapper targetWrapper = new BeanWrapperImpl(target);

        for (PropertyDescriptor descriptor : sourceWrapper.getPropertyDescriptors()) {
            String propertyName = descriptor.getName();
            if (!"class".equals(propertyName) && !"category".equals(propertyName) && !"brand".equals(propertyName)) {
                Object sourceValue = sourceWrapper.getPropertyValue(propertyName);
                if (sourceValue != null) {
                    targetWrapper.setPropertyValue(propertyName, sourceValue);
                }
            }
        }
    }
}
