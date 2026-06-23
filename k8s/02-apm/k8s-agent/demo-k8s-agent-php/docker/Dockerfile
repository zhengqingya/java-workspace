# 使用 PHP-FPM 运行环境，生产部署由 Nginx 通过 FastCGI 转发到 PHP-FPM。
FROM swr.cn-north-4.myhuaweicloud.com/ddn-k8s/docker.io/php:8.3-fpm

COPY --from=composer:2 /usr/bin/composer /usr/bin/composer

ENV DEBIAN_FRONTEND=noninteractive
ENV RUSTUP_HOME=/usr/local/rustup
ENV CARGO_HOME=/usr/local/cargo
ENV PATH=/usr/local/cargo/bin:${PATH}
ENV SW_AGENT_NAME=demo-k8s-agent-php
ENV SW_AGENT_COLLECTOR_BACKEND_SERVICES=skywalking-oap.skywalking.svc.cluster.local:11800
ENV SW_AGENT_PSR_LOGGING_LEVEL=Info

# SkyWalking PHP Agent 使用 PECL 安装，编译阶段需要 Rust/clang/protobuf 等依赖。
RUN apt-get update \
    && apt-get install -y --no-install-recommends \
        autoconf \
        build-essential \
        ca-certificates \
        clang \
        curl \
        unzip \
        libclang-dev \
        make \
        pkg-config \
        protobuf-c-compiler \
        protobuf-compiler \
    && rm -rf /var/lib/apt/lists/*

RUN curl --proto '=https' --tlsv1.2 -sSf https://sh.rustup.rs | sh -s -- -y --default-toolchain stable \
    && pecl install skywalking_agent \
    && docker-php-ext-enable skywalking_agent

WORKDIR /var/www/html

COPY composer.json /var/www/html/composer.json
RUN composer install --no-dev --optimize-autoloader --no-interaction --no-progress

COPY public /var/www/html/public
COPY php/skywalking.ini /usr/local/etc/php/conf.d/skywalking.ini
COPY php/zz-demo-fpm.conf /usr/local/etc/php-fpm.d/zz-demo-fpm.conf

EXPOSE 9000

CMD ["php-fpm", "-F"]
