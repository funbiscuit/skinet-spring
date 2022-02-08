### Build
FROM node:12-alpine as builder

RUN mkdir -p /ng-app/dist

WORKDIR /ng-app

## cache node_modules
COPY client/package.json client/package-lock.json ./
RUN npm install

## build  app
COPY client/. ./
RUN npm run ng build


### setup image
FROM nginx:1.20-alpine

## remove default nginx website
RUN rm -rf "/usr/share/nginx/html/*"

## copy artifacts from builder step
COPY --from=builder /ng-app/dist/client /usr/share/nginx/html
## copy images
COPY skinet-api/src/main/resources/static /usr/share/nginx/html

## copy nginx configuration
COPY docker/nginx.conf /etc/nginx/conf.d/default.conf

CMD ["nginx", "-g", "daemon off;"]
