# Glady Backend challenge
This project is an implementation of the Glady backend challenge.

## Statements
Companies can use Wedoogift services to distribute:

* Gift deposits
* Meal deposits

### Gift deposits
Gift deposits has 365 days lifespan, beyond this period it will no longer be counted in the user's balance.
example:
John receives a Gift distribution with the amount of $100 euros from Tesla. he will therefore have $100 in gift cards in his account.
He received it on 06/15/2021. The gift distribution will expire on 06/14/2022.

### Meal deposits
Meal deposit works like the Gift deposit excepting for the end date. In fact meal deposits expires at the end of February of the year following the distribution date.
example:
Jessica receives a Meal distribution from Apple with the amount of $50 on 01/01/2020, the distribution ends on 02/28/2021.

Implement one or two functions allowing companies to distribute gift and meal deposits to a user if the company balance allows it.
Implement a function to calculate the user's balance.

## 🎬 Start the application

Installation, build and run tests :

```sh
mvn clean install
```

launch the application :

```sh
docker run axoniq/axonserver
mvn spring-boot:run "-Dspring-boot.run.profiles=dev"
```

## To do

* some tests
* end to end tests with test-containers
* some docs / schemas
* some logs and logback config
* the controllers if called by REST APIs
