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
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
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
    private TokenModel tokenModel;
    private LocalTime tokenLastUpdate;
    private LocalTime tokenCreatedAt;

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

        if (this.tokenModel == null || this.isNeedToReLogin()) {
            return this.makeTokenRequest(settingsModel);
        } else if (this.isNeedToRefresh()) {
            return this.makeRefreshTokenRequest(settingsModel, this.tokenModel.getRefreshToken());
        } else {
            return this.tokenModel;
        }
    }

    private TokenModel makeTokenRequest(SettingsModel setting) {

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            log.debug("Try to retrieve token");

            HttpPost request = new HttpPost(this.authUrl);

            List<NameValuePair> postParameters = new ArrayList<>();
            postParameters.add(new BasicNameValuePair("grant_type", "password"));
            postParameters.add(new BasicNameValuePair("client_id", this.kubeClientId));
            postParameters.add(new BasicNameValuePair("client_secret", this.kubeClientSecret));
            postParameters.add(new BasicNameValuePair("username", setting.getFullName()));
            postParameters.add(new BasicNameValuePair("password", setting.getPassword()));
            postParameters.add(new BasicNameValuePair("scope", "openid"));
            postParameters.add(new BasicNameValuePair("response_type", "id_token"));

            request.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));

            HttpResponse result = httpClient.execute(request);
            String json = EntityUtils.toString(result.getEntity(), "UTF-8");

            if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                log.info("Token retrieved successfully");
            } else {
                log.error("Token retrieved with error. Response: ");
                log.error(json);
            }
            this.tokenModel = GsonUtility.parse(json, TokenModel.class);
            this.tokenLastUpdate = LocalTime.now();
            this.tokenCreatedAt = LocalTime.now();
            return this.tokenModel;

        } catch (IOException ex) {
            log.error(ex.getMessage());
            return null;
        }
    }

    private TokenModel makeRefreshTokenRequest(SettingsModel setting, String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            return this.makeTokenRequest(setting);
        }

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            log.debug("Try to refresh token");

            HttpPost request = new HttpPost(this.authUrl);

            List<NameValuePair> postParameters = new ArrayList<>();
            postParameters.add(new BasicNameValuePair("grant_type", "refresh_token"));
            postParameters.add(new BasicNameValuePair("client_id", this.kubeClientId));
            postParameters.add(new BasicNameValuePair("client_secret", this.kubeClientSecret));
            postParameters.add(new BasicNameValuePair("scope", "openid"));
            postParameters.add(new BasicNameValuePair("refresh_token", refreshToken));

            request.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));

            HttpResponse result = httpClient.execute(request);
            String json = EntityUtils.toString(result.getEntity(), "UTF-8");

            if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                log.info("Token refreshed successfully");
            } else if (result.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
                log.debug("Can't refresh token. Try to re-login");
                return this.makeTokenRequest(setting);
            } else {
                log.error("Token refreshed with error. Response: ");
                log.error(json);
            }
            this.tokenModel = GsonUtility.parse(json, TokenModel.class);
            this.tokenLastUpdate = LocalTime.now();
            return this.tokenModel;

        } catch (IOException ex) {
            log.error(ex.getMessage());
            return null;
        }
    }

    private boolean isNeedToReLogin() {
        return this.tokenCreatedAt == null ||
                this.tokenModel == null ||
                ChronoUnit.HOURS.between(LocalTime.now(), this.tokenCreatedAt) >= 3;

    }

    private boolean isNeedToRefresh() {
        if (this.tokenLastUpdate == null || this.tokenModel == null) {
            return true;
        }

        int expiredTimeInSecond;
        try {
            expiredTimeInSecond = Integer.parseInt(this.tokenModel.getExpiresIn());
        } catch (NumberFormatException e) {
            log.error("Can't parse token expiration time as integer. Current value: " + this.tokenModel.getExpiresIn());
            return true;
        }

        return ChronoUnit.SECONDS.between(this.tokenLastUpdate, LocalTime.now()) >= expiredTimeInSecond - 5;
    }
}
