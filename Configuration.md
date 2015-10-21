# generate-sql #

| **Name** | **Type** | **Since** | **Description** |
|:---------|:---------|:----------|:----------------|
|syabseUser|String    |0.0.1      |The SYBASE user; **Required property**; **User property is:**sybaseproc.sybase.user|
|sybasePassword|String    |0.0.1      |The SYBASE password; **Required property**; **User property is:**sybaseproc.sybase.password|
|syabseDriverVersion|String    |0.0.1      |The SYBASE driver version; **Required property** |
|connectionString|String    |0.0.1      |The SYBASE connection string; **Required property** |
|proceduresToCheck|List      |0.0.1      |The list of procedures to check. The procedure name should be composed of database name followed by ".." and the procedure name|
|proceduresConfigFile|String    |0.0.1      |The procedures configuration file. The file should be a valid XML file. There is no strict file structure required. The only prerequisite concerns the configuration of the stored procedure which should be encapsulated between 

&lt;storedProcedureConfiguration&gt;

 tag and should contain 

&lt;name&gt;

 and 

&lt;baseName&gt;

 tags indicating procedure name and database name respectively. If this parameter is configured, the proceduresToCheck is ignored.|
|proceduresConfigFiles|String    |0.0.1      |Allows to specify more than one configuration file. See The procedures configuration file for more details.|
|outputDir |String    |0.0.1      |The path to put generated sources; **Required property** |
|htmlOutputFile|String    |0.0.1      |Write summary report HTML table to that file under "outputDir" directory; **Default value is:**report.html|
|deleteUnusedSQLFiles|boolean   |0.0.1      |If true, all SQL files that are in output directory and haven't were not identified will be deleted; **Default value is:**true|

# publish #

| **Name** | **Type** | **Since** | **Description** |
|:---------|:---------|:----------|:----------------|
|confluenceUser|String    |0.0.1      |The Confluence user; **Required property**; **User property is:**sybaseproc.confluence.user|
|confluencePassword|String    |0.0.1      |The Confluence password; **Required property**; **User property is:**sybaseproc.confluence.password|
|confluenceServer|String    |0.0.1      |The Confluence server address; **Required property** |
|confluencePageID|Object    |0.0.1      |The Confluence page to update. you may find the page ID at the very end of the URL of the page in edit mode; **Required property** |
|confluenceKeyWordForUpdate|String    |0.0.1      |The keyword on Confluence page under which the new content will be put. If none given, the content will be put at the top of the page|
|updateHeader|String    |0.0.1      |The Confluence header for updated content |
|outputDir |String    |0.0.1      |The path to put generated sources; **Required property** |
|htmlOutputFile|String    |0.0.1      |Reads summary report HTML table from that file under "outputDir" directory; **Default value is:**report.html|