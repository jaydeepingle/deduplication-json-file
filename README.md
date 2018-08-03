Implementation Details:
	Reading json file
	Creating Objects
	Iterating over the list to de-dup with respect to ID
	Iterating over the list to de-dup with respect to Email
	In both of the above steps taking care of date as well

Libraries used:
	json-simple-1.1.jar - For dealing with json
	gson-2.8.4 - For writing the JSONObjects to output.json

Steps to run:
	$ make clean
	$ make 
	$ make run

Output:	
	$ cat output.json
	$ cat logs.json
	Output is written to output.json