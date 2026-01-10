# Developer Documentation - frontend

## Introduction
This guide aims to get you pointed in the right direction when working with stuff in
`frontend.*`. Note that not all aspects of the `frontend` code will be discussed here,
since most of it is lengthy but trivial, and is left as an exercise for the reader to
understand. A good tab to have open as you read through the following sections is
the [JavaFX 25 Documentation](https://openjfx.io/javadoc/25/index.html).

> [!IMPORTANT]
> Words in *italics* are informal terms, and have been included for over-simplification.\
> Words in **bold** are of significant importance to the reader.\
> Words in `monospace` are code-related, and can usually be found in the source code.
>

## Overview
An image illustrating a high-level frontend overview is shown below.

![frontend_overview_IMG](https://github.com/n-xiao/mable-artifacts/blob/main/media/dev-docs/annotated-frontend-overview.png)

Essentially, the frontend is made up of one `MainContainer` instance which, initialises, *contains*, and holds separate
references to one instance of `Sidebar` and one instance of `Content` on application launch. The specifics of *how*
a user interface entity *"contains"* other entities will be discussed later.

> [!NOTE]
>`Content` is **omitted** from the annotation as it is an entity with a sole purpose of *containing*\
>other UI entities in order to support "page-switching". It, alone, is never seen by the user.
>

Page-switching refers to the design intention of `Content` to control the visibility of *pages* (e.g `HomePage`).
A good analogy would be: switching between *tabs of a web browser*; only the page that is selected by the user is displayed.

Notice how the `Sidebar` and `HomePage` contain other entities as well: the `Sidebar` *contains* a `SidebarStatsPane`
and a `SidebarFolderManager`, while the `HomePage` *contains* `CountdownPaneViewTitle` (indicated by the light blue dotted
box) and `CountdownPaneView`. Note that `CountdownPaneView` *contains* multiple `CountdownPane`s (3 in this example).

## Singleton Class Design Pattern
The singleton class is a design pattern which has been used extensively throughout the code in `frontend`. This is because OOP
isn't very ideal when it comes to creating user interfaces.
Geeks4Geeks has an excellent article about [singleton classes](https://www.geeksforgeeks.org/system-design/singleton-design-pattern/).

*You may disagree with this design choice. Oh no!*

Anyways, with singleton classes, we can **reference** the same instance of a class from all other classes while remaining in
the comfort of Java boilerplate-ness.

> [!NOTE]
> This is where we clarify the difference between "**holding a reference to**" and "**contains**".
>

When I say **"`foo` contains `bar`"**, I am referring to how `foo` **is a JavaFX [Parent](https://openjfx.io/javadoc/25/javafx.graphics/javafx/scene/Parent.html#)**, where its `getChildren()` method returns a list which **contains** `foo`.

When I say **"`foo` references `bar`"**, I am referring to how `foo` has accessed an object reference to `bar`,
but may not **contain** `bar`. With the singleton design pattern, this is often done through the use of a
`Bar.getInstance()` static method which returns `bar`.

When I say "I love my girlfriend", `NullPointerException` is thrown because "girlfriend" is `null`.
