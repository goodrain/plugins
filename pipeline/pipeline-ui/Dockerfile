FROM node:16.14.2 AS BUILD

COPY . /app

WORKDIR /app

RUN yarn config set registry https://registry.npmmirror.com && \
    yarn install && \
    yarn run build

FROM nginx:alpine

COPY --from=BUILD /app/dist /app
COPY default.conf /etc/nginx/conf.d/default.conf
