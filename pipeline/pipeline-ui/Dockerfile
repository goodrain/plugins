FROM node:16-alpine as BUILD

COPY . /app

WORKDIR /app

RUN sed -i 's/http:\/\/dl-cdn.alpinelinux.org/https:\/\/mirrors.aliyun.com/g' /etc/apk/repositories && \
    apk update && apk add --no-cache autoconf && \
    yarn config set registry https://registry.npmmirror.com && \
    yarn install && \
    yarn run build

FROM nginx:alpine

COPY --from=BUILD /app/dist /app
COPY default.conf /etc/nginx/conf.d/default.conf
