package com.evnpay.gateway.service.apigateway.filter;

import com.evnpay.gateway.service.apigateway.dto.UserDto;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    private final WebClient.Builder webClientBuilder;

    public AuthFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }


    private final static String BEARER_TYPE = "Bearer";

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String token = "";
            try{
                String authToken = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                String[] partOfAuthToken = authToken.split(" ");
                if(partOfAuthToken.length!=2||!BEARER_TYPE.equals(partOfAuthToken[0])){
                    token = "98";
                }
                else{
                    token = partOfAuthToken[1];
                }
            }
            catch (Exception e){
                token = "99";
            }
            return webClientBuilder.build()
                    .post()
                    .uri("http://auth-service/api/auth/validate_token?token="+token)
                    .retrieve().bodyToMono(UserDto.class)
                    .map(userDto -> {
                        logger.info("response:{}",userDto);
                        ServerHttpRequest.Builder request = exchange.getRequest()
                                .mutate();
                        request.header("x-user-permission", userDto.getPermission().toString());
                        request.header("x-user-username", userDto.getUserName());
                        request.header("x-user-id",userDto.getUserId());
                        return exchange;
                    })
                    .flatMap(chain::filter);
        };
    }

    @NoArgsConstructor
    public static class Config {
    }
}
