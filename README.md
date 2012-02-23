# Nexpose JAVA API
_**This tool is made available to aid users in developing software that used the Nexpose API.**_

## Recommendations
- You must install the [JAVA SDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk-7u3-download-1501626.html) to use this tool
- It is highly recommended that you use and **IDE** (ie: [Eclipse](http://www.eclipse.org/downloads/), [IntelliJ](http://www.jetbrains.com/idea/download/) ... etc) when working with these APIs

## Resources
Use the following to assist in understanding the API's:  
- [Nexpose API guide version 1.1](http://download2.rapid7.com/download/NeXpose-v4/NeXpose_API_v1.1_Guide.pdf)  
- [Nexpose API guide version 1.2](http://download2.rapid7.com/download/NeXpose-v4/NeXpose_Extended_API_v1.2_Guide.pdf)

## Usage:

##### Login:

```java
...
// Create a URL that points to your nexpose instance.
URL url = new URL("https://<nexpose_netaddress>:<nexpose_port>");

// Create a session object
// NOTE: APISupportedVersion.XXX V1_0("1.0"), V1_1("1.1"), V1_2("1.2") These correspond the API version  
APISession session = new APISession(url, "xml", APISupportedVersion.V1_2, <username>, <password>));

// Now login
session.login(null);
...
```

##### Perform one or many operations:

```java
...   
see org/rapid7/nexpose/api/APISession.java for a list of supported API operations.   
...
```
   
##### Logout:

```java
...
session.logout(session.getSessionID(), null);
...
```   
   
## Examples
This tool comes with some examples located at:
**org/rapid7/nexpose/api/examples**


##### To run an example from the command line:
1. Compile: 
`javac org/rapid7/nexpose/api/examples/<Class_Name>.java`
   
2. Run with options:
`java org/rapid7/nexpose/api/examples/<Class_Name> <nexpose_netaddress> <port> <username> <password> <other options if needed>`

