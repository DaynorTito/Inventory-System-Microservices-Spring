package com.tienda.com.tienda.inventoryserver.infraestructure.adapters.adapterimpl;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;

public class UpdateHelper {

    /**
     * Method to update non-null fields from the source object to the target object
     *
     * @param source Source object with the values to copy from
     * @param target Target object where the values will be set
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
