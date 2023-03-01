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

import org.yaml.snakeyaml.constructor.Constructor;

public class SkipJobsConstructor extends Constructor  {


//    protected Object constructJavaBean2ndStep(MappingNode node, Object object) {
//        flattenMapping(node);
//        Class<? extends Object> beanType = node.getType();
//        List<NodeTuple> nodeValue = node.getValue();
//        for (NodeTuple tuple : nodeValue) {
//            ScalarNode keyNode;
//            if (tuple.getKeyNode() instanceof ScalarNode) {
//                // key must be scalar
//                keyNode = (ScalarNode) tuple.getKeyNode();
//            } else {
//                throw new YAMLException(
//                        "Keys must be scalars but found: " + tuple.getKeyNode());
//            }
//            Node valueNode = tuple.getValueNode();
//            // keys can only be Strings
//            keyNode.setType(String.class);
//            String key = (String) constructObject(keyNode);
//            try {
//                TypeDescription memberDescription = typeDefinitions.get(beanType);
//                Property property = memberDescription == null ? getProperty(beanType, key)
//                        : memberDescription.getProperty(key);
//
//                if (!property.isWritable()) {
//                    throw new YAMLException("No writable property '" + key + "' on class: "
//                            + beanType.getName());
//                }
//
//                valueNode.setType(property.getType());
//                final boolean typeDetected = (memberDescription != null)
//                        ? memberDescription.setupPropertyType(key, valueNode)
//                        : false;
//                if (!typeDetected && valueNode.getNodeId() != NodeId.scalar) {
//                    // only if there is no explicit TypeDescription
//                    Class<?>[] arguments = property.getActualTypeArguments();
//                    if (arguments != null && arguments.length > 0) {
//                        // type safe (generic) collection may contain the
//                        // proper class
//                        if (valueNode.getNodeId() == NodeId.sequence) {
//                            Class<?> t = arguments[0];
//                            SequenceNode snode = (SequenceNode) valueNode;
//                            snode.setListType(t);
//                        } else if (Set.class.isAssignableFrom(valueNode.getType())) {
//                            Class<?> t = arguments[0];
//                            MappingNode mnode = (MappingNode) valueNode;
//                            mnode.setOnlyKeyType(t);
//                            mnode.setUseClassConstructor(true);
//                        } else if (Map.class.isAssignableFrom(valueNode.getType())) {
//                            Class<?> keyType = arguments[0];
//                            Class<?> valueType = arguments[1];
//                            MappingNode mnode = (MappingNode) valueNode;
//                            mnode.setTypes(keyType, valueType);
//                            mnode.setUseClassConstructor(true);
//                        }
//                    }
//                }
//
//                Object value = (memberDescription != null)
//                        ? newInstance(memberDescription, key, valueNode)
//                        : constructObject(valueNode);
//                // Correct when the property expects float but double was
//                // constructed
//                if (property.getType() == Float.TYPE || property.getType() == Float.class) {
//                    if (value instanceof Double) {
//                        value = ((Double) value).floatValue();
//                    }
//                }
//                // Correct when the property a String but the value is binary
//                if (property.getType() == String.class && Tag.BINARY.equals(valueNode.getTag())
//                        && value instanceof byte[]) {
//                    value = new String((byte[]) value);
//                }
//
//                if (memberDescription == null
//                        || !memberDescription.setProperty(object, key, value)) {
//                    property.set(object, value);
//                }
//            } catch (DuplicateKeyException e) {
//                throw e;
//            } catch (Exception e) {
//                throw new BusinessRuntimeException(ABizCode.FAIL,
//                        "Cannot create property=" + key + " for JavaBean=" + object, e);
//            }
//        }
//        return object;
//    }

//    private Object newInstance(TypeDescription memberDescription, String propertyName,
//                               Node node) {
//        Object newInstance = memberDescription.newInstance(propertyName, node);
//        if (newInstance != null) {
//            constructedObjects.put(node, newInstance);
//            return constructObjectNoCheck(node);
//        }
//        return constructObject(node);
//    }

}
