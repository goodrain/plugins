FROM mysql:5.7

COPY plugins.sql /docker-entrypoint-initdb.d/

ENV MYSQL_ROOT_PASSWORD=root

VOLUME /var/lib/mysql