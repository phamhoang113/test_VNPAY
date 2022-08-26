package com.vnpay.test.order.service.orderservice.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@Order(1)
public class LoggingFilter implements Filter {

    private final static Logger LOG = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        LOG.info("Initializing filter :{}", this);
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        LOG.info("Starting Transaction for req :{}", req.getRequestURI());
//        if(null==req.getHeader("x-auth-key")||req.getHeader("x-auth-key").isEmpty()){
//            throw new RuntimeException("Missing x-auth-key header");
//        }
//        else{
//            checkPartner(req.getHeader("x-auth-key"));
//        }
        chain.doFilter(request, response);
        LOG.info("Committing Transaction for req :{}", req.getRequestURI());
    }
    @Override
    public void destroy() {
        LOG.warn("Destructing filter :{}", this);
    }

    private void checkPartner(String key) throws RuntimeException{
        if(!key.equalsIgnoreCase("gateway")){
            throw new RuntimeException("x-auth-key error");
        }
    }
}