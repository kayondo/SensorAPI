
Assumptions 

. The user of the API has installed the latest version of Java SDK and the Java Runtime 
Environment (JRE) on their machine. 
. The user need not install a third party software on their system in order to use the API 
other than the latest JRE and Java SDK that is already installed on their system. 
. Assume that time in the current format will always be present as the default. 
. Assume the user inputs the time to collect the data in minutes 
. Assume the user inputs the measurement types and measurements units separated by 
commas and those two types will be separated by a whitespace (e.g. 
Temperature,Humidity,Pressure F, H, Kpa). 
. In case a measurement type doesn’t have a unit for example Humidity, the user will place 
a hypen “-” while writing the measurement unit for example 
Temperature,Humidity,Pressure F,-,kPa 
. Assume that the user sets a start and end time that lies in the range of the data u 
downloaded prior, if not an error is returned. 
. Assume the time span of the data is the starting time and the ending time of the data 
collected. 
. Assume the Sensor(DataGen) gives correct values when asked to. 


 

 

Design of the File System 

 

Files are created and are their names are made up of the time and date at which they are created. 
The reason is that we have to get a unique name for each file, and the file should also be 
accessed by the API when it needs to summarize, search or to read that file. 

 

The file is created when the user asks to collect the data. The user will never know how the file is 
created. 

 

Files in the file system are stored in folders or directories. Therefore the file system protects the 
different users and their data. Different users of the API can only access the data they have 
collected. They don’t have access to data collected by other users. 

 

API structure 

 

An API is a basic library consisting of interfaces, functions, classes, structures, enumerations, 
etc. for building a software application. Our SensorAPI has 4 APIs. It includes interfaces for 
different operations, such as collecting the data, summarizing the collected data, searching and 


reading the collected data. Other APIs (For example, APIs that can access external memory) can 
also be built onto our API because the files of the data collected are saved in the physical 
memory (For example: edit, delete…). 

 

The API adheres to a same-origin policy i.e. all files created by the API whenever a test program 
collects data from the sensor are kept in the same directory as the API. The API further restricts 
all test programs to only perform collections, summaries of data and searches, retrievals on files 
from this particular directory. The reason is to protect private data by preventing access and 
deletion from other users or clients. 

 

Metadata management, file type and structure 

 

The file is created in the same directory as the Sensor API. This file is named based on the 
current data and time (for example filename: 18042016100200.txt as 18th of April, 2016 at 
10:02:00 AM). It is a flat file with a .txt extension. We chose files be created with this format 
for it is easy to retrieve each line into the API sequentially as a string from the file and perform 
operations such as read. The file is populated by a line made of time, and the data coming from 
the DataGen server. The first line of the file will always be the header with labels (measurement 
types) and the units. This is to avoid the redundancy in the file as they will only be written once. 

 

SensorAPI methods 

 

API interface contains the following methods: collect(time, labels), summary(), search(), 
readData(measurementType, startTime, endTime). 

 

We used the interface to maintain the design structure of our program. 

These methods will be explained below. 

 

collect(time,labels): gets the time and labels as strings and collect data of those labels in that 
time from the sensor (server) and store them in a file created with the timestamp as the name. 

 

This method sets up a socket connection with the Sensor (DataGen.java). The default port 
number used is 6789. The user is prompted to input how long they want to collect the data and 
determine what labels or measurement types and units. 

The labels are sent to the sensor (server) which in turn generates and returns random values 
along with the timestamp when the collection was done. 

 

The timestamp and values collected for each turn are returned to the client in this method where 
they are used to make a line and that line is written to the file. The file creation is abstraction 
from the user’s knowledge. The user can only get the file with values in the same folder as the 
application. 


 

. Two array lists, measurementTypesList and measurementUnitsList are used to store the 
labels and units respectively in the order that the user input prior. We used array lists 
because it is easy to get the index for each value. 
. The measurements types (labels) are later on called by readData() to get their index to be 
used by this function to easily retrieve data where we have those measurements. (e.g if 
the user wants data for only temperature, readData() gets the index of temperature from 
the original data and use it to retrieve only temperature data which will be displayed to 
the user). We chose this because it is easy to retrieve from array lists. 


 

summary(): provides a summary of the data from the user. It uses the file created to provide that 
summary to the user. 

 

The summary includes: 

. The time span over which the data has been collected from the sensor. This is obtained by 
reading the second and last lines of the file stored and obtaining and displaying the two 
time indices to the user. 
. The measurement labels and their respective units. To get these, the first line is read from 
the file. This line has all the measurement labels and their respective units. We opted for 
this over searching from the arraylists containing this information, in order to curb time 
complexity. 
. How much data has been collected from the sensor. This is obtained by getting the size of 
the file (in bytes) created while collecting data from the sensor. 


 

 

