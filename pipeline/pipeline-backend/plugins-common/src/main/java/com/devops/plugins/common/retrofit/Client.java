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

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import javax.net.ssl.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class Client {

    private final Retrofit retrofit;

    public Client(final String apiHost, final String tokenPrefix, final String token, final Boolean basic, final String username, final String password) {
        // Create an ssl socket factory with our all-trusting manager
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        if(basic != null) {
            if (!basic) {
                okHttpClientBuilder.addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .addHeader("Authorization", tokenPrefix == null ? token : tokenPrefix + token)
                            .addHeader("Accept", "application/json")
                            .build();
                    return chain.proceed(request);
                });
            } else if (basic) {
                String credential = Credentials.basic(username, password);
                okHttpClientBuilder.addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .addHeader("Authorization", credential)
                            .addHeader("Accept", "application/json")
                            .build();
                    return chain.proceed(request);
                });
            }
        }
        okHttpClientBuilder.sslSocketFactory(getSSLSocketFactory(), getX509TrustManager());
        okHttpClientBuilder.hostnameVerifier((requestedHost, remoteServerSession) -> {
            return requestedHost.equalsIgnoreCase(remoteServerSession.getPeerHost()); // Compliant
        });
        OkHttpClient okHttpClient = okHttpClientBuilder.build();

        this.retrofit = new Retrofit.Builder()
                .baseUrl(apiHost.endsWith("/") ? apiHost : apiHost + "/")
                .client(okHttpClient)
                .addConverterFactory(JacksonConverterFactory.create(configureObjectMapper()))
                .build();
    }

    //获取这个SSLSocketFactory
    public static SSLSocketFactory getSSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, getTrustManager(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //获取TrustManager
    private static TrustManager[] getTrustManager() {
        return new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }
        };
    }

    //获取HostnameVerifier
    public static HostnameVerifier getHostnameVerifier() {
        return (s, sslSession) -> true;
    }

    public static X509TrustManager getX509TrustManager() {
        X509TrustManager trustManager = null;
        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
            }
            trustManager = (X509TrustManager) trustManagers[0];
        } catch (Exception e) {
            e.printStackTrace();
        }

        return trustManager;
    }


    private ObjectMapper configureObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper;
    }

    public <T> T type(Class<T> service) {
        return retrofit.create(service);
    }

}
