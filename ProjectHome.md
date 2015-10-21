# SYBASE PROCEDURES MAVEN PLUGIN #

Maven plugin that allows to generate sources of SYBASE stored procedures so the generated files might then be versioned together with project code.

This plugin allows to generate sources for selected procedures as well as all sub-procedures that are being called by "exec" command.

Additionally, the stored procedures configuration might be publish automatically to CONFLUENCE.

**Please visit [our WIKI page](https://code.google.com/p/sybase-proc-maven-plugin/w/list) for more informations.**

# Dependency #

Check the example of sybase-proc-maven-plugin configuration in pom.xml

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