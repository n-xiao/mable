# Developer Documentation - frontend

## Introduction
This guide aims to get you pointed in the right direction when working with stuff in
`frontend.*`. It details design objectives and reasoning for most design patterns found in the source code.
Note that not all aspects of the `frontend` code will be discussed here,
since most of it is lengthy but trivial, and is left as an exercise for the reader to
understand. A good tab to have open as you read through the following sections is
the [JavaFX 25 Documentation](https://openjfx.io/javadoc/25/index.html).

> [!TIP]
> Words in `monospace` are code-related, and can usually be found in the source code.\
> Words in *italics* are informal terms, and have been included for over-simplification.\
> Words in **bold** are of significant importance to the reader.
>

## Overview
An image illustrating a high-level frontend overview is shown below.

![frontend_overview_IMG](https://github.com/n-xiao/mable-artifacts/blob/main/media/dev-docs/annotated-frontend-overview.png)

Essentially, the frontend is made up of one `MainContainer` instance which, initialises, *contains*, and holds separate
references to one instance of `Sidebar` and one instance of `Content` on application launch. The specifics of *how*
a user interface entity *"contains"* other entities will be discussed later.

> [!NOTE]
>`Content` is **omitted** from the annotation as it is an entity with the sole purpose of *containing*\
>other UI entities in order to support "page-switching". It, alone, is never seen by the user.
>

Page-switching refers to the design intention of `Content` to control the visibility of *pages* (e.g `HomePage`).
A good analogy would be: switching between *tabs of a web browser*; only the page that is selected by the user is displayed.

Notice how the `Sidebar` and `HomePage` contains other components as well: the `Sidebar` *contains* a `SidebarStatsPane`
and a `SidebarFolderManager`, while the `HomePage` *contains* `CountdownPaneViewTitle` and `CountdownPaneView`.
Note that `CountdownPaneView` *contains* multiple `CountdownPane`s (3 in this example).

## Singleton Class Design Pattern
The [singleton class](https://www.geeksforgeeks.org/system-design/singleton-design-pattern/) is a
design pattern which has been used extensively throughout the code in `frontend`. This is because OOP
isn't very ideal when it comes to creating user interfaces. *You may disagree with this design choice. Oh no!*
Anyways, with singleton classes, we can **reference** the same instance of a class from all other classes
while remaining in the comfort of Java boilerplate-ness.

> [!IMPORTANT]
> This is where we clarify the difference between "**holding a reference to**" and "**contains**".
>
>
>
> - When I say **"`foo` contains `bar`"**, I am referring to how `foo` **is a JavaFX [Parent](https://openjfx.io/javadoc/25/javafx.graphics/javafx/scene/Parent.html#)**, where `foo.getChildren()` returns a list which **contains** `bar`.
>     - `foo` is the (JavaFX) parent component of `bar`
>     - `bar` is the (JavaFX) child component of `foo`
>
> - When I say **"`foo` references `bar`"**, I am referring to how `foo` has utilised an object reference to `bar`,
>but may not **contain** `bar`. With the singleton design pattern, this is often done through the use of a
>`Bar.getInstance()` static method which returns `bar`.
>

>[!WARNING]
> Do not mix up JavaFX "parent component" and "child component" terminology
> with OOP "parent class" and "child class" terminology.
>


### Code Example
Here is a code snippet from the singleton class `CountdownPaneViewTitle`:

```java
...

public class CountdownPaneViewTitle extends VBox {
    private static CountdownPaneViewTitle instance = null;

    private final Label LABEL;

    private CountdownPaneViewTitle() {
        this.setBackground(null);
        this.setFillWidth(true);
        this.LABEL = new Label();
        configureLabel();
        this.getChildren().addAll(this.LABEL, createHorizontalLine());
    }

    public static CountdownPaneViewTitle getInstance() {
        if (instance == null) {
            instance = new CountdownPaneViewTitle();
        }
        return instance;
    }
...
}
```

One advantage of using the singleton design pattern is that the instance of the class can still be a JavaFX component
(through inheritance); saying that "a `CountdownPaneViewTitle` **is a** `VBox`" is intuitive. [^1]
[^1]: This is why the singleton class is preferred over other design patterns, such as a utility or factory class.

> [!CAUTION]
> Use caution when calling **any** `getInstance()` methods within **any** class's constructor.
>
> - Calling the `getInstance()` method in the constructor of the same class will result in an infinite recursive loop
>ending in a `StackOverflowError` as `getInstance()` will never be able to return an incomplete object; `getInstance()`
>is called again (by the constructor) before the object can be constructed.
>
> * The above issue may seem trivial at first, but many UI components rely on their parents' properties
>when defining their own properties, such as component sizes. As a result, if a parent calls the `getInstance()` method
>of its child component's class while neither components have been constructed **and** if the child component's
>constructor also calls `getInstance()` on the parent (or vice versa), a deadlock situation arises where
>neither `getInstance()` methods complete as they both end up calling each other.
>
> * You can identify this issue and offending class(es) by looking at the output of the `StackOverflowError`.
> Multiple `getInstance()` calls would be printed in the stack trace.
>


<sub>Copyright © 2026 Nicholas Siow  <nxiao.dev@gmail.com> </sub>
