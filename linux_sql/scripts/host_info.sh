psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

if [ "$#" -ne 5 ]; then
    echo "Illegal number of parameters"
    exit 1
fi

vmstat_mb=$(vmstat --unit M)
hostname=$(hostname -f)
lscpu_out=$(lscpu)
dmesg_out=$(dmesg)

cpu_number=$(echo "$lscpu_out"  | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out" | egrep "Architecture:" | awk '{print $2}' | xargs)
cpu_model=$(echo "$lscpu_out" | grep 'Model name' | cut -f 2 -d ":" | awk '{$1=$1}1' | xargs)
cpu_mhz=$(echo "$dmesg" | grep MHz | awk '{print $5}')
l2_cache=$(echo "$lscpu_out" | egrep "L2 cache:" | awk '{print $3}' | xargs)
total_mem=$(echo "$vmstat_mb" | tail -1 | awk '{print $4}')



timestamp=$(date -u +"%Y-%m-%d %H:%M:%S")
id="" #Need to find a way to increment ID for insert statement

insert_stmt="INSERT INTO host_info (id, hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, 'timestamp', total_mem) VALUES(1, '$hostname', '$cpu_number', '$cpu_architecture', '$cpu_model', '$cpu_mhz', '$l2_cache', '$timestamp', '$total_mem');"

export PGPASSWORD=$psql_password

psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
exit $?