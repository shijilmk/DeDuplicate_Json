# DeDuplicate_Json Java Program
Remove duplicate records from Json file based on keys

 1. The data from the newest date should be preferred

2. Duplicate ids count as dups. Duplicate emails count as dups. Both must be unique in our dataset. Duplicate values elsewhere do not count as dups.

3. If the dates are identical the data from the record provided last in the list should be preferred

Details
Input Source - sample : input.json
Output results : output.json

Library - json

Running jar from Command Line:
Download the deduplicatejson-jar-with-dependencies.jar file and  navigate to the path and use below command to run it :
java -jar deduplicatejson-jar-with-dependencies.jar
