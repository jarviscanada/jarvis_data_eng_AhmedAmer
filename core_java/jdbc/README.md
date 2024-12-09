Command for running docker images:
docker run --rm --name stock-quote -e POSTGRES_PASSWORD=password -d -v $HOME/srv/postgres:/var/lib/postgresql/data -p 5432:5432 postgres
docker run --rm -it -e ALPHA_VANTAGE_KEY=$env:ALPHA_VANTAGE_KEY ahmedamerworks/jdbc
docker run --rm -it -e ALPHA_VANTAGE_KEY=$ALPHA_VANTAGE_KEY ahmedamerworks/jdbc
