# Introduction
Given a network of 10 or so Linux machines, the Linux Cluster Admin team requested a way to record and store each machine's memory usage data in real-time. 
Our team designed and implemented a way to do so using an RDBMS (postgresSQL), a container to run and manage database entries, and bash scripts that would help automate the data collection process. 
Our codebase is housed in a remote git repository.
The end result involves two Bash scripts, host_info and host_usage, where host_info collects hardware specs of each machine once and sends it to the database. 
host_usage runs on each machine every 1 minute (using crontab) and tracks their memory usage in real-time.


# Quick Start
- Start the psql instance using the psql_docker.sh Bash script. Use the start option.

```bash ./scripts/psqldocker.sh start```

- Use ddl.sql to quickly initialize the SQL tables host_info and host_usage

```bash ./sql/ddl.sql```

- Insert hardware specs using host_info.sh - This will be done once for each machine

```bash ./scripts/host_info.sh psql_host psql_port db_name psql_username psql_password```

- Insert memory usage specs using the host_usage.sh script, which will also be run every minute using crontab.

```bash ./scripts/host_usage.sh psql_host psql_port db_name psql_username psql_password```

- Finally, create a crontab job for host_usage.sh

```crontab -e```

```* * * * * bash [path]/scripts/host_usage.sh psql_host psql_port db_name psql_username psql_password > /tmp/host_usage.log```

- crontab -l lets us check new jobs

```crontab -l```

## Architecture
*Image to be inserted* 

## Scripts
- psql_docker.sh:
	Use psql_docker to start/stop/create the postgreSQL container.
	The script will check to see if container is running or if it has been created.
	Commands: start | stop | create [db_username] [db_password]
- host_info.sh:
	Run once for every node in the network.
	Gathers hardware info and stores it in host_info table using each node's ID
	All arguments required: 
		psl_host: "localhost"
		psql_port: the port connecting the psql container to the volume
		db_name: name of the psql database
		psql_user: username to access database
		psql_password: password to access database
- host_usage.sh:
	Run every minute using crontab to collect memory usage of each node. Data is stored in host_usage table.
	Same arguments as host_info script.

## Database Modeling
Below is a brief overview of the data schema of the SQL tables with examples:
host_info (hardware specifications):
- id: NON NULL SERIAL PRIMARY KEY e.g. -> 1 
- hostname: NON NULL VARCHAR e.g. -> 'jrvs-remote...'  
- cpu_number: NON NULL INT2 e.g. -> 2
- cpu_architecture: NON NULL VARCHAR e.g. -> 'x86_64'
- cpu_model: NON NULL VARCHAR e.g -> 'Intel(R) Xeon(R) CPU @ 2.30GHz'
- cpu_mhz: NON NULL FLOAT8 e.g. -> 2300
- l2_cache: NON NULL INT4 e.g. -> 256 
- timestamp: TIMESTAMP e.g. -> '2024-07-09 16:49:53.000'
- total_mem: INT4 e.g. -> 599233

host_usage (memory usage every minute):
- timestamp: TIMESTAMP e.g. -> '2024-07-09 16:49:53.000'
- host_id: NON NULL SERIAL FOREIGN KEY e.g. -> 1
- memory_free: NON NULL INT4 e.g. -> 256
- cpu_idle: NON NULL INT2 e.g. -> 98
- cpu_kernel: NON NULL INT4 e.g. -> 0
- disk_io: NON NULL INT4 e.g. -> 1
- disk_available: NON NULL INT4 e.g. -> 30020

# Test
Testing the scripts in some cases was as simple as running them in the shell. For host_info.sh, I ran the script with no arguments to make sure the script throws an error message.
Then I tested it with real arguments to see if it stored the data in the correct table and in the correct columns. Same for host_usage.sh

For psql_docker.sh, I made sure it threw an error if the right amount of arguments was NOT supplied. In the case that the script ran, I made sure the 'create' option correctly checked
for whether the docker container already existed. For the 'run' option, I made sure the script checked whether the container was already running.

Lastly, for ddl.sql I made sure the script checks to see if the database and table already exists first before executing the commands.


# Deployment
This git repository was used to house the entire code base in Gitflow fashion.
I.E. changes usually ran from feature_branch -> develop branch -> main

As mentioned above, crontab was used to deploy and automate the host_usage script, while the db and postgreSQL were built using the docker container.



# Improvements
- One thing to consider is that maybe there should be a different host_usage table for each machine to keep the data more organized and accessible.
- Some of the actions of the scripts like ddl.sql could be converted to reusable functions.
- Add extra script for SQL data manipulation, or repeated SQL queries.
