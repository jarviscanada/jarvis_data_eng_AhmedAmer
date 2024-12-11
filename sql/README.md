# Introduction

This project set up a database for a mock scenario of a newly created country club using PSQL/Docker. The goal was to learn SQL concepts such as DDL, DML, and DQL statements and additional clauses that filter for selected data (WHERE, HAVING, GROUP BY, etc). We also dove into the different forms of JOINs, aggregate functions, string manipulation, and advanced concepts such as window functions. The database sits locally on the JRD where each statement is run via the Linux CLI. It consists of three tables - members, bookings, and facilities. The DDL statements are listed below, as well as each SELECTION and data MODIFICATION statement after that.

# SQL Queries

###### Table Setup (DDL)

```SQL
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

```SQL
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

```SQL
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

### Modifying Data

###### Question 1: Show all members

```SQL
SELECT * FROM members
```

###### Question 2: Insert some data

```SQL
INSERT INTO
  cd.facilities (
    facid,
    name,
    membercost,
    guestcost,
    initialoutlay,
    monthlymaintenance
  )
VALUES
  (9, 'Spa', 20, 30, 100000, 800);
```

###### Question 3: Insert calculated data

```SQL
INSERT INTO
  cd.facilities (
    facid,
    name,
    membercost,
    guestcost,
    initialoutlay,
    monthlymaintenance
  )
SELECT
  (
    SELECT
      max(facid)
    FROM
      cd.facilities
  ) + 1,
  'Spa',
  20,
  30,
  100000,
  800;
```

###### Question 4: Update some existing data

```SQL
UPDATE cd.facilities
SET
  initialoutlay = 10000
WHERE
  facid = 1;

```

###### Question 5: Update a row based on contents of another

```SQL
UPDATE cd.facilities
SET
  membercost = (
    SELECT
      membercost * 1.1
    from
      cd.facilities
    WHERE
      facid = 0
  ),
  guestcost = (
    SELECT
      guestcost * 1.1
    from
      cd.facilities
    WHERE
      facid = 0
  )
WHERE
  facid = 1;
```

###### Question 6: Delete all bookings

```SQL
DELETE FROM cd.bookings;
```

###### Question 7: Delete a member from the cd.members table

```SQL
DELETE FROM cd.members
WHERE
  memid = 37;
```

### Basics

###### Question 8: Control which rows are retrieved 2

```SQL
SELECT
  facid,
  name,
  membercost,
  monthlymaintenance
from
  cd.facilities
WHERE
  membercost > 0
  AND membercost < (monthlymaintenance / 50);
```

###### Question 9: Basic string searches

```SQL
SELECT
  *
FROM
  cd.facilities
WHERE
  name like '%Tennis%';
```

###### Question 10: Matching against multiple values

```SQL
SELECT
  *
FROM
  cd.facilities
WHERE
  facid in (1, 5);
```

###### Question 11: Working with dates

```SQL
SELECT
  memid,
  surname,
  firstname,
  joindate
FROM
  cd.members
WHERE
  joindate > timestamp '2012-09-01 00:00:00';
```

###### Question 12: Combining results from multiple queries

```SQL
SELECT
  surname
FROM
  cd.members
UNION
(
  SELECT
    name
  FROM
    cd.facilities
);
```

### Join

###### Question 13: Start times of members' bookings

```SQL
SELECT
  starttime
FROM
  cd.bookings as book
  INNER JOIN cd.members as memb ON book.memid = memb.memid
WHERE
  firstname = 'David'
  AND surname = 'Farrell';
```

###### Question 14: Work out start times of bookings...

```SQL
SELECT
  starttime as start,
  name
FROM
  cd.bookings as book
  INNER JOIN cd.facilities as fac ON book.facid = fac.facid
WHERE
  starttime >= DATE '2012-09-21'
  AND starttime < '2012-09-22'
  AND name in ('Tennis Court 2', 'Tennis Court 1')
ORDER BY
  start;
```

###### Question 15: Members and their recommender

```SQL
SELECT
  mem1.firstname as memfname,
  mem1.surname as memsname,
  mem2.firstname as recfname,
  mem2.surname as recsname
