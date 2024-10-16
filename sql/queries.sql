-- Modifying Data
-- Question 2:
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

-- Question 3:
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

-- Question 4:
UPDATE cd.facilities
SET
  initialoutlay = 10000
WHERE
  facid = 1;

-- Question 5:
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

-- Question 6:
DELETE FROM cd.bookings;

-- Question 7:
DELETE FROM cd.members
WHERE
  memid = 37;

-- Basics
-- Question 8:
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

-- Question 9:
SELECT
  *
FROM
  cd.facilities
WHERE
  name like '%Tennis%';

-- Question 10:
SELECT
  *
FROM
  cd.facilities
WHERE
  facid in (1, 5);

-- Question 11:
SELECT
  memid,
  surname,
  firstname,
  joindate
FROM
  cd.members
WHERE
  joindate > timestamp '2012-09-01 00:00:00';

-- Question 12:
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

-- Join
-- Question 13:
SELECT
  starttime
FROM
  cd.bookings as book
  INNER JOIN cd.members as memb ON book.memid = memb.memid
WHERE
  firstname = 'David'
  AND surname = 'Farrell';

-- Question 14:
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

-- Question 15:
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

-- Question 16:
SELECT DISTINCT
  mem1.firstname as firstname,
  mem1.surname as surname
FROM
  cd.members as mem1
  INNER JOIN cd.members as mem2 ON mem1.memid = mem2.recommendedby
ORDER BY
  surname,
  firstname;

-- Question 17:
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

-- Aggregation
-- Question 18:
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

-- Question 19:
SELECT
  facid,
  SUM(slots) as "Total Slots"
FROM
  cd.bookings
GROUP BY
  facid
ORDER BY
  facid;

-- Question 20:
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

-- Question 21:
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

-- Question 22:
SELECT
  COUNT(DISTINCT memid) as count
FROM
  cd.bookings;

-- Question 23:
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

-- Question 24:
SELECT
  COUNT(*) OVER (),
  firstname,
  surname
FROM
  cd.members
ORDER BY
  joindate;

-- Question 25:
SELECT
  ROW_NUMBER() OVER (
    ORDER BY
      joindate
  ),
  firstname,
  surname
FROM
  cd.members;

-- Question 26:
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

-- Question 27:
SELECT
  surname || ', ' || firstname as name
FROM
  cd.members;

-- Question 28:
SELECT
  memid,
  telephone
FROM
  cd.members
WHERE
  telephone like '(___)%';

-- Question 29:
SELECT
  left (surname, 1) as letter,
  Count(*) as count
FROM
  cd.members
GROUP BY
  letter
ORDER BY
  letter;
