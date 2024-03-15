# SRGFloytTask

Floyt task

* Kotlin used for all code.
* Clean Architecture structure used.

## Project Modules

### domain

* Business logic definitions, data model, abstract definition of repositories, and use cases.

### data

* Implementation of the abstract domain layer.
* Repositories and data sources, network apis, any database definition and DAOs, mappers, etc.

### app

* Dependency injections, main Android definitions.

### presentation

* Main activity and fragments of the app, along with their viewModels.
* Common utilities and helpers that the app may need.
* Also the module where UI assets are kept - layouts, drawables, strings, etc.

## Main libraries/jetpack/android features used

* Hilt for dependency injection
* Retrofit and Gson for network calling
* Room for persisting data after closing it
* Material3 to keep up with default android looks
* Shimmer to show while the network call is loading
* JUnit, Mockito and Turbine for unit testing

## Explanation of the app

As per the requirements upon starting the app there is an initial call to retrieve the products, the
user can then select which ones they want, check items in their basket and finally do the checkout.

### MainActivity

It features a custom toolbar so we can have more control over screen titles and navigation. Here is
defined a bottom navigation menu instead of the proposed solution to give it more scalability.
Here is also set the callback action for when the back button is pressed in the whole app.

### HomeFragment

The main fragment that loads upon opening the app. It makes a network call to retrieve the products
as well as loading previous ones if present, which are presented with all the required fields. The
user can select how many of each items they want, also being able to reduce the amount to 0 if
desired.

### BasketFragment

The user can check the products they selected, modify them and delete them if they wish too. They
will be able to continue to the checkout from here only if they have at least one item. An empty
state view is displayed if no product has been added.

### CheckoutFragment

Some text boxes are displayed for the user to fill with their information and when everything is
filled and correct they can proceed to make the (fake) payment, though not all of the fields have
been done but there are some defined to see the effect. The user can also click on the back arrow if
they want to cancel this process.

### PaymentConfirmationFragment

A simple view to show the user that their purchase has been completed, closing or clicking on the
button will take the user back to the `HomeFragment` and empty their basket.

## Other considerations

* Coroutines have been used to make network and database calls.
* Layouts done in XML instead of Composable due to being more familiar with them.
* There isn't a test for every class but some examples are present.
* Not all fields in the checkout screen are defined but some are present.
* Taken in consideration the possibility of process of death for the home screen so the network call
  is not done again.
* The previous mentioned test classes, fields in checkout screen, process of death handling are cut
  short due to time constraints.
* Logging, Firebase, Detekt, etc. would be nice to haves but also cut short due to time constraints.
