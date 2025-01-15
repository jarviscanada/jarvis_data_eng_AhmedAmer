-- Show table schema 
\d+ retail;

-- Show first 10 rows
SELECT * FROM retail limit 10;

-- Check # of records
SELECT COUNT(*) as number_of_records FROM retail;

-- number of clients (e.g. unique client ID)
SELECT COUNT(DISTINCT customer_id) FROM retail;

-- range of invoice dates (maximum and minimum)
SELECT MAX(invoice_date) as max, MIN(invoice_date) as min
FROM retail;

-- number of unique SKU/merchants
SELECT COUNT(DISTINCT stock_code) FROM retail;

-- Average invoice amount excluding cancelled orders
SELECT AVG(invoice_amount) as AVG
FROM (
  SELECT SUM(unit_price * quantity) as invoice_amount
  FROM retail
  GROUP BY invoice_no
  HAVING SUM(unit_price * quantity) > 0
) as subquery;

-- Total Revenue
SELECT SUM(unit_price * quantity) as sum
FROM retail;

-- Total Revenue by YYYYMM
SELECT CAST(TO_CHAR(invoice_date, 'YYYYMM') AS INTEGER) as yyyymm, 
SUM(unit_price * quantity) as sum
FROM retail
GROUP BY yyyymm
ORDER BY yyyymm;