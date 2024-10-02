-- Question 2:
INSERT INTO cd.facilities (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
    VALUES (9, 'Spa', 20, 30, 100000, 800);

-- Question 3:
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