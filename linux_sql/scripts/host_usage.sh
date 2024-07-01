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
df_out=$(df -BM)

memory_free=$(echo "$vmstat_mb" | tail -1 | awk -v col="4" '{print $col}')
cpu_idle=$(echo "$vmstat_mb" | tail -1 | awk -v col="15" '{print $col}')
cpu_kernel=$(echo "$vmstat_mb" | tail -1 | awk -v col="14" '{print $col}')
disk_io=$(echo "$vmstat_mb" | tail -1 | awk -v col="10" '{print $col}')
disk_available=$(echo "$df_out" | tail -1 | awk -v col="4" '{print $col}' | sed 's/.$//')


timestamp=$(date -u +"%Y-%m-%d %H:%M:%S")

host_id="(SELECT id FROM host_info WHERE hostname='$hostname')";

insert_stmt="INSERT INTO host_usage ('timestamp', host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available) VALUES('$timestamp', '$host_id', '$memory_free', '$cpu_idle', '$cpu_kernel', '$disk_io', '$disk_available');"

export PGPASSWORD=$psql_password

psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
exit $?