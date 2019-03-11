7-- Create the table
CREATE TABLE IF NOT EXISTS policecalls (
  priority STRING,
  call_type STRING,
  jurisdiction STRING,
  dispatch_area STRING,
  received_date STRING,
  received_time BIGINT,
  dispatch_time BIGINT,
  arrival_time BIGINT,
  cleared_time BIGINT,
  disposition STRING
)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ',';

-- Load the data
LOAD DATA LOCAL INPATH '/home/vmuser/host/repos/Hadoop/vm/datasets/rpd_data_all.csv' 
OVERWRITE INTO TABLE policecalls;

-- Verify the data
SELECT * FROM policecalls LIMIT 1;

-- Describe the table
DESCRIBE policecalls;

-- All emergency calls
SELECT * FROM policecalls
WHERE priority = "E";

-- All priority 1 calls on 12/31/2013
SELECT * FROM policecalls
WHERE priority = "1" AND received_date = " 12/31/2013";

-- First 10 calls for
SELECT * FROM policecalls
WHERE received_date = " 03/20/2013"
LIMIT 10;

-- Advanced Queries
SELECT * FROM policecalls
WHERE TRIM(received_date) = "03/20/2013"
LIMIT 10;

SELECT received_date, 
CAST(unix_timestamp(received_date,'dd/mm/yyyy') as TIMESTAMP) as received_date_unix
FROM policecalls
LIMIT 10;