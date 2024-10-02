# Introduction

# SQL Queries

###### Table Setup (DDL)

```
CREATE TABLE cd.members(
  memid INTEGER NOT NULL, 
  surname CHARACTER VARYING(200) NOT NULL, 
  firstname CHARACTER VARYING(200) NOT NULL, 
  address CHARACTER VARYING(300) NOT NULL, 
  zipcode INTEGER NOT NULL, 
  telephone CHARACTER VARYING(20) NOT NULL, 
  recommendedby INTEGER, 
  joindate TIMESTAMP NOT NULL, 
  CONSTRAINT members_pk PRIMARY KEY (memid), 
  CONSTRAINT fk_member_recommendedby FOREIGN KEY (recommendedby) REFERENCES cd.members(memid) ON DELETE 
  SET 
    NULL
);
```

```
CREATE TABLE cd.bookings(
  bookid INTEGER NOT NULL, 
  facid INTEGER NOT NULL, 
  memid INTEGER NOT NULL, 
  starttime TIMESTAMP NOT NULL, 
  slots INTEGER NOT NULL, 
  CONSTRAINT bookings_pk PRIMARY KEY (bookid), 
  CONSTRAINT fk_bookings_facid FOREIGN KEY (facid) REFERENCES cd.facilities(facid), 
  CONSTRAINT fk_bookings_memid FOREIGN KEY (memid) REFERENCES cd.members(memid)
);
```

```
CREATE TABLE cd.facilities(
  facid INTEGER NOT NULL, 
  name CHARACTER VARYING(100) NOT NULL, 
  membercost NUMERIC NOT NULL, 
  guestcost NUMERIC NOT NULL, 
  initialoutlay NUMERIC NOT NULL, 
  monthlymaintenance NUMERIC NOT NULL, 
  CONSTRAINT facilities_pk PRIMARY KEY (facid)
);
```

###### Question 1: Show all members

```sql SELECT * FROM members```

###### Question 2: Insert some data

```
INSERT INTO facilities (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance) 
    VALUES (9, 'Spa', 20, 30, 100000, 800);
```

###### Question 3: Insert calculated data

```
INSERT INTO cd.facilities (
  facid, name, membercost, guestcost, 
  initialoutlay, monthlymaintenance
) 
SELECT 
  (
    SELECT 
      max(facid) 
    FROM 
      cd.facilities
  )+ 1, 
  'Spa', 
  20, 
  30, 
  100000, 
  800;
```
###### Question 4: Update some existing data

``` 
UPDATE cd.facilities
SET initialoutlay = 10000
WHERE facid = 1;
```





