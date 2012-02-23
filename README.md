## You must install the *JAVA SDK* to use this tool

### Simple Usage:

1. Login:
   ...
   // Create a URL that points to your nexpose instance.
   URL url = new URL("https://<nexpose_netaddress>:<nexpose_port>");
   
   // Create a session object
   // NOTE: APISupportedVersion.XXX V1_0("1.0"), V1_1("1.1"), V1_2("1.2") These correspond the API version  
   APISession session = new APISession(url, "xml", APISupportedVersion.V1_2, <username>, <password>));
   
   // Now login
   session.login(null);
   ...
   
2. Perform one or many operations:
   ...   
   see org/rapid7/nexpose/api/APISession.java for a list of supported API operations.   
   ...
   
3. Logout
   ...
   session.logout(session.getSessionID(), null);
   ...
   
   
### Examples of how to use the API can be found at:
*org/rapid7/nexpose/api/examples*

### To run an example from the command line:
1. Compile:
   javac org/rapid7/nexpose/api/examples/<Class_Name>.java
   
2. Run with options:
   java org/rapid7/nexpose/api/examples/<Class_Name> <nexpose_netaddress> <port> <username> <password> <other options if needed>


*** It is highly recommended that you use and IDE (ie: Eclipse, IntelliJ) when working with these APIs ***
