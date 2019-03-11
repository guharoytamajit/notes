-- Create the jurisdictions table
CREATE TABLE IF NOT EXISTS jurisdictions (
  jurisdiction STRING,
  description STRING
)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ',';

-- Load the data
LOAD DATA INPATH '/user/vmuser/jurisdictions.csv' 
OVERWRITE INTO TABLE jurisdictions;

-- Explicit join
SELECT *
FROM policecalls
JOIN jurisdictions ON policecalls.jurisdiction = jurisdictions.jurisdiction
LIMIT 10;

-- Implicit join
SELECT *
FROM policecalls, jurisdictions
WHERE policecalls.jurisdiction = jurisdictions.jurisdiction
LIMIT 10;

-- No jurisdictions
SELECT *
FROM policecalls
LEFT JOIN jurisdictions ON policecalls.jurisdiction = jurisdictions.jurisdiction
WHERE jurisdictions.description IS NULL
LIMIT 10;

-- Count of total calls
SELECT COUNT(*) AS totalcalls
FROM policecalls;

-- Count of calls in 2013
SELECT COUNT(*) AS totalcalls
FROM policecalls
WHERE received_date LIKE "%2013";

-- Count of no jurisdiction
SELECT COUNT(*) AS nojurisdiction
FROM policecalls
LEFT JOIN jurisdictions ON policecalls.jurisdiction = jurisdictions.jurisdiction
WHERE jurisdictions.description IS NULL;

-- Count of calls by priority
SELECT priority, COUNT(*) AS totalcalls
FROM policecalls
GROUP BY priority;

-- Sorted count of calls by priority
SELECT priority, COUNT(*) AS totalcalls
FROM policecalls
GROUP BY priority
ORDER BY totalcalls DESC;

-- Advanced Queries
-- No jurisdiction as a percentage
SELECT nojurisdictions.nojurisdiction, totalstable.totalcalls, 
  ((nojurisdictions.nojurisdiction / totalstable.totalcalls) * 100) AS percentage
FROM  
  (SELECT COUNT(*) AS nojurisdiction, 1 as joiner
  FROM policecalls
  LEFT JOIN jurisdictions ON policecalls.jurisdiction = jurisdictions.jurisdiction
  WHERE jurisdictions.description IS NULL) nojurisdictions 
FULL OUTER JOIN
  (SELECT count(*) AS totalcalls, 1 as joiner
  FROM policecalls) totalstable
ON nojurisdictions.joiner = totalstable.joiner;

-- Calls per year
SELECT callyear, COUNT(*) as callsperyear
FROM
  (SELECT year(from_unixtime(unix_timestamp(received_date,' MM/dd/yyyy'))) as callyear
  FROM policecalls) callyears
GROUP BY callyear;