search() performs search on the collected data and return the time index of the first occurrence 
fulfilling a set criteria. The user is offered an option to get the next occurrence by pressing any 
button and “exit” to leave the search method. (E.g. Search let the user enter “Temperature > 
25 & Pressure < 24 | Humidity > 25 end “ and take each condition, pass it internalSearch() 
which returns a list of values satisfying this condition, then do it again for each condition, until it 
meets the word “end”). 

 

The search method takes the query, provided by the user, and returns the timestamps, in order of 
occurrence, of values that satisfy the query. These timestamps are stored in a Queue ADT. To 
display the result in order of occurrence, we dequeue the values. The order of occurrence is 
maintained, the reason why we chose to use the Queue ADT. 

 

readData(measurementType, startime, endTime): gets the labels of interest from the user, the 
start time and the end time as strings and reads the data within a user-specified timespan and 
store it in a new file. The method creates the file that stored the data got from that operation of 
reading. 


 

The arraylist ADT is used to store the indices of labels entered by the user, because it is easy to 
use those indices and check if the label entered is valid and to get the corresponding values from 
the data filtered using the start time and the end time. 

 

Other methods in the API 

 

internalSearch(searchString): It is called by search() and it takes a string, splits and checks 
from the data where the data satisfies the condition. (e.g Temperature > 25). The function will 
return to search() a list of values satisfying the condition. 

 

firstOccurence(): This method displays the first timestamp at which a particular search query is 
satisfied. This is achieved by dequeuing the first element from the queue holding the results. 
These results are enqueued in order of occurrence . 

 

nextOccurence(): Just like the firstOccurence() method, this method prints the element at the 
top of the queue holding the time stamps that satisfy a search query results. This continues for as 
long as either :- 

(i) The queue is not empty 
(ii) the user asks the program to stop displaying these results by typing exit. 


 

Boolean isBlank(str): function that gets a string and checks if the string given is null or blank or 
a whitespace and then returns true or false. 

It is used everywhere users have to enter something so that the program can prompt for input 
again if they did not enter something. 

 

readFile(filename): takes the file and read it. This is necessary in case the read is needed. We 
did not include it in the testProgram because we haven’t being asked to. 

 

copyFile(filename): copies the contents of one file onto an empty file. This is done to keep track 
of different files created without overwritting another one. 

 

Other Classes 

 

DataGen class: sensor or server to generate random values depending on how many 
measurements the user entered. The user is allowed to enter any number of labels and random 
values of those labels will be provided to the user or client. 

We have edited the server for it to be able to provide those values randomly without hardcoding. 

DataGen uses GetRandomNumber() and CoinToss() to create the randomness of numbers. 

 


Data class: takes members, time as a string and values as linked list with float type. The time is 
the timestamp of the data from the sensor and the values are those representing labels and got 
from the sensor. 

It has getters and setters, toString() and ListIterator to loop through the elements of Data type. 

 

 

 

Analysis of search algorithms in terms of performance 

 

. Reading the file that was stored on the disk is O(n) 


 Each query done is O(k), k as number of lines to search from and results. 

The performance of search is O(n) + O(k). 

. Storing the the data read from a file in the memory is O(n), but storing the results from a 
search is O(k) where k is the size of the results got. 


 The memory usage is O(n) + O(k). 

. We opt to overwrite the file each time data is collected by the user in order to preserve 
memory. Users are prone to error for example collecting measurement labels they do not 
need. Creating a new file was also a good idea for the user to keep track of all files for 
future use. This will be put in our tradeoff below. 


 

 

 

Description of limitations and tradeoffs 

 

. We planned to create a new file by using the timestamp (data + time) as the name when 
the user created the file but the program was not yet ready to do it, by the time we 
submitted. One tradeoff is that it is overwriting the existing file. 
. Another tradeoff is achieving performance of the program for enough memory to be able 
to handle different data collected by the user. 


 

SensorAPI programmers guide. 

 

The SensorAPI programmers’ guide is attached in the zip folder for more information. 

 

Summary: The user will be able to see and use our API by running the following functions, 
details are in the above comments made. 

 

collect(): to collect data from the sensor or server where the file is created. 

summary(): to get the summary of the data created by displaying the results to the user. 

search(): to search from the data created by displaying the data filtered by condition given in 
order of occurrence. 


readData(): to read the data from the file created and return the file created for the user to be user 
for other purposes. 

 

Every function is independent apart from collect() which will always be called first. Calling 
summary(), search() or readData() before call will display an error to the user to first collect the 
data. This is because the server provides data depending on labels and the number of labels given 
by the user. 

After calling collect(), you can either call summary() or search() or readData() any time you 
want. 

 

 

Special cases: 

. When the user searches by putting a wrong syntax. E.g Temperature > 25 without an end 
keyword at the “end”, an error is thrown that it is a wrong syntax. 


 

 

 

 

 

 


