package io.digdag.plugin.prometheus;

import io.digdag.spi.OperatorFactory;
import io.digdag.spi.OperatorProvider;
import io.digdag.spi.Plugin;
import io.digdag.spi.TemplateEngine;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

public class PrometheusPlugin implements Plugin {
    @Override
    public <T> Class<? extends T> getServiceProvider(Class<T> type) {
        if (type == OperatorProvider.class) {
            return PrometheusOperatorProvider.class.asSubclass(type);
        } else {
            return null;
        }
    }

    public static class PrometheusOperatorProvider implements OperatorProvider {
        @Inject
        protected TemplateEngine templateEngine;

        @Override
        public List<OperatorFactory> get() {
            return Arrays.asList(new PrometheusOperatorFactory(templateEngine));
        }
    }
}
