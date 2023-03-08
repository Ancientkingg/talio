# Code Contributions and Code Reviews

**NEVER FORGET TO MERGE EVERY BRANCH TO MAIN BEFORE THE MEETING!!!**
I can only review what was added on the main branch, therefore this report will not contain some of your MRs for this week. Please, on Sunday evening (the latest), merge all your MRs into main.

Another general suggestion: please work with issues more. The branches you work on should be created **from** those issues and merged directly to main/ a development branch as I explained last meeting.

#### Focused Commits

Grade: Good

Feedback: The commit names are clear and there is an overall good amount of commits **on the whole repo** (including your other branches). The a seem to only handle a single piece of functionality, which is good. Please make sure you do not have commented code (or big pieces at least) when pushing a commit.
**However**, not all people have commits on the main branch, which is definitely not enough for a whole week. Again, the final grades are computed based on the main branch, so never forget to merge.


#### Isolation

Grade: Very Good

Feedback: The, branches and merge requests seem to be used correctly to isolate individual features during development and are focused on the specific functionality you are implementing, which is great.


#### Reviewability

Grade: Good

Feedback: The merge requests need to have more elaborate descriptions, in order to make it clear for the other teammates what they are reviewing. Regarding their content, they contain a small amount of commits, with coherent and related changes. Good work so far!


#### Code Reviews

Grade: Very Good

Feedback: The code reviews are good, cover the topic extensively and help to iteratively improve the code. However, I could notice a difference between the amount of implication between team members regarding these code reviews. If you feel like you weren't this involved this week in writing code reviews, please make an effort this upcoming week.


#### Build Server

Grade: Very good

Feedback: It's great that you push frequently, since it shows that the build server was an important part of your development process. The build duration is reasonable and most of the pipelines passed.
Great that you have the checkstyle pipeline set up! You can try splitting the pipeline in 3 instead of 2, with the checkstyle moved in the middle (so: build - checkstyle - test). From personal experience, this is the usual flow I've seen when working on these kind of projects.

