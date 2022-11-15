package com.shop4me.core.domain.model.request;

import com.shop4me.core.domain.port.web.client.WebClientFabricationPlan;
import lombok.Getter;
import org.springframework.http.codec.FormHttpMessageReader;
import org.springframework.web.reactive.function.client.WebClient;

@Getter
public class WebClientFabricationPlanImpl implements WebClientFabricationPlan {

    private final String component;

    private final String url;

    private final String username;

    private final String password;

    private final WebClient baseProduct;

    public WebClientFabricationPlanImpl(String component, String url, String username, String password) {
        this.component = component;
        this.url = url;
        this.username = username;
        this.password = password;
        this.baseProduct = createBaseClient();
    }

    @Override
    public String toString() {
        return "WebClientFabricationPlan{" +
                "component=" + component +
                ", url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", password= '" +getPasswordEcho()+ '\'' +
                '}';
    }

    private String getPasswordEcho(){
        var passwordEcho = new StringBuilder();
        passwordEcho.append("*".repeat(password.length()));
        return passwordEcho.toString();
    }

    private WebClient createBaseClient(){
        return WebClient.builder()
                .codecs(configurer -> {
                    configurer.registerDefaults(true);
                    configurer.customCodecs().register(new FormHttpMessageReader());
                })
                .baseUrl(url)
                .build();
    }

}
