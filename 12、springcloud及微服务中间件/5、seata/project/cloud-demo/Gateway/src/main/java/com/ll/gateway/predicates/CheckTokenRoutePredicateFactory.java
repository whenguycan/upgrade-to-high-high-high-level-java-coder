package com.ll.gateway.predicates;

import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.cloud.gateway.handler.predicate.GatewayPredicate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @Auther: tangwei
 * @Date: 2023/4/24 10:00 AM
 * @Description: 类描述信息
 */
@Component
public class CheckTokenRoutePredicateFactory extends AbstractRoutePredicateFactory<CheckTokenRoutePredicateFactory.Config> {

    public CheckTokenRoutePredicateFactory() {
        super(Config.class);
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        return new GatewayPredicate() {
            @Override
            public boolean test(ServerWebExchange serverWebExchange) {

                System.out.println("这儿开始判断断言，是否符合要求");

                System.out.println("字符串为：" + config.getCheckToken());

                URI uri = serverWebExchange.getRequest().getURI();
                return uri.getPath().startsWith(config.getCheckToken());
            }


        };
    }

    @Override
    public ShortcutType shortcutType() {
        return ShortcutType.DEFAULT;
    }

    @Override
    public List<String> shortcutFieldOrder() {
        ArrayList<String> list = new ArrayList<>();
        list.add("checkToken");
        return list;
    }

    public static class Config{
        public String checkToken;

        public String getCheckToken() {
            return checkToken;
        }

        public void setCheckToken(String checkToken) {
            this.checkToken = checkToken;
        }
    }
}


