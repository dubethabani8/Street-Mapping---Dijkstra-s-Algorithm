Implementations of Graph, Node and Edge classes have been adopted from rosettacode.org

HOW TO RUN:
This program runs according to the following command line arguments:

java StreetMap map.txt [ --show ] [--directions startIntersection endIntersection ]

Example runs:
java StreetMap ur.txt --show --directions HOYT MOREY   // Showing both map and the directions
java StreetMap ur.txt --show    // Just showing the map
java StreetMap ur.txt --directions HOYT MOREY   // Showing the map is optional .

However, check when copy/pasting arguments that all arguments are correct including spacing between -- and
show/directions. Should be --show and --directions without spacing.


I implemented extra features that make the program more interactive.

-You can run the program without any arguments and it will default to the ur.txt map.
-You can use drop down menus to select directions between any two points and map them
-You can also change the map choices and everything will change accordingly.

NB: When switching between maps, allow for some time to refresh as some map sizes are
large and require longer computations to build up.

Many complexities arose from this implementation requiring me to add a lot of checks and change the way the maps
are built from user input. This was so as to allow inputs and respective details to be switched during runtime
without having to build a new program altogether.
