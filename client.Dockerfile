FROM nginx:1.20-alpine

## remove default nginx website
RUN rm -rf "/usr/share/nginx/html/*"

## copy images
COPY skinet-api/src/main/resources/static /usr/share/nginx/html

## copy artifacts from builder step
COPY client/dist/client /usr/share/nginx/html

## copy nginx configuration and keys
COPY docker/nginx.conf /etc/nginx/conf.d/default.conf
COPY docker/nginx-ssl.key /etc/ssl/private/
COPY docker/nginx-ssl.crt /etc/ssl/certs/
COPY docker/dhparam.pem /etc/ssl/certs/

CMD ["nginx", "-g", "daemon off;"]
