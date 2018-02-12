# aem-restful-api
A simple REST client API for use with AEM to quickly, and easily integrate with external restful APIs

About me: https://www.linkedin.com/in/brandonmaynard/
- **Java8 is required** and defined in the parent (root) POM (mainly for Base64 Encoding for Basic Authentication purposes). If you are using a 
Java version under 8, the build **will** fail.
- This was developed and tested using AEM 6.0; it should work on AEM 6.1+, but no promises

# Documentation
TBD

# Installation
- Make sure AEM is up and running
- **mvn clean install** on the parent (root) POM
- Configure each integration's component with proper credentials in the Felix (OSGi) Console
- Once installed and configured, navigate to **/content/restful** within the SiteAdmin to see the supplied example pages

_* There is only a dev profile set up for this project - modify the POM to create other profiles as needed_

# History
**2018/02/11 - v1.0.0** Initial commit: Base client, OAUTH client, Simple client, Ebay connector OSGi component, SalesForce connector OSGi 
component, Wunderground connector OSGi component, sample components, templates, design, sample content, AngularJS clientlib, and supporting 
sling servlets.
