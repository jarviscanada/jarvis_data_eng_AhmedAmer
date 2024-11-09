# Introduction
This project features a Java app that mimics the behaviour of the Linux terminal 'grep' command - searching for lines that house
strings that match the provided Regex pattern. We first designed an interface for the implementation and then used standard Java API
BufferedReaders and BufferedWriters to read lines and write them to a new file. After facing performance issues, methods were implemented
using streams and lambda expressions instead. We also used SLF4J logger for debugging, Maven to build and package the app, and Docker to build
an image of the app and distribute it.

# Quick Start
First pull the docker image to your system using:
```bash
docker pull ahmedamerworks/grep
```
Then create and run the image in a docker container. For example:
```bash
docker run --rm -v `pwd`/data:/data -v `pwd`/log:/log ahmedamerworks/grep [regex] [rootPath] [outFile]
```
Note that the app requires all three arguments where:
- regex: the regex expression used to find matches in the files
- rootPath: the directory where you are looking for matches
- outFile: path of the file you want to write to

# Implementation
## Pseudocode
The process() method was designed as follows - it serves as the main method for the app:
```
    Array matchLines
    for file in listFiles(rootDir):
        for line in readLines(file):
            if containsPattern(line):
                matchedLines.add(line)
    writeToFile(matchedLines)
```

## Performance Issue
One performance issue encountered during the testing phase of the app is that we run into an ```OutofMemory```
exception in cases where Java heap memory is used up. This occurs with very large files that need to be loaded when
reading each line and therefore takes up more memory. One approach we used to combat this was to make sure the Java heap
min/max configurations housed enough memory for larger files.

# Test
For testing, we ran the app with different regex and several times in order to check for consistency.
We made sure to test for scenarios in which a file was being overwritten with the app's output as well as scenarios
where the app was searching for files in empty folders, folders with only folders present, and folders with files that
could not be read.

# Deployment
Docker was used to build an image out of the app and ship to Docker hub. From there, the app is readily available
for download and can be easily accessed via pulling the Docker image and running it inside a local container.

# Improvement
- In terms of performance, we can seek out a more efficient implementation that reduces the possibility of 
```OutOfMemory``` exceptions.
- We could also work on using just streams instead of any for loops.
- Lastly, the app could benefit from more features, such as an argument for returning just the first line or last line.