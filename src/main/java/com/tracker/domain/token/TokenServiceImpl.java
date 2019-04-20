package com.tracker.domain.token;

import com.tracker.domain.settings.SettingService;
import com.tracker.domain.settings.SettingsModel;
import com.tracker.utils.GsonUtility;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides the implementation of {@link TokenService}
 */
@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    private final String authUrl;
    private final String kubeClientId;
    private final String kubeClientSecret;
    private final SettingService settingService;

    /**
     * Initialize new instance of {@link TokenServiceImpl}
     */
    @Autowired
    public TokenServiceImpl(@Value("${auth.url}") String authUrl,
                            @Value("${kube.client_id}") String kubeClientId,
                            @Value("${kube.client_secret}") String kubeClientSecret,
                            SettingService settingService) {
        this.authUrl = authUrl;
        this.kubeClientId = kubeClientId;
        this.kubeClientSecret = kubeClientSecret;
        this.settingService = settingService;
        this.getToken();
    }

    /**
     * Gets the token model
     */
    @Override
    public synchronized TokenModel getToken() {
        SettingsModel settingsModel = this.settingService.getSettings();
        return this.makeTokenRequest(settingsModel);
    }

    private TokenModel makeTokenRequest(SettingsModel setting) {

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            log.debug("Try to retrieve token");

            HttpPost request = new HttpPost(this.authUrl);

            List<NameValuePair> postParameters = new ArrayList<>();
            postParameters.add(new BasicNameValuePair("grant_type", "password"));
            postParameters.add(new BasicNameValuePair("client_id", this.kubeClientId));
            postParameters.add(new BasicNameValuePair("client_secret", this.kubeClientSecret));
            postParameters.add(new BasicNameValuePair("username", setting.getKubernetesName()));
            postParameters.add(new BasicNameValuePair("password", setting.getPassword()));
            postParameters.add(new BasicNameValuePair("scope", "openid"));
            postParameters.add(new BasicNameValuePair("response_type", "id_token"));

            request.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));

            HttpResponse result = httpClient.execute(request);
            String json = EntityUtils.toString(result.getEntity(), "UTF-8");

            if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                log.info("Token retrieved successfully");
            } else {
                log.error("Token retrieved with error. Response: {}", json);
            }
            return GsonUtility.parse(json, TokenModel.class);

        } catch (IOException ex) {
            log.error(ex.getMessage());
            return null;
        }
    }
}
