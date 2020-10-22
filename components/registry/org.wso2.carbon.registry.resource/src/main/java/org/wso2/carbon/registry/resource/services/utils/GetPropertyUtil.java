/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.wso2.carbon.registry.resource.services.utils;

import org.wso2.carbon.registry.core.session.UserRegistry;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.registry.resource.beans.PermissionEntry;
import org.wso2.carbon.registry.resource.beans.PropertiesBean;
import org.wso2.carbon.registry.resource.beans.Property;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

/**
 *
 */
public class GetPropertyUtil {

    private static final Log log = LogFactory.getLog(GetPropertyUtil.class);

    public static String getProperty(UserRegistry registry,
                                     String resourcePath, String key) throws RegistryException {

        try {
            if (registry.resourceExists(resourcePath)) {
                
                Resource resource = registry.get(resourcePath);
                if (resource != null) {
                    String value = resource.getProperty(key);
                    resource.discard();
                    return value;
                }
            }
            
        } catch (RegistryException e) {

            String msg = "Failed to get the resource information of resource " + resourcePath +
                    " for retrieving a property with key : " + key + ". Error :" +
                    ((e.getCause() instanceof SQLException) ?
                    "" : e.getMessage());
            log.error(msg, e);
            throw e;
        }

        return "";
    }

    public static PropertiesBean getProperties(UserRegistry registry, String resourcePath) throws RegistryException {

        try {
            if (registry.resourceExists(resourcePath)) {

                Resource resource = registry.get(resourcePath);
                if (resource != null) {
                    Properties props = resource.getProperties();
                    PropertiesBean propertiesBean = new PropertiesBean();

                    java.util.List<Property> p = new ArrayList<>();
                    props.forEach((k, v) -> {
                        Property item = new Property();
                        item.setKey(k.toString());
                        item.setValue(((ArrayList) v).get(0).toString());
                        p.add(item);
                    });
                    resource.discard();
                    propertiesBean.setProperties(p.toArray(new Property[p.size()]));
                    return propertiesBean;
                }
            }

        } catch (RegistryException e) {

            String msg = "Failed to get the resource information of resource " + resourcePath +
                    " for retrieving the properties . Error :" +
                    ((e.getCause() instanceof SQLException) ?
                            "" : e.getMessage());
            log.error(msg, e);
            throw e;
        }

        return null;
    }
}
