# SpringBoot Rest OO
This application example is to demonstrate using the benefits of springboot to manage the RESTful 
interface 
and still allow OO methods and a Hexagonal Architecture to be used for the domain model.

It is heavily documented so as you read through the code you will see why separations and decisions 
had been made.

Please report errors, or to see other blogs and content, go to my <a href="https://builtbetter.blogspot.com/">Built Better</a> blog.

Swagger is through <a href="https://springfox.github.io/springfox/docs/current/">SpringFox</a>. Though it has not been set up yet.

When it is, run 

    ./gradlew allDocs

or

    .\gradlew.bat allDocs

## So What's Wrong With Spring?
Spring, and Springboot, are very intrusive application frameworks. While Spring had more flexibility, 
Springboot, as an opinionated framework, encourages a particular structure to the application.

Spring, and even more so, Springboot, has distinct advantages. For applications with light to moderate 
complexity these frameworks can allow you to whip out functional applications quickly and easily. 
However, there is an often-startling complexity in the framework that can be revealed when you 
deviate from the standard.
 
At moderate to higher complexities the inability to build true object-oriented application architecture reveals 
itself. I say "true" because these frameworks claim to enable OO, and often use the language from OO 
in examples, but the designs they encourage are definitely not. In this example, we cannot use any of the 
Entity and Persistence framework components for that reason.

## Information Hiding (or not)
The big missing piece that causes problems when there is complexity is well known in software engineering, 
not just Spring and Java: information hiding helps make the complicated easier to reason about. 
The usual practice in using Spring is to make everything,
all the data and all the methods, public. Spring has improved so you are not required to do this
any more, but it is still the most usual implementation.

And why not? Like many scripting languages, and many competing frameworks, if you are doing something
relatively simple, information hiding is annoying. If all the logic is laid out in one place, 
and you have everything that you need to know in your head, simply write a script. Very practical.

OO methods are for systems that are complicated enough that information hiding is helpful, where 
the methods and designs become an elegant way of separating functionality so that each piece is simple, 
testable, and reasonably bug free. OO trades quickness for architectural restrictions, 
simplicity, predictability, maintainability, and quality. YMMV.