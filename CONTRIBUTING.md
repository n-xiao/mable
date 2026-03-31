<h3 align="center">
  Contributing to Mable
</h3>

Thank you for your interest in contributing to Mable. Please make sure the changes you wish to make are in line with the project direction. If you wish to make major changes, or if you are unsure about how your changes may affect Mable, open an issue first; we can discuss it there.

If you are new, start with something small to get familiar with the project and its development process. While innovation is encouraged, trying to add large components without familiarity with the project can cause disruptions to other contributors' ongoing work.

## Creating Issues
Before creating or contributing to an issue, make sure it has met the following criteria:
- One issue per bug. Putting multiple bugs in a single issue can cause confusion and make it difficult to fix.
- Only issues on supported operating systems (MacOS Sequoia 15.7.4 or later and Windows 11) are allowed.
- No build issues should be created. Please work it out locally. Everyone has differing development environments, and will likely not be able to replicate issues you face.
- Keep your contributions constructive. Jokes or other irrelevant commentary only serve to distract people from the conversation.

## Project governance
Currently, Mable is governed solely by [Nicholas Siow (@n-xiao)](https://github.com/n-xiao).
All pull requests must undergo a review.

## Communication Policy
The following guidelines apply to all user-facing strings, code, comments, and commit messages:
- The official language of this project is British English.
- Dates should be formatted in ISO 8601 (YYYY-MM-DD).
- Use metric units.
- Use proper spelling, grammar and punctuation.
- Avoid contractions, slang and idioms.
- Avoid humour, sarcasm, and other forms of non-literal language.
- Use gender-neutral pronouns, except when talking about a specific person.

Note that this also applies to debug logging, and other internal strings, as they may be exposed to users in the future.

## Testing policy
At the time of writing, tests involving JavaFX is minimal as there isn't a suitable library to facilitate said tests. If you are making changes to the user interface or user experience, a screen recording (or screen recordings) showcasing the difference made (before changes vs. after changes) will suffice. Use on-screen annotations or record a voiceover if needed.

You should still create tests for backend functionality with the provided test libraries where possible.

## Code submission policy

**Do:**
- Write in Java 25 code
- Do not format your code in ways that differ greatly from the current code format. (e.g variable names should not start with a special character like "gFoobar" instead of "foobar"). The current code is formatted with `clang-format` with arguments: `BasedOnStyle: Google, IdentWidth: 4, PenaltyBreakComment: 0`.
- Split your changes into separate, atomic commits (i.e. A commit per feature or fix, where the build, tests and the system are all functioning).
- Wrap your commit messages at 72 characters.
- Pull requests should have a title that starts with an appropriate semantic prefix:
  - **feat:** A new feature
  - **fix:** A bug fix
  - **docs:** Documentation only changes
  - **style:** Changes that do not affect the meaning of the code (white-space, formatting, missing semi-colons, etc)
  - **refactor:** A code change that neither fixes a bug nor adds a feature
  - **perf:** A code change that improves performance
  - **test:** Adding missing tests or correcting existing tests
  - **build:** Changes that affect the build system or external dependencies (examples: Shadow, JavaFX, CoreNLP)
  - **chore:** Other changes that don't modify src or test files
  - **revert:** Reverts a previous commit
- Write the commit message subject line in the imperative mood ("Foo: Change the way dates work", not "Foo: Changed the way dates work").
- Write your commit messages in proper English, with care and punctuation.
- Amend your existing commits when adding changes after a review, where relevant.
- Mark each review comment as "resolved" after pushing a fix with the requested changes.
- Add your personal copyright line to files when making substantive changes. (Optional but encouraged!)
- Check the spelling of your code, comments and commit messages.

**Don't:**
- Submit changes that are incompatible with the project licence (AGPL 3.0)
- Touch anything outside the stated scope of the pull request.
- Iterate excessively on your design across multiple commits.
- Only use vague words like "refactor" or "fix" to avoid explaining what's being changed.
- End commit message subject lines with a period.
- Include commented-out code, unless to debug conveniently. If so, add a comment as to why you are leaving the code there.
- Attempt large architectural changes until you are familiar with the system and have worked on it for a while.
- Engage in excessive "feng shui programming" by cutting and pasting code around without quantifiable benefit.
- Add jokes or other "funny" things to user-facing parts of the system.

## On usage of AI and LLMs
Please refer to the [Mable's AI policy](https://github.com/n-xiao/mable/blob/master/docs/USE_OF_AI.md).

## Abandoned pull requests
Sometimes good pull requests (PRs) get abandoned by the author for one reason or another. If the PR is fundamentally good, but the author is not responding to requests, the PR may be manually integrated with minor changes to code and commit messages.

To make this easier, enable the "Allow edits from maintainers" flag on PRs.

## On neutrality
Mable will always be a program for everyone, irrespective of their opinions or worldviews. 
- This project will never take positions on issues outside the scope of the program.
- This project will not be used as a platform to advertise or promote unrelated causes.
- Discussions on societal politics and other divisive topics are discouraged in project spaces.

Sharing personal views and opinions outside of project spaces is encouraged. However, please keep project spaces focused on project goals.

## On bad-faith contributions and brigading
As the owner of Mable, [Nicholas Siow (@n-xiao)](https://github.com/n-xiao) reserves the right to reject issues and pull requests that appear to be motivated by bad faith.

Additionally, anyone found participating in social media brigading of Mable will be permanently banned from the project.

<br>

<h4 align="center">
  END OF CONTRIBUTING GUIDELINES
</h4>

<br>

## Attribution

This `CONTRIBUTING` document was adapted from the Ladybird `CONTRIBUTING` [document](https://github.com/LadybirdBrowser/ladybird/blob/master/CONTRIBUTING.md).
```
BSD 2-Clause License

Copyright (c) 2018-2025, the Ladybird developers.
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
```

The semantic prefix mentioned in this `CONTRIBUTING` document was adapted from the [Excalidraw development documentation](https://docs.excalidraw.com/docs/introduction/contributing#title).

```
MIT License

Copyright (c) 2020 Excalidraw

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

## License
This file is part of Mable.

Mable is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

Mable is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with Mable. If not, see <https://www.gnu.org/licenses/>.

<sub>Copyright © 2026 Nicholas Siow  <nxiao.dev@gmail.com> </sub>
