# Backlog (Draft)

**Basic Features (Required for passing)**

• Client-Server Application (Spring Boot + JavaFX)

• No registration

• Multi User

• Auto synchronization of overview

• Drag and Drop

• Card == Title

• Flexibility (Add/remove cards and lists)

**Advanced Features (in order of priority)**

• Multi-Board (Join boards by name/key)

• Cards can have details

• Description

• Nested Task List

• Tag Support

• Customization (Background, Tag colors)

• Keyboard shortcuts/usage

• Password protected boards (read vs. write)

**Stakeholders**



* Casual Solo User (personal non-collaborative todo list. Think of a student organizing his/her homework)
* Casual Collaborative User (collaborative todo list to organize private projects. Think of a school project team’s members)
* Professional User (organizes collaborative workflow. Think of a developer, manager, team-leader etc.)

**Term Definitions:**

<span style="text-decoration:underline;">Board:</span> A “board” refers to one collaborative environment containing “columns”, which themselves contain “cards”.

<span style="text-decoration:underline;">Card:</span> A card is an entity describing a single task, it can be moved and categorized. A card can not leave a board, and can only travel across columns. 

<span style="text-decoration:underline;">Column:</span> A column refers to the actual column in the GUI which is used to categorize different cards based on their progress (eg: to do, in progress, done). 

<span style="text-decoration:underline;">Field:</span> A place where one can enter input to give to the application (eg: text area)

**User Epic 1 - Joining/creating board**



* As a developer, I want to be able to join the board my team is working on with the board-code so that I can collaborate with my team.
* As a manager, I want to be able to create a board for my team, share it with all the team members.
* As a casual user, I want to create my own board with various chores I need to do.
* As a casual user, I want to create a board and then share it with my friends so that we can collaboratively plan for an event. A group chat does not work so well because messages get lost among a flood of other messages, and we can’t organize very well.

**User Epic 2 - Adding a Card**



* As a user, I want to be able to create/add a card to a board without using my mouse. I want to use a nice shortcut to open the field to add a new card, enter the details, switching between fields using tab and shift+tab, and then add the card to the board by selecting the add button and pressing enter
* As a user, I want my friends/colleagues to see any cards I add in (more-or-less) real-time and want to see any updates to completion of tasks.
* As a user, I want to add tags to different cards both as I create them and after creation, to clearly organize my boards and make the boards more readable for other users. I also want to be able to edit and customize tags (their colors) 
* As a user, I want to create a new column to house new cards that all have something in common, but do not yet have a good column to place them in. This can also help declutter a column, by splitting it into several different columns. I guess this means I want to be able to add/remove/edit the columns that I have available. I also want to be able to add sub-tasks for my tasks, breaking them down into more manageable sizes while also keeping them grouped (making a separate column in these cases will clutter up my board and I don’t like that ):

**User Epic 3 - Moving a Card/ Removing cards**



* As a user, I would like to be able to copy and paste cards using the usual commands for cut, copy and paste. I want an easy shortcut to navigate between columns.
* As a user, I want to delete a card because I realized that my friend already created a card for that task, so there is a duplicate. 
* As a user, I want to drag and drop cards to put them into an order I find makes more sense and put them into corresponding columns.

**User Epic 4 - Collaborating with others**



* As a user, I would like to be able to collaborate on boards with other users of Talio. Synchronization between me and the users should be automatic and instant, so that we can collaborate in real time. 
* As a professional user/ manager, I want to be able to share access to my board through a password/code allowing me to collaborate with my team. 

**User Epic 5 - Multiple Boards/ Customization**



* As a user, I want to be able to make custom tags and assign a custom color to them.
* As a user, I want to be able to copy cards and even entire columns and move/paste them into a different board. I don’t like the texture of my trackpad and don’t always have a mouse on me so I want to be able to do this using my keyboard.
* Acceptance criteria: use arrow keys to navigate within a column, and use commands to navigate boards and columns (something for next/previous board/column)
* As a user, I want to be able to change my keyboard shortcuts to accommodate me better.

**Admin Epic 1**



* As an admin, I want to easily be able to restart the TALIO server.
* As an admin, I want to be able to add, remove or change passwords to/from existing boards.
* As an admin, I want to be able to remove existing boards from the overview.
* As an admin I want to be able to do all of the above things in the application

Question: Without any registration, how do we differentiate between admins and normal users?

Maybe based on whether they know the admin password or not.

