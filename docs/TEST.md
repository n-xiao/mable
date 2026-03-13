# Unit Tests
## Introduction
Mable uses the following to implement unit tests:

- JUnit 6
- Mockito 5

See the [build.gradle](https://github.com/n-xiao/mable/blob/master/build.gradle) for exact versions.

### Things to take note of:
 - When writing tests, try to follow the recommendations of [this Baeldung article](https://www.baeldung.com/java-unit-testing-best-practices)
 - Also, for convenience's sake, try to use the `var` keyword for declarations.

## Frontend — Wait... Where are they?
Yeah... about that... Unfortunately, JavaFX does not seem to have a straightforward way of unit testing at the moment.
The only testing framework I could find was [TestFX](https://github.com/TestFX/TestFX), which looks abandoned to me.

So, for now, the most effective way to test UI things is to do it the old-fashioned way and do `./gradlew run`, then
test the application yourself, interacting with the UI. I would argue that this is better, because:

1. You can visually inspect the look-ism of your UI change(s).
2. Your UI change(s) can be observed with all other UI components, functioning together.
3. No need to write tests. More time to make silly mistakes. Yay!
4. Mable is a relatively small project. It's not like we're one of those open-source projects which supports
the entirety of the internet.

## Backend
This is basically grasping at straws, but I have created some tests for the `backend` classes. It's very minimal,
since the `backend` has way less code than the `frontend` of Mable (for obvious reasons).

If you do add `backend` functionality, please write appropriate tests. Thanks.

<sub>Copyright © 2026 Nicholas Siow  <nxiao.dev@gmail.com> </sub>
