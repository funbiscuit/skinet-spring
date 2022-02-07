FROM nginx:1.20-alpine

## remove default nginx website
RUN rm -rf "/usr/share/nginx/html/*"

## copy images
COPY skinet-api/src/main/resources/static /usr/share/nginx/html

RUN ls -R

## copy artifacts from builder step
COPY client/dist/client /usr/share/nginx/html

## copy nginx configuration
COPY docker/nginx.conf /etc/nginx/conf.d/default.conf

CMD ["nginx", "-g", "daemon off;"]
