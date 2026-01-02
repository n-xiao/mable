# Mable
> [!IMPORTANT]
> Mable is still in an unreleased state, and is only suitable for use by developers.
>

## About
Mable is a simple, free and open source deadline manager for Windows and MacOS.
Mable is built to make the management of many ongoing, concurrent deadlines easier by allowing you to see the
number of days that remain for each deadline in a sorted layout. This makes
it easy for you to see which deadline is most (or least) urgent.
Mable is partially inspired by the [Bears Countdown App](https://apps.apple.com/us/app/bears-countdown/id1536711520)
and the visual aesthetics of [Excalidraw](https://github.com/excalidraw/excalidraw).

## Installation Guide
For developers,
 - Clone the repository and run `./gradlew build` followed by `./gradlew run` in the project root directory.
 - Mable will store all data in JSON format in the folder `mable_data`,
   located within your `HOME` directory aka Java's `System.getProperty("user.home")`.
   Please ensure that your development environment is configured for read and write operations.


> [!WARNING]
> The current version of Mable does not come with an update manager and **will not automatically update**.\
> You are encouraged to check this repository periodically for any updates.
>

## Contributing
// TODO

## Acknowledgements
- Mable uses [JavaFX](https://github.com/openjdk/jfx/tree/master) as a Gradle dependency. You can find a copy of their
License [here](https://github.com/openjdk/jfx/blob/master/LICENSE).
- Mable uses [Jackson](https://github.com/FasterXML/jackson) as a Gradle dependency. You can find a copy of their
License [here](https://github.com/FasterXML/jackson-core/blob/3.x/LICENSE).

## License
This file is part of Mable.

Mable is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

Mable is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with Mable. If not, see <https://www.gnu.org/licenses/>.

<sub>Copyright © 2026 Nicholas Siow  <nxiao.dev@protonmail.com> </sub>
