package io.digdag.plugin.prometheus;

import static java.nio.charset.StandardCharsets.UTF_8;
import io.digdag.client.config.Config;
import io.digdag.client.config.ConfigException;
import io.digdag.spi.Operator;
import io.digdag.spi.OperatorContext;
import io.digdag.spi.OperatorFactory;
import io.digdag.spi.TaskResult;
import io.digdag.spi.TemplateEngine;
import io.digdag.util.BaseOperator;

import java.io.IOException;
import java.nio.file.Files;

import com.google.common.base.Throwables;

import io.prometheus.client.exporter.PushGateway;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;

public class PrometheusOperatorFactory implements OperatorFactory {
    private final TemplateEngine templateEngine;

    public PrometheusOperatorFactory(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String getType() {
        return "prometheus";
    }

    @Override
    public Operator newOperator(OperatorContext context) {
        return new PrometheusOperator(context);
    }

    private class PrometheusOperator extends BaseOperator {
        public PrometheusOperator(OperatorContext context) {
            super(context);
        }

        @Override
        public TaskResult runTask() {
            Config params = request.getConfig().mergeDefault(
                request.getConfig().getNestedOrGetEmpty("prometheus"));

            if (!params.has("pushgateway_url")) {
                throw new ConfigException("'pushgateway_url' is required");
            }

            CollectorRegistry registry = new CollectorRegistry();
            PushGateway pg = new PushGateway(params.get("pushgateway_url", String.class));

            Counter digdag = Counter.build()
                .name("digdag_total").help("Total digdag tasks.")
                .labelNames("task")
                .register();

            digdag.labels(params.get("task_name", String.class)).inc();

            try {
                pg.pushAdd(registry, "digdag");
            } catch (IOException ex) {
                throw Throwables.propagate(ex);
            }

            return TaskResult.empty(request);
        }
    }

}
