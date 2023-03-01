/*
 *
 * Copyright 2023 Talkweb Co., Ltd.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * /
 */

package com.devops.plugins.core.pojo.yaml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.introspector.MissingProperty;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.introspector.PropertyUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;

public class JobsPropertityUtils  extends PropertyUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobsPropertityUtils.class);

    private boolean skipMissingProperties = false;


    public Property getProperty(Class<? extends Object> type, String name, BeanAccess bAccess) {
        Map<String, Property> properties = getPropertiesMap(type, bAccess);
        Property property = properties.get(name);
        if (property == null && skipMissingProperties) {
            property = new MissingProperty(name);
        }
        if (property == null) {
//            return properties.get("jobs");
//            throw new YAMLException(
//                    "Unable to find property '" + name + "' on class: " + type.getName());
            return properties.get("jobs");
        }
        return property;
    }

    @Override
    public boolean isSkipMissingProperties() {
        return skipMissingProperties;
    }

    @Override
    public void setSkipMissingProperties(boolean skipMissingProperties) {
        this.skipMissingProperties = skipMissingProperties;
    }

    /**
     * 获取字段的特定Annotation
     *
     * @param property        属性
     * @param javaBean        javaBean
     * @param annotationClass 注解的class类型
     * @param <T>             泛型，Annotation的子类
     * @return 注解，如果有。没有就返回null
     */
    private static <T extends Annotation> T getAnnotation(Property property, Object javaBean, Class<T> annotationClass) {
        try {
            Field field = javaBean.getClass().getDeclaredField(property.getName());
            return field.getAnnotation(annotationClass);
        } catch (NoSuchFieldException e) {
            LOGGER.info("SkipNullAndUnwrapMapRepresenter: NoSuchFieldException: class: {}, field: {}", javaBean.getClass().getName(), property.getName());
        }
        return null;
    }
}
