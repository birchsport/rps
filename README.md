# README #

This is the initial impl of a simple RPS game using an 'optimal algorithm'
strategy for winning the game (http://arstechnica.com/science/2014/05/win-at-rock-paper-scissors-by-knowing-thy-opponent/).  

I have tried to capture good/acceptable
Scala idioms/standards as much as possible, and documented it reasonably.
(With the obvious exception of having everything in one file.)

The initial idea is to use it as a base for teaching students 
how to write a simple game like this from scratch.

To play, check out this project, cd to the directory, and type 'sbt run'.

Also, please feel free to refactor, document, alter as you see fit,
just keep in mind the goal.

**Potential todos**

* Create some UI (i.e. web/Java2d)
* Add support for multiple players
* Add support for additional pieces (i.e. Lizard and Spock)
* Add support for more complex piece computation (analysis of previous moves, not just focusing on the last move)