FROM node:16.14.2 as builder

COPY . /app

WORKDIR /app

RUN yarn config set registry https://registry.npmmirror.com && \
    yarn install && \
    yarn run build

FROM nginx:alpine

COPY --from=builder /app/dist /app
COPY --from=builder /app/default.conf /etc/nginx/conf.d/default.conf