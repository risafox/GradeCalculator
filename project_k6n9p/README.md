# Grade Calculator
## Project Overview
The grade calculator is a simple desktop application designed to 
**help students keep track of their grades across numerous classes**. 

The grade calculator will come with *many* functionalities:
- Calculate your current average within one course and across all 
  courses, weighted appropriately
- Calculate what grade you need on the remainder of your
  assignments to achieve your intended grade  
- Add/edit/delete courses and items within courses
- Keep track of your courses throughout the semester
- Enter grades as raw scores (fractions), as well as a decimal 
number or percentage
  

## Intended Audience
This grade calculator has been designed to be **optimal for students**.
However, this can be used by anyone interested in keeping track 
of marks, such as teachers and parents.

## Why did I build this project?
I typically spend a lot of time during finals season using Ben Eggleston's
online grade calculator (https://www.thegradecalculator.com/ -- it's great), 
calculating the mark I need to get on the final to achieve my intended grade
instead of actually studying for my finals. However, there are two main problems 
I had with this site:

1. You can not enter fractions. This was often a pain, because I received many
of my marks as fractions (e.g. 45/50), which I would then need to convert to a decimal on a
separate calculator. It seemed redundant to have to use two calculators to calculate my grades.
2. The site would not save your data across sessions. Since I frequented this site
throughout the semester, I found myself entering the same data over and over with few
changes from the previous time.
   
This grade calculator addresses both of those issues, without losing any of the 
smooth functionality seen in other grade calculators.
  
## User Stories
Phase 1 Stories:
- As a user, I want to be able to add a new assignment (X) to a course (Y).
- As a user, I want to be able to add multiple courses, each with their own set of assignments.
- As a user, I want to be able to add, edit, or delete my courses.
- As a user, I want to be able to calculate my current average for each course
  based on the appropriate weight of each assignment.
- As a user, I want the grade calculator to tell me what mark I would need to 
  achieve my intended final mark for the course. I also want to be told the mark 
  I would receive if I got x% on the remainder of my assignments.
  
Phase 2 Stories:
- As a user, I want to be able to save my courses and grades to a file.
- As a user, I want to be able to load my courses from a previous time I used the grade calculator.

Phase 3 Stories:
- None, saving them for phase 4  

## Phase 3 Criteria
- Adding multiple X to Y:
  - There are multiple courses added to a course list in the main panel, as well as assignments added
    to those courses.
  - Events that relate to those X and Y: 
    - Buttons that facilitate adding, editing, and removing courses, as well as saving, loading, and clearing data
    - Double-clicking on an element in the main panel will bring up a dialog box informing the user of their progress
      in this course
- Saving and loading:    
  - There are buttons that facilitate this process near the top right-hand side of the frame
- Audiovisual component:
  - The icon for the application (on the top left-hand corner of the frame) has been changed from the default
    Java swing penguin to a calculator icon.
  - There are 3 images (good, neutral, and bad), that are displayed upon double-clicking on a course, depending on the
    user's progress in that course.
      

  
  