## Antaeus

Antaeus (/ænˈtiːəs/), in Greek mythology, a giant of Libya, the son of the sea god Poseidon and the Earth goddess Gaia. He compelled all strangers who were passing through the country to wrestle with him. Whenever Antaeus touched the Earth (his mother), his strength was renewed, so that even if thrown to the ground, he was invincible. Heracles, in combat with him, discovered the source of his strength and, lifting him up from Earth, crushed him to death.

Welcome to our challenge.

## The challenge

As most "Software as a Service" (SaaS) companies, Pleo needs to charge a subscription fee every month. Our database contains a few invoices for the different markets in which we operate. Your task is to build the logic that will schedule payment of those invoices on the first of the month. While this may seem simple, there is space for some decisions to be taken and you will be expected to justify them.

## Instructions

Fork this repo with your solution. Ideally, we'd like to see your progression through commits, and don't forget to update the README.md to explain your thought process.

Please let us know how long the challenge takes you. We're not looking for how speedy or lengthy you are. It's just really to give us a clearer idea of what you've produced in the time you decided to take. Feel free to go as big or as small as you want.

## Developing

Requirements:
- \>= Java 11 environment

Open the project using your favorite text editor. If you are using IntelliJ, you can open the `build.gradle.kts` file and it is gonna setup the project in the IDE for you.

### Building

```
./gradlew build
```

### Running

There are 2 options for running Anteus. You either need libsqlite3 or docker. Docker is easier but requires some docker knowledge. We do recommend docker though.

*Running Natively*

Native java with sqlite (requires libsqlite3):

If you use homebrew on MacOS `brew install sqlite`.

```
./gradlew run
```

*Running through docker*

Install docker for your platform

```
docker build -t antaeus
docker run antaeus
```

### App Structure
The code given is structured as follows. Feel free however to modify the structure to fit your needs.
```
├── buildSrc
|  | gradle build scripts and project wide dependency declarations
|  └ src/main/kotlin/utils.kt 
|      Dependencies
|
├── pleo-antaeus-app
|       main() & initialization
|
├── pleo-antaeus-core
|       This is probably where you will introduce most of your new code.
|       Pay attention to the PaymentProvider and BillingService class.
|
├── pleo-antaeus-data
|       Module interfacing with the database. Contains the database 
|       models, mappings and access layer.
|
├── pleo-antaeus-models
|       Definition of the Internal and API models used throughout the
|       application.
|
└── pleo-antaeus-rest
        Entry point for HTTP REST API. This is where the routes are defined.
```

### Main Libraries and dependencies
* [Exposed](https://github.com/JetBrains/Exposed) - DSL for type-safe SQL
* [Javalin](https://javalin.io/) - Simple web framework (for REST)
* [kotlin-logging](https://github.com/MicroUtils/kotlin-logging) - Simple logging framework for Kotlin
* [JUnit 5](https://junit.org/junit5/) - Testing framework
* [Mockk](https://mockk.io/) - Mocking library
* [Sqlite3](https://sqlite.org/index.html) - Database storage engine

Happy hacking 😁!

################

Before I started, I mainly divided the project into couple tasks:
* Clone, build and run the project (Kotlin and Docker were pretty new for me, I needed some time to research and understand)
* Develop two new endpoints: fetch pending invoices and charge to do tasks with API requests. I mainly used Flask for the past three years, therefore felt strong developing new endpoints to solve the request as a first phase.
* Understand how "PaymentProvider" works & Why it is an interface but not a class & delete the overridden "charge" function and develop my own with exception handling.
* Write a test for currency mismatch, other exceptions implemented in the code.
* Decide on a scheduling mechanism that will either:
  * Get all the pending invoices on a frequency, turn them into jobs, check what day is it, and schedule them for the 1st of the coming month
  * Check which day is it, look for the first of the month, charge all invoices if it is the 1st of the month.
* Think about a solution on failed payments (retry, inform, etc.)

I've pretty much achieved all the steps in this plan and decided to go with the latter option on scheduling, mainly because of a single reason. It was very easy to implement. After researching for a time on schedules, threads and performance, I am pretty sure that initializing a schedule globally will create memory leaks and performance related issues. Considering that, I still have some pain points I want to tackle or improve:
* Left the REST routes that I've developed during the first phase without any iteration, simply they were to be used for testing.
* There is only a test for currency mismatch scenario but there can be more tests depending on how "charge" function works. 
* Didn't implement a scenario for network failure. There can be a retry mechanism to take the payment if network exception occurs.
* Added utils.kt to test folder under core, just to be able to use getPaymentProvider() func as a constructor. Again, it can be easily solved by making PaymentProvider interface a class, but I wasn't sure since PaymentProvider is an external service and will be used as an external service.
* A better way to schedule pending invoices to be charged. I still need some time to understand push functions into cronjobs and schedule them.



