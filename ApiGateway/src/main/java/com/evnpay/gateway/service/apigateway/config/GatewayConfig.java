package com.evnpay.gateway.service.apigateway.config;

import com.evnpay.gateway.service.apigateway.filter.AuthFilter;
import com.evnpay.gateway.service.apigateway.filter.MyFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableHystrix
public class GatewayConfig {


    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder, MyFilter myFilter, AuthFilter authFilter) {
        return builder.routes()
                .route("order-service", r -> r.path("/api/order/**").
                        filters(f -> f.filter(authFilter.apply(new AuthFilter.Config()))
                                .filter(myFilter.apply(new MyFilter.Config("Order Service", true, true)))
                        ).
                        uri("lb://order-service"))
                .route("book-service", r -> r.path("/api/book/**").
                        filters(f -> f.filter(authFilter.apply(new AuthFilter.Config()))
                                .filter(myFilter.apply(new MyFilter.Config("Book Service", true, true)))
                        ).
                        uri("lb://book-service"))
                .route("auth-service", r -> r.path("/api/auth/**").
                        filters(f -> f.filter(myFilter.apply(new MyFilter.Config("Auth Service", true, true)))
                        ).
                        uri("lb://auth-service"))
                .build();
    }

}
