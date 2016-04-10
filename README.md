# wovnjava

This is WOVN backend for Java, which is implemented as a servlet filter.

## Installation

### Maven

#### 1. Add the JitPack repository to your project's pom.xml.

```XML
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
  
  <!-- These lines are not needed if you do not use SNAPSHOT version. -->
  <snapshots>
    <enabled>true</enabled>
    <updatePolicy>always</updatePolicy>
  </snapshots>
  <!-- end -->
  
</repositories>
```

#### 2. Add the dependency to your project's pom.xml.

```XML
<dependency>
  <groupId>com.github.wovnio</groupId>
  <artifactId>wovnjava</artifactId>
  <!-- set the version of wovnjava you use. -->
  <version>-SNAPSHOT</version>
</dependency>
```

You can see all available versions of wovnjava [here](https://jitpack.io/#wovnio/wovnjava).

#### Step 3. Add the settings of wovnjava to your servlet's web.xml.

```XML
<filter>
  <filter-name>wovn</filter-name>
  <filter-class>com.github.wovnio.wovnjava.WovnServletFilter</filter-class>
  <init-param>
    <param-name>userToken</param-name>
    <!-- set your user token. -->
    <param-value>2Wle3</param-value>
  </init-param>
  <init-param>
    <param-name>secretKey</param-name>
    <param-value>secret</param-value>
  </init-param>
</filter>

<filter-mapping>
  <filter-name>wovn</filter-name>
  <!-- set filter URL pattern you use wovnjava. -->
  <url-pattern>/*</url-pattern>
</filter-mapping>
```

## Parameters
