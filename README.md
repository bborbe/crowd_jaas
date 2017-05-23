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


## server.xml

```
<Realm className="org.apache.catalina.realm.LockOutRealm">
	<Realm className="org.apache.catalina.realm.CombinedRealm">
		<Realm className="org.apache.catalina.realm.UserDatabaseRealm" resourceName="UserDatabase"></Realm>
		<Realm className="org.apache.catalina.realm.JAASRealm" appName="Crowd" userClassNames="de.benjaminborbe.jaas.crowd.UserPrincipal" roleClassNames="de.benjaminborbe.jaas.crowd.RolePrincipal"></Realm>
	</Realm>
</Realm>
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
