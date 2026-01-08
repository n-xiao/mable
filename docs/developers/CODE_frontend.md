# Developer Documentation - frontend

## Introduction
This guide aims to get you pointed in the right direction when working with stuff in
`frontend.*`. Note that not all aspects of the `frontend` code will be discussed here,
since most of it is lengthy but trivial, and is left as an exercise for the reader to
understand. A good tab to have open as you read through the following sections is
the [JavaFX 25 Documentation](https://openjfx.io/javadoc/25/index.html).

## Overview
An image illustrating the frontend design is shown below.

![frontend_overview_IMG](https://github.com/n-xiao/mable-artifacts/blob/main/media/dev-docs/annotated-frontend-overview.png)

Essentially, the frontend has one `MainContainer` which, during runtime, contains a `Sidebar` and a `HomePage`.
The specifics of *how* a class, or instance of the class, "contains" other entities will be discussed later.
Notice how the `Sidebar` and `HomePage` contain other entities as well: the `Sidebar` contains a `SidebarStatsPane`
and a `SidebarFolderManager`, while the `HomePage` contains `CountdownPaneViewTitle` (indicated by the light blue dotted
box) and `CountdownPaneView`. Note that `CountdownPaneView` contains multiple `CountdownPane`s (3 in this example, where
only 1 of the 3 is annotated).

> [!NOTE]
> The `MainContainer` and `HomePage` are designed with "page-switching" in mind; it is intended that users
> would be able to switch between different pages (e.g a `SettingsPage`) and `HomePage`.
>
