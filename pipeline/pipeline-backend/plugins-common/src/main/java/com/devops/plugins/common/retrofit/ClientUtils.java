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

package com.devops.plugins.common.retrofit;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class ClientUtils {

    private static final ConcurrentMap<String, ClientUtils> UTILS_MAP = new ConcurrentHashMap<>();

    private final Client client;

    private ClientUtils(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    public static ClientUtils client(final String apiHost, final  String tokenPrefix,  final String token) {
        if (isBlank(apiHost) || isBlank(token)) {
            return null;
        }
        String key = getMD5InHex(apiHost + token);
        if (UTILS_MAP.containsKey(key)) {
            return UTILS_MAP.get(key);
        }
        ClientUtils clientUtil = new ClientUtils(new Client(apiHost,  tokenPrefix, token, false, null, null));
        UTILS_MAP.put(key, clientUtil);
        return clientUtil;
    }

    public static ClientUtils basicClient(final String apiHost, final String username, final String password) {
        if (isBlank(apiHost) || isBlank(username) || isBlank(password)) {
            return null;
        }
        String key = getMD5InHex(apiHost + username + password);
        if (UTILS_MAP.containsKey(key)) {
            return UTILS_MAP.get(key);
        }
        ClientUtils clientUtil = new ClientUtils(new Client(apiHost,  null, null, true, username, password));
        UTILS_MAP.put(key, clientUtil);
        return clientUtil;
    }

    public static ClientUtils emptyClient(final String apiHost) {
        if (isBlank(apiHost)) {
            return null;
        }
        ClientUtils clientUtil = new ClientUtils(new Client(apiHost,  null, null, null, null, null));
        return clientUtil;
    }

    private static boolean isBlank(String content) {
        if (content == null) {
            return true;
        }
        return content.trim().length() <= 0;
    }

    private static String getMD5InHex(String data) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(data.getBytes());
            byte[] result = messageDigest.digest();
            return new BigInteger(1, result).toString(16);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
