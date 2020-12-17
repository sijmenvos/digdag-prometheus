# digdag-prometheus
[![](https://jitpack.io/v/sijmenvos/digdag-prometheus.svg?style=flat-square)](https://jitpack.io/#sijmenvos/digdag-prometheus)
[![](https://jitpack.io/v/sijmenvos/digdag-prometheus/month.svg?style=flat-square)](https://jitpack.io/#sijmenvos/digdag-prometheus)

## Description
digdag-prometheus is a plugin sending last run timestamps to Prometheus Pushgateway.

## Features

- Adds the `prometheus>` opetaror, you can set the metric name here
- Pushes last session unix timestamp with label `project_id`

## Requirement

- [Digdag](https://www.digdag.io/)
- Prometheus
  - pushgateway_url [(Pushgateway)](https://github.com/prometheus/pushgateway)

## Usage
Also, you can see expamle workflow at [sample](https://github.com/sijmenvos/digdag-prometheus/tree/master/sample) directory.

1. Create workflow file (e.g. prometheus.dig)

  ```yaml
  _export:
    plugin:
      repositories:
        - https://jitpack.io
      dependencies:
        - com.github.sijmenvos:digdag-prometheus:0.1.1
    pushgateway_url: "localhost:9091"

  +success:
    _check:
      prometheus>: "digdag_task_last_success"
    echo>: "Job completed successfully"

  +failure:
    _error:
      prometheus>: "digdag_task_last_failure"
    sh>: "exit 1"

  ```

2. Run workflow
  ```console
  $ digdag run -a prometheus.dig
  ```

## License

[Apache License 2.0](LICENSE)
