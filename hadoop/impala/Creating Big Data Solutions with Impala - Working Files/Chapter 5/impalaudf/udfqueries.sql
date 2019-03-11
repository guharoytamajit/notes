-- Create the functions
CREATE FUNCTION has_vowels (string) returns boolean LOCATION '/user/hive/udfs/libudfsample.so' symbol='HasVowels';
CREATE FUNCTION count_vowels (string) returns int LOCATION '/user/hive/udfs/libudfsample.so' symbol='CountVowels';
CREATE FUNCTION strip_vowels (string) returns string LOCATION '/user/hive/udfs/libudfsample.so' symbol='StripVowels';

-- Check for vowels
SELECT has_vowels("bcdf");
SELECT has_vowels("abcdef");

-- Check for number of vowels
SELECT count_vowels("bcdf");
SELECT count_vowels("abcdef");

-- Strip vowels
SELECT strip_vowels("bcdf");
SELECT strip_vowels("abcdef");
