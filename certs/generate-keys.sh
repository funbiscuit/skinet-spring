#!/usr/bin/env bash
# certificates for nginx ssl
openssl dhparam -out dhparam.pem 2048
openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout server.key -out server.crt -subj "/C=US/CN=skinet"

# certificates for spring ssl connection
keytool -genkeypair -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore ssl.p12 -validity 365 -storepass password -dname "c=US, cn=skinet"

# stripe publishable key for angular client
echo "{\"publishableKey\":\"pk_test_YOUR_KEY\"}" > client/stripe.json
