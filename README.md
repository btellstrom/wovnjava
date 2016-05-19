# wovnjava

This is WOVN backend for Java, which is implemented as a servlet filter.

## 1. Installation

### 1.1. Maven

#### 1.1.1. Add the JitPack repository to your project's pom.xml.

```XML
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
    <!-- These lines are not needed if you do not use SNAPSHOT version. -->
    <snapshots>
      <enabled>true</enabled>
      <updatePolicy>always</updatePolicy>
    </snapshots>
    <!-- end -->
  </repository>
  
</repositories>
```

#### 1.1.2. Add the dependency to your project's pom.xml.

```XML
<dependency>
  <groupId>com.github.wovnio</groupId>
  <artifactId>wovnjava</artifactId>
  <!-- set the version of wovnjava you use. -->
  <!-- if you want to use development version of wovnjava, please set "-SNAPSHOT" here. -->
  <version>0.1.0</version>
</dependency>
```

You can see all available versions of wovnjava [here](https://jitpack.io/#wovnio/wovnjava).

#### 1.1.3. Add the settings of wovnjava to your servlet's web.xml.

```XML
<filter>
  <filter-name>wovn</filter-name>
  <filter-class>com.github.wovnio.wovnjava.WovnServletFilter</filter-class>
  <init-param>
    <param-name>userToken</param-name>
    <param-value>2Wle3</param-value><!-- set your user token. -->
  </init-param>
  <init-param>
    <param-name>secretKey</param-name>
    <param-value>secret</param-value>
  </init-param>
</filter>

<filter-mapping>
  <filter-name>wovn</filter-name>
  <url-pattern>/*</url-pattern><!-- set filter URL pattern you use wovnjava. -->
</filter-mapping>
```

## 2. Parameter setting

wovnjava's valid parameters are as follows.

Parameter Name            | Required | Default Setting
------------------------- | -------- | ------------
userToken                 | yes      | ''
secretKey                 | yes      | ''
urlPattern                | yes      | 'path'
query                     |          | ''
defaultLang               | yes      | 'en'
useProxy                  |          | 'false'
debugMode                 |          | '0'
originalUrlHeader         |          | ''
originalQueryStringHeader |          | ''

### 2.1. userToken

Set your WOVN.io Account's user token. This parameter is required.

### 2.2. secretKey

This parameter is in development; it is not currently used. However, it is a required parameter, so ensure to set a value for it.

### 2.3. urlPattern

The Library works in the Java Application by adding new URL's to be translated. You can set the type of url with the url_pattern parameter. There are 3 types that can be set.

parameters  | Translated page's URL           | Notes
----------- | ------------------------------- | ------
'path'      | https://wovn.io/ja/contact      | Default Value. If no settings have been set, url_pattern defaults to this value.
'subdomain' | https://ja.wovn.io/contact      | DNS settings must be set.
'query'     | https://wovn.io/contact?wovn=ja | The least amount of changes to the application required to complete setup.

※ The following is an example of a URL that has been translated by the library using the above URL's.

    https://wovn.io/contact

### 2.4. query

WOVN.io ignores query parameters when searching translated page. If you want to add query parameter to translated page's URL, you should configure "query" parameter. (You need to configure WOVN.io too)

    https://wovn.io/ja/contact?os=mac&keyboard=us

If the defualt_lang is 'en', and the query is , the above URL will be modified into the following URL to search for the page's translation.

    https://wovn.io/contact

If the default_lang is 'en', and the query is set to 'mac', the above URL will be modified into the following URL to search for the page's translation.

    https://wovn.io/contact?os=mac

### 2.5. defaultLang

This sets the Java application's default language. The default value is english ('en').

If a requested page, where the default language's parameter is included in the URL, the request is redirected before translating. The default_lang parameter is used for this purpose.

If the default_lang is set to 'en', when receiving a request for the following URL,

    https://wovn.io/en/contact

The library will redirect to the following URL.

    https://wovn.io/contact"

### 2.6. useProxy

There is some case that wovnjava with reverse proxy cannot get translated data. When useProxy setting is true, wovnjava uses X-Forwarded-Host header for getting translated data.

### 2.7. debugMode

When debugMode is 1, wovnjava outputs debug logs. This setting is for development.

### 2.8. originalUrlHeader, originalQueryStringHeader

When you are using mod_rewrite module of Apache HTTP server and it is rewriting Request URL, wovnjava receives URL after rewriting and it sometimes cannot get correct translation data from API server.

If you configure originalUrlHeader and originalQueryStringHeader in your Application setting file, wovnjava uses request header values set in originalUrlHeader and originalQueryStringHeader.

In following setting of Apache HTTP server, if you configure URL before rewriting in request headers,

```
SetEnvIf Request_URI "^(.*)$" REQUEST_URI=$1
RequestHeader set X-Request-Uri "%{REQUEST_URI}e"
RewriteRule .* - [E=REQUEST_QUERY_STRING:%{QUERY_STRING}]
RequestHeader set X-Query-String "%{REQUEST_QUERY_STRING}e"
```

wovnjava uses URL before rewriting with following setting, and can get correct translation data from API server.

```XML
<filter>
  ...
  <init-param>
    <param-name>originalUrlHeader</param-name>
    <param-value>X-Request-Uri</param-value>
  </init-param>
  <init-param>
    <param-name>originalQueryStringHeader</param-name>
    <param-value>X-Query-String</param-value>
  </init-param>
  ...
</filter>
```
※ Above sample of request header setting is referred from following site.

https://coderwall.com/p/jhkw7w/passing-request-uri-into-request-header
