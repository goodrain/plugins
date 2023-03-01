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

import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.introspector.PropertyUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 让Snakeyaml获取的字段属性的排序依据Javabean的属性排序
 */
public class FieldOrderPropertyUtil extends PropertyUtils {
    @Override
    protected Set<Property> createPropertySet(Class<?> type, BeanAccess bAccess) {
        // 获取类的字段，这里是不包含父类的
        Field[] fields = type.getDeclaredFields();
        // 为了set遍历有序
        Set<Property> properties = new LinkedHashSet<>();
        // 从父类中获取集合，这里不重写父类这个方法，是因为无法重写
        Collection<Property> props = getPropertiesMap(type, bAccess).values();

        Map<String, Property> propertyMap = props.stream().collect(Collectors.toMap(Property::getName, Function.identity()));

        // 将此类的字段按照顺序放入set
        for (Field field : fields) {
            Property temp = propertyMap.get(field.getName());
            if (temp == null) {
                continue;
            }
            properties.add(temp);
            propertyMap.remove(field.getName());
        }
        for (Property property : propertyMap.values()) {
            if (property.isReadable()) {
                properties.add(property);
            }
        }
        return properties;
    }
}
