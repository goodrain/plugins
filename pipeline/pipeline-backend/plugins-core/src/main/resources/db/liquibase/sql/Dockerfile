FROM mysql:8

COPY plugins.sql /docker-entrypoint-initdb.d/

ENV MYSQL_ROOT_PASSWORD=root

VOLUME /var/lib/mysql