# Skinet

This code is based on repo https://github.com/TryCatchLearn/skinet for .NET+Angular course. Instead of .NET it uses
Spring Boot (MVC, Data, Security) with JWT auth and OpenAPI. Angular module is the same with minor tweaks (for example,
Stripe is optional). Functionality from user perspective is identical.

# Development

`docker-compose.yaml` contains necessary dependencies (PostgreSQL and Redis) to run development locally.

Client can be started with

```shell
ng serve
```

API with

```shell
gradlew :skinet-api:bootRun
```

# Run images of API and UI locally

To run locally you need to generate ssl certificates. For example:

```shell
mv certs
chmod +x generate-keys.sh
generate-keys.sh
```

Next, optionally, you can configure Stripe keys:

1. Publishable key for ui should be placed as json in certs/client/stripe.json. For example (test key is generated by
   the above script):

```json
{
  "publishableKey": "YOUR_KEY"
}
```

2. Stripe keys for API are provided as environment variables in
   docker-compose-hub.yaml:

```
app.stripe.publishable-key: pk_key_YOUR_KEY
app.stripe.secret-key: sk_key_YOUR_KEY
app.stripe.webhook-key: whsec_key_YOUR_KEY
```

When running locally, webhook key should be acquired from Stripe CLI:

```shell
stripe login
stripe listen --forward-to https://localhost:8443/api/payments/webhook --skip-verify
```

To run app just use following command:

```shell
docker-compose up -f docker-compose-hub.yaml
```

Default user is `bob@gmail.com` with password `123`

App can be tested without correct Stripe keys. For that you should use following values of secret and publishable keys:

```
app.stripe.publishable-key: pk_key_YOUR_KEY
app.stripe.secret-key: sk_key_YOUR_KEY
```

In that case order status will be decided based on shipping country:

| Country |      Status      |
|---------|------------------|
| USA     | Payment received |
| UK      | Payment failed   |
| Other   | Pending          |
