sventon is a Subversion web client for Java Servlet containers.

A Java SE 6 runtime and a servlet 2.4/JSP 2.0 compliant container is
required for deployment, thus Tomcat 6.0 is a good container companion.
You'll also need a Subversion server set up and configured with a
repository. For info on Subversion, see http://subversion.apache.org

Starting with version 2.5.0 both JavaHL and SVNKit are supported as drivers to
connect to Subversion. Using JavaHL requires that Subversion binaries including
JavaHL bindings are installed on the system. SVNKit requires has no dependencies
outside the war. Note that you will have to build the JavaHL version yourself
from the source code. See http://wiki.sventon.org/index.php?n=Main.Building
for details

The JHighlight library from https://jhighlight.dev.java.net is used for
syntax highlighting.
Currently the library supports colorization of a number of file formats, including:
* HTML/XHTML
* Java
* Groovy
* C++
* XML
* LZX
* RIFE (http://rifers.org)

Installation:
1. Download and unzip the file. 
2. Put the war-file in the appropriate application server directory,
   on a standard Tomcat installation this is in 'webapps'.
3. Point your browser to http://<host:port>/svn
4. Enter and submit basic configuration data.

If the repository cache has been enabled during setup, the background
cache/indexing process of the revisions and the files in HEAD will start.
If you have a repository with many files (and/or tags or branches),
this can take quite some time. You will be able to use sventon during the
indexing process, but the search and flatten buttons will be disabled until
indexing is completed.

For non-US-ASCII charsets to work correctly with Tomcat, the Tomcat connector
attribute "URIEncoding" should be set to "UTF-8", or alternatively the attribute 
"useBodyEncodingForURI" should be set to "true"

Upgrading instructions
See upgrade.txt

Documentation and FAQ
See http://wiki.sventon.org

Source
If the sventon source was not included in the download, you can fetch it
from http://www.sventon.org

License
License files are included in the download, read them carefully.

Many thanks to the developers of the libraries that sventon depends on,
and to the developers of Subversion and TortoiseSVN!

Enjoy!

sventon project - http://www.sventon.org
