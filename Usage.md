# Maven repository #

The sybase-proc-maven-plugin is hosted on OSS SONATYPE repository, thus, in order to use it in your maven project you must add the OSS SONATYPE repository configuration to your pom.xml file.

Add following code to your pom.xml:

```
<repositories>
  <repository>
    <id>sonatype-oss-public</id>
    <url>https://oss.sonatype.org/content/groups/public/</url>
    <releases>
      <enabled>true</enabled>
    </releases>
    <snapshots>
      <enabled>true</enabled>
    </snapshots>
  </repository>
</repositories>
```

# Downloads #

To see all available releases downloads visit [OSS SONATYPE repository for sybase-proc-maven-plugin](https://oss.sonatype.org/content/groups/public/com/googlecode/msidor/maven/plugins/sybase-proc-maven-plugin/).

# jConnect Dependency #

The sybase-proc-maven-plugin depends on jConnect SYBASE driver.
However the jConnect driver is not included in the project dependencies nor is available from any public maven repository (as for today).

Thus, the jConnect must be explictly configured in sybase-proc-maven-plugin configuration.

If you are interested in this plugin, it is very likely that your project uses already the jConnect driver. In that case, copy the jConnect dependency configuration to the sybase-proc-maven-plugin configuration.

The following example shows basic configuration of sybase-proc-maven-plugin:
```
TODO: Add example
```

If not, you will have to download and install the jConnect driver to your local repository.

## Installing jConnect driver ##

Steps for downloading jConnect

  1. Log in to the [Sybase Software Download Web site](http://downloads.sybase.com/).
  1. The product list page appears.
  1. Select jConnect for JDBC (you might need to select "in all months" in product filter settings to JConnect appears).
  1. The latest versions of jConnect for JDBC appears.
  1. Select the jConnect for JDBC software that you need.
  1. Read the Sybase End User License Agreement. Select I Agree.
  1. Read the Export Control Restrictions. Select I Agree.
  1. Click Continue.
  1. The product download page lists the software package and the EBF-related documentation for jConnect.
  1. Download and uncompress the jConnect software package.
  1. Launch the jConnect software package installer.
  1. Go to the installation directory where your JConnect jar file is.
  1. install the JConnect driver in your maven local repository by executing the following command in CMD:

```
mvn install:install-file -Dfile=jconn4.jar -DgroupId=jconnect -DartifactId=jconn4 -Dversion=ebf20686 -Dpackaging=jar
```

# Goals #

The sybase-proc-maven-plugin provides two goals :

## generate-sql ##

This goal will generate SQL scripts and the HTML report table. To run this goal execute the following command in your project:

```
mvn sybase-proc:generate-sql -Dsybaseproc.sybase.user=sybaseuser -Dsybaseproc.sybase.password=sybasepassword
```

## publish ##

This goal will publish the previously generated HTML report table to CONFLUENCE. To run this goal execute the following command in your project:

```
mvn sybase-proc:publish -Dsybaseproc.confluence.user=confluenceuser -Dsybaseproc.confluence.password=confluencepassword
```

# Project applications #
The reason why the functionality of this plugin is spread into two goals is to adapt it to the project lifecycle. You may, for instance, attach the generate-sql goal to the generate-sources phase. This way, whenever you will launch the project build via maven, you will always have the latest SQL files in your project.

It might not be wanted hoverer to publish the list of identified stored procedures at each build (for instance for SNAPSHOT releases). Thus the publish goal must be executed separately.

Probably, the best moment to publish the stored procedures list would be the post-release phase. You might, for instance, execute the publish goal on the checkout repository produced by release:perform goal.