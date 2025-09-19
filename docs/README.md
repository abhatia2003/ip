# Morpheus ChatBot User Guide

---
layout: page
title: Morpheus ChatBot User Guide
---

Morpheus ChatBot is a **desktop app for managing tasks, optimized for use via a Command Line Interface (CLI)** while still having the benefits of a Graphical User Interface (GUI). If you can type fast, Morpheus can help you manage your tasks faster than traditional GUI apps.

* Table of Contents
  {:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed on your computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

2. Download the latest `.jar` file from the releases page.

3. Copy the file to the folder you want to use as the _home folder_ for Morpheus ChatBot.

4. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar morpheus.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](docs/Ui.png)

5. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

    * `list` : Lists all tasks.
    * `todo read book` : Adds a ToDo task called `read book`.
    * `deadline submit report /by 2025-09-18` : Adds a Deadline task.
    * `event project meeting /from 2025-09-18 /to 2025-09-19` : Adds an Event task.
    * `remind`: Adds a new Reminder to a given task.
    * `reminders` : Shows a list of tasks that are due within the next 7 days.
    * `mark 2` : Marks the 2nd task shown in the current list as done.
    * `unmark 2` : Marks the 2nd task shown in the current list as not done.
    * `delete 3` : Deletes the 3rd task shown in the current list.
    * `bye` : Exits the app.

6. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `todo DESCRIPTION`, `DESCRIPTION` is a parameter which can be used as `todo read book`.

* Items in square brackets are optional.<br>
  e.g. `event DESCRIPTION /from START /to END` can be used as `event project meeting /from 2025-09-18 /to 2025-09-19` or as `event project meeting`.

* Items with `…` after them can be used multiple times including zero times.<br>
  e.g. `[tag/TAG]…` can be used as nothing (0 times), `tag/urgent`, or `tag/urgent tag/work`.

* Parameters can be in any order.<br>
  e.g. if the command specifies `todo DESCRIPTION`, `DESCRIPTION todo` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `list` and `bye`) will be ignored.<br>
  e.g. if the command specifies `list 123`, it will be interpreted as `list`.

</div>

---

### Listing all tasks : `list`
Shows a list of all tasks currently stored in Morpheus ChatBot.

**Format:** list
---

### Adding a ToDo : `todo`
Adds a ToDo task.

**Format:**  todo DESCRIPTION
**Example:**  todo read book
---

### Adding a Deadline : `deadline`
Adds a Deadline task.

**Format:** deadline DESCRIPTION /by DATE
**Example:** deadline submit report /by 18/09/2025