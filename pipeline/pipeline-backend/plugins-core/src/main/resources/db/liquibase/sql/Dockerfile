FROM mysql:8.0.32

COPY plugins.sql /docker-entrypoint-initdb.d/

ENV MYSQL_ROOT_PASSWORD=root

VOLUME /var/lib/mysql