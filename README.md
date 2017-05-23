# Crowd Jaas

Basic Crowd Jaas implementation WITHOUT external dependencies.

## Setup

### Copy crowd-jaas.jar 

`mkdir /opt/crowd-client/`

`cp target/crowd-jaas.jar /opt/crowd-client/` 

### Create crowd.conf

`vi /etc/tomcat7/crowd.conf`

```
Crowd {
 de.benjaminborbe.jaas.crowd.CrowdLoginModule required
 application.name="tomcat" application.password="xxx"
 crowd.base.url="http://127.0.0.1:18680/crowd/";
};
```

### Add /opt/crowd-client to common-loader

`vi /etc/tomcat7/catalina.properties`

```
common.loader="${catalina.base}/lib,${catalina.base}/lib/*.jar,${catalina.home}/lib,${catalina.home}/lib/*.jar,${catalina.home}/common/classes,${catalina.home}/common/*.jar,/opt/crowd-client,/opt/crowd-client/*.jar"
```

### Add java.security.auth.login.conf to JAVA_OPTS

`vi /etc/default/tomcat7`

`JAVA_OPTS="-Djava.security.auth.login.config=/etc/tomcat7/crowd.conf"`


### server.xml

```
<Realm className="org.apache.catalina.realm.LockOutRealm">
	<Realm className="org.apache.catalina.realm.CombinedRealm">
		<Realm className="org.apache.catalina.realm.UserDatabaseRealm" resourceName="UserDatabase"></Realm>
		<Realm className="org.apache.catalina.realm.JAASRealm" appName="Crowd" userClassNames="de.benjaminborbe.jaas.crowd.UserPrincipal" roleClassNames="de.benjaminborbe.jaas.crowd.RolePrincipal"></Realm>
	</Realm>
</Realm>
```

## Sample Curl

```
curl \
-u 'tomcat:xxx' \
-H 'Content-Type: application/xml' \
-d '<?xml version="1.0" encoding="UTF-8"?><password><value>xxx</value></password>' \
'http://127.0.0.1:8680/crowd/rest/usermanagement/latest/authentication?username=sampleuser'
```

```
<user expand="attributes" name="sampleuser">
  <link href="http://127.0.0.1:8680/crowd/rest/usermanagement/latest/user?username=sampleuser" rel="self"/>
  <first-name>Sample</first-name>
  <last-name>User</last-name>
  <display-name>Sample User</display-name>
  <email>sampleuser@example.com</email>
  <password>
    <link href="http://127.0.0.1:8680/crowd/rest/usermanagement/latest/user/password?username=sampleuser" rel="edit"/>
  </password>
  <key>32769:dbcced50-5a4d-4a86-8504-0da6e19ceed5</key>
  <created-date>2016-02-07T15:57:30Z</created-date>
  <updated-date>2017-05-22T16:39:44.090Z</updated-date>
  <active>true</active>
  <attributes>
    <attribute name="autoGroupsAdded">
      <link href="http://127.0.0.1:8680/crowd/rest/usermanagement/latest/user/attribute?username=sampleuser&amp;attributename=autoGroupsAdded" rel="self"/>
      <values>
        <value>true</value>
      </values>
    </attribute>
    <attribute name="invalidPasswordAttempts">
      <link href="http://127.0.0.1:8680/crowd/rest/usermanagement/latest/user/attribute?username=sampleuser&amp;attributename=invalidPasswordAttempts" rel="self"/>
      <values>
        <value>0</value>
      </values>
    </attribute>
    <attribute name="lastActive">
      <link href="http://127.0.0.1:8680/crowd/rest/usermanagement/latest/user/attribute?username=sampleuser&amp;attributename=lastActive" rel="self"/>
      <values>
        <value>1495547420473</value>
      </values>
    </attribute>
    <attribute name="lastAuthenticated">
      <link href="http://127.0.0.1:8680/crowd/rest/usermanagement/latest/user/attribute?username=sampleuser&amp;attributename=lastAuthenticated" rel="self"/>
      <values>
        <value>1495547583277</value>
      </values>
    </attribute>
    <attribute name="passwordLastChanged">
      <link href="http://127.0.0.1:8680/crowd/rest/usermanagement/latest/user/attribute?username=sampleuser&amp;attributename=passwordLastChanged" rel="self"/>
      <values>
        <value>1495471184090</value>
      </values>
    </attribute>
    <attribute name="requiresPasswordChange">
      <link href="http://127.0.0.1:8680/crowd/rest/usermanagement/latest/user/attribute?username=sampleuser&amp;attributename=requiresPasswordChange" rel="self"/>
      <values>
        <value>false</value>
      </values>
    </attribute>
    <link href="http://127.0.0.1:8680/crowd/rest/usermanagement/latest/user/attribute?username=sampleuser" rel="self"/>
  </attributes>
</user>
```

## Continuous integration

[Jenkins](https://www.benjamin-borbe.de/jenkins/job/Crowd-Jaas/)

## Copyright and license

    Copyright (c) 2016, Benjamin Borbe <bborbe@rocketnews.de>
    All rights reserved.
    
    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions are
    met:
    
       * Redistributions of source code must retain the above copyright
         notice, this list of conditions and the following disclaimer.
       * Redistributions in binary form must reproduce the above
         copyright notice, this list of conditions and the following
         disclaimer in the documentation and/or other materials provided
         with the distribution.

    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
    "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
    LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
    A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
    OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
    SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
    LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
    DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
    THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
    (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
    OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