FROM
  cd.members as mem1
  LEFT OUTER JOIN cd.members as mem2 ON mem1.recommendedby = mem2.memid
ORDER BY
  memsname,
  memfname;
```

###### Question 16: All members who recommended

```SQL
SELECT DISTINCT
  mem1.firstname as firstname,
  mem1.surname as surname
FROM
  cd.members as mem1
  INNER JOIN cd.members as mem2 ON mem1.memid = mem2.recommendedby
ORDER BY
  surname,
  firstname;
```

###### Question 17: All members, recommeders, no joins

```SQL
SELECT DISTINCT
  mem1.firstname || ' ' || mem1.surname as member,
  (
    SELECT
      mem2.firstname || ' ' || mem2.surname as recommender
    FROM
      cd.members as mem2
    WHERE
      mem2.memid = mem1.recommendedby
  )
FROM
  cd.members as mem1
ORDER BY
  member;
```

### Aggregation

###### Question 18: Number of recommendations each member made

```SQL
SELECT
  recommendedby,
  COUNT(*) as count
FROM
  cd.members
WHERE
  recommendedby IS NOT NULL
GROUP BY
  recommendedby
ORDER BY
  recommendedby;
```

###### Question 19: Total slots per facility

```SQL
SELECT
  facid,
  SUM(slots) as "Total Slots"
FROM
  cd.bookings
GROUP BY
  facid
ORDER BY
  facid;
```

###### Question 20: Total slots per facility in one month

```SQL
SELECT
  facid,
  SUM(slots) as "Total Slots"
FROM
  cd.bookings
WHERE
  starttime >= '2012-09-01'
  AND starttime < '2012-10-01'
GROUP BY
  facid
ORDER BY
  "Total Slots";
```

###### Question 21: Total slots per facility per month

```SQL
SELECT
  facid,
  EXTRACT(
    MONTH
    FROM
      starttime
  ) as month,
  SUM(slots) as "Total Slots"
FROM
  cd.bookings
WHERE
  starttime >= '2012-01-01'
  AND starttime < '2013-01-01'
GROUP BY
  facid,
  month
ORDER BY
  facid,
  month;
```

###### Question 22: Count members with at least one booking

```SQL
SELECT
  COUNT(DISTINCT memid) as count
FROM
  cd.bookings;
```

###### Question 23: Each member's first booking after 2012-09

```SQL
SELECT
  mem.surname,
  mem.firstname,
  mem.memid,
  MIN(book.starttime) as startime
FROM
  cd.members as mem
  INNER JOIN cd.bookings as book ON mem.memid = book.memid
WHERE
  starttime > '2012-09-01'
GROUP BY
  mem.surname,
  mem.firstname,
  mem.memid
ORDER BY
  mem.memid;
```

###### Question 24: Member names, with each row containing the total member count

```SQL
SELECT
  COUNT(*) OVER (),
  firstname,
  surname
FROM
  cd.members
ORDER BY
  joindate;
```

###### Question 25: Numbered list of members

```SQL
SELECT
  ROW_NUMBER() OVER (
    ORDER BY
      joindate
  ),
  firstname,
  surname
FROM
  cd.members;
```

###### Question 26: Facility id with highest number of slots

```SQL
SELECT
  facid,
  total
FROM
  (
    SELECT
      facid,
      SUM(slots) as total,
      RANK() OVER (
        ORDER BY
          SUM(slots) DESC
      ) as rank
    FROM
      cd.bookings
    GROUP BY
      facid
  ) as ranked
WHERE
  rank = 1;
```

### String

###### Question 27: Format names of members

```SQL
SELECT
  surname || ', ' || firstname as name
FROM
  cd.members;
```

###### Question 28: Find telephone parentheses

```SQL
SELECT
  memid,
  telephone
FROM
  cd.members
WHERE
  telephone like '(___)%';
```

###### Question 29: Count members by each surname letter

```SQL
SELECT
  left (surname, 1) as letter,
  Count(*) as count
FROM
  cd.members
GROUP BY
  letter
ORDER BY
  letter;
```
