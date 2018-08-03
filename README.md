Implementation Details:<br/>
	Reading json file<br/>
	Creating Objects<br/>
	Iterating over the list to de-dup with respect to ID<br/>
	Iterating over the list to de-dup with respect to Email<br/>
	In both of the above steps taking care of date as well<br/>
<br/><br/>
Libraries used:<br/>
	json-simple-1.1.jar - For dealing with json<br/>
	gson-2.8.4 - For writing the JSONObjects to output.json<br/>
<br/><br/>
Steps to run:<br/>
	$ make clean<br/>
	$ make <br/>
	$ make run<br/>
<br/><br/>
Output:	<br/>
	$ cat output.json<br/>
	$ cat logs.json<br/>
	Output is written to output.json<br/>