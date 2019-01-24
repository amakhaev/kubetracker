package com.tracker.domain.kube;

import com.tracker.domain.token.TokenModel;
import com.tracker.utils.ResourceHelper;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;

/**
 * Provides the implementation of {@link PodsService}
 */
@Slf4j
class PodsServiceImpl implements PodsService {

    private Config kubeClientConfig;

    /**
     * Initialize new instance of {@link PodsServiceImpl}
     */
    PodsServiceImpl(String namespace) {
        this.kubeClientConfig = new ConfigBuilder()
                .withMasterUrl(ResourceHelper.getProperty("master_url"))
                .withTrustCerts(true)
                .withNamespace(namespace)
                .build();
    }

    /**
     * Gets the pod list by given token
     *
     * @param tokenModel - the token to retrieve
     * @return the {@link PodList} instance
     */
    @Override
    public PodList getPods(TokenModel tokenModel) {
        if (tokenModel.getAccessToken() == null) {
            throw new NullPointerException("The access token is empty");
        }

        try {
            this.kubeClientConfig.setOauthToken(tokenModel.getAccessToken());
            final KubernetesClient client = new DefaultKubernetesClient(this.kubeClientConfig);
            return client.pods().list();
        } catch (Exception e) {
            log.error(e.getMessage());
            return new PodList();
        }
    }
}
