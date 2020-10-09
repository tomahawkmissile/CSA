# CSA

This program was for my final project for my high school computer science class. It is very basic and it is non-modular. It takes in an EAGLE board XML file and is able to produce a couple of metrics about it. These metrics include listing all capacitors, getting the total length of all copper traces of a certain signal, and getting the total area of all copper polygons of a certain signal.

Usage: java -jar XML.jar <file> [args]

Arguments Available:
# demo                =   run demonstration program
# listCapacitors      =   list capacitors in EAGLE XML document
# getLength <signal>  =   get total signal length of a given signal name
# getArea <signal>    =   get total signal polygon area given a signal name

**Note: output may be very large, and you will have the option to save it to a text file.**
