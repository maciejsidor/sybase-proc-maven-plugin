# Introduction #

sybase-proc-maven-plugin allows to generate sources of SYBASE stored procedures so the generated files might then be versioned together with project code.

This plugin allows to generate sources for selected procedures as well as all sub-procedures that are being called by "exec" command.

Additionally, the stored procedures configuration might be publish automatically to CONFLUENCE.

# Tasks #

The functionality of sybase-proc-maven-plugin might be summarized in following tasks:

  1. Parsing of stored procedure configuration file (if one has been given)
  1. Establishing SYBASE connection
  1. Retrieving stored procedures and all sub-procedures
  1. Writing HTML report table summarizing the basic infos of all  procedures and all identified sub-procedures
  1. Writing SQL scripts for all stored procedures and all sub-procedures found
  1. Deleting files that were not concerned by the generation (optional)
  1. Updating confluence page with summary report HTML table with all procedures that have been identified and dependencies among them (optional)

## Retrieving stored procedures and all sub-procedures ##
Detecting sub-procedure calls is based on procedure body parsing (scanning for "exec" keyword). The commented calls are however ignored.

## Writing SQL scripts ##
The SQL scripts are written-out only if the SQL file for procedure doesn't exist yet or if the procedure body has been changed against the existing SQL file. This allows to avoid uncommitted changes issues during project release.

## Writing summary report  HTML table ##
The table row is composed of 4 columns:
  * Nested level (0 for top level procedure)
  * Procedure name
  * Database name
  * Compilation date

## Updating confluence page ##
The content is put under the under the given keyword (or at the top of page in none given nor found).

Optionally, the update header might be specified.