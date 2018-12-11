package com.tracker.domain.token;

import com.tracker.domain.settings.SettingModel;
import com.tracker.domain.settings.SettingService;
import com.tracker.utils.GsonUtility;
import com.tracker.utils.ResourceHelper;
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

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides the implementation of {@link TokenService}
 */
@Slf4j
public class TokenServiceImpl implements TokenService {

    private TokenModel tokenModel;
    private LocalTime tokenLastUpdate;

    /**
     * Gets the token model
     */
    @Override
    public TokenModel getToken() {
        SettingModel settingModel = SettingService.INSTANCE.getSettings();

        return this.isNeedToRefresh() ?
                this.makeTokenRequest(
                        ResourceHelper.getProperty("auth_url"),
                        settingModel.getFullName(),
                        settingModel.getPassword()
                ) :
                this.tokenModel;
    }

    private TokenModel makeTokenRequest(String url, String userName, String password) {

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            log.debug("Try to retrieve token");

            HttpPost request = new HttpPost(url);

            List<NameValuePair> postParameters = new ArrayList<>();
            postParameters.add(new BasicNameValuePair("grant_type", "password"));
            postParameters.add(new BasicNameValuePair("client_id", ResourceHelper.getProperty("client_id")));
            postParameters.add(new BasicNameValuePair("client_secret", ResourceHelper.getProperty("client_secret")));
            postParameters.add(new BasicNameValuePair("username", userName));
            postParameters.add(new BasicNameValuePair("password", password));
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
            return this.tokenModel;

        } catch (IOException ex) {
            log.error(ex.getMessage());
            return null;
        }
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
