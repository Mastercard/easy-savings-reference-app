package com.mastercard.easysavingstutorialapp.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.mastercard.developer.utils.AuthenticationUtils;
import com.mastercard.easysavingstutorialapp.models.LocationsModel;
import org.openapitools.client.ApiException;
import com.mastercard.developer.interceptors.OkHttpOAuth1Interceptor;
import org.openapitools.client.ApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

@Service
public class MastercardService {
    @Value("${mastercard.p12.path}")
    private String p12Path;

    @Value("${mastercard.keystore.alias}")
    private String keyAlias;

    @Value("${mastercard.keystore.pass}")
    private String keyPass;

    @Value("${mastercard.consumer.key}")
    private String consumerKey;

    private String basepath = "https://sandbox.api.mastercard.com/eop/offer/v1";

    public ApiClient signRequest() {
        ApiClient client = new ApiClient();
        client.setBasePath(basepath);
        client.setHttpClient(
                client.getHttpClient()
                        .newBuilder()
                        .addInterceptor(new OkHttpOAuth1Interceptor(consumerKey, getSigningKey()))
                        .build()
        );

        return client;
    }

    private PrivateKey getSigningKey() {
        PrivateKey signingKey = null;
        try {
            signingKey = AuthenticationUtils.loadSigningKey(p12Path, keyAlias, keyPass);
        } catch (IOException | NoSuchProviderException | KeyStoreException | NoSuchAlgorithmException | CertificateException | UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        return signingKey;
    }

    public void getErrorAttributes(Exception e, RedirectAttributes attr) {
        attr.addFlashAttribute("error", e.getMessage());
        JsonParser parser = new JsonParser();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement el = parser.parse(((ApiException) e).getResponseBody());
        attr.addFlashAttribute("response", gson.toJson(el));
    }
}
