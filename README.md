# Nexpose JAVA API
_**This tool is made available to aid users in developing software that uses the Nexpose API.**_  
_**This software is not officially supported by Rapid7 and is made available for the community without warranty**_

## Recommendations
- You must install the [JAVA SDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk-7u3-download-1501626.html) to use this tool
- It is highly recommended that you use and **IDE** (ie: [Eclipse](http://www.eclipse.org/downloads/), [IntelliJ](http://www.jetbrains.com/idea/download/) ... etc) when working with these APIs

## Resources
Use the following to assist in understanding the API's:  
- [Nexpose API guide](https://community.rapid7.com/docs/DOC-1896)

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
// see org/rapid7/nexpose/api/APISession.java for a list of supported API operations.   
// Example: The following will print out all the asset groups and their associated risk
List<AssetGroupSummary> assetGroups = (List<AssetGroupSummary>)session.listAssetGroups(session.getSessionID(), null);
for (AssetGroupSummary assetGroup : assetGroups)
{
   System.out.println("************************************");
   System.out.println("Name: " + assetGroup.getName());
   System.out.println("Risk: " + assetGroup.getRiskScore());
   System.out.println("************************************");
}
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

