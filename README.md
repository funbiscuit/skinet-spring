# Skinet

This code is based on repo https://github.com/TryCatchLearn/skinet for
.NET+Angular course Instead of .NET it uses Spring Boot. Angular part is the
same with minor tweaks. Otherwise, almost all functionality is identical.

docker-compose file contains necessary dependencies (PostgreSQL and Redis) to
run development locally.

# Run built images locally

To run locally you need to generate ssl certificates. For example:

```shell
mv certs
keytool -genkeypair -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore dev-ssl.p12 -validity 3650
openssl dhparam -out dhparam.pem 2048
openssl req -x509 -nodes -days 3650 -newkey rsa:2048 -keyout server.key -out server.crt
```

Next, optionally, you can configure Stripe keys:

1. Publishable key for ui should be placed as json in certs/stripe.json. For
   example:

```json
{
  "publishableKey": "YOUR_KEY"
}
```

2. Stripe keys for API are provided as environment variables in
   docker-compose-hub.yaml:

```
app.stripe.publishable-key: pk_key
app.stripe.secret-key: sk_key
app.stripe.webhook-key: whsec_key
```

When running locally, webhook key should be acquired from Stripe CLI:

```shell
stripe login
stripe.exe listen --forward-to https://localhost:8443/api/payments/webhook --skip-verify
```

To run app just use following command:

```shell
docker-compose up -f docker-compose-hub.yaml
```

Default user is `bob@gmail.com` with password `123`

If Stripe keys were not provided, app will send 500 responses when trying to pay
for the order.
