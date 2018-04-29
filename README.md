# Web-Server
An implementation of multi-threaded web server (file-based) with thread-pooling and support to HTTP/1.1 keep-alive in Java. This project is delivered as the first step of Adobe recruitment process.

## Explanation
The working flow:
1. `Main` class initiated the `HttpServer`.
2. `HttpServer` bind the TCP server socket and handle (`HttpHandler`) new incoming connection (request) per-thread in a new thread or in an existing pooled thread.
3. `HttpHandler` handle incoming stream of input, parse it (`HttpRequestParser`) and write appropriate string of response (`HttpResponse`) to stream of output.
4. `HttpHeader` and `HttpStatus` contains builder of HTTP's header responses and mapping of HTTP's response status.

### Web Directory
`WebDirectory` is the path to the directory containing the static html files. Ensure to pass this path when executing the application. By default, the web-server will be able to serve all html files inside the `WebDirectory`. For example, file located in "`(webDirectory)/havefun.html`" can be viewed on `localhost:<port>/havefun.html`. If the client try to access unexisted files, by default the server will return `notfound.html` file located in `webDirectory` alongside with 404 status code.

### HTTP/1.1 Keep-alive (Persistent Connection)
To use the persistent connection, pass `Keep-alive: <time_in_seconds>` to the HTTP request header.

### HTTP Request Parser
This code is taken from here: [http://www.java2s.com/Code/Java/Network-Protocol/HttpParser.htm](http://www.java2s.com/Code/Java/Network-Protocol/HttpParser.htm). Aside from the class name, nothing has been changed.

## Usage

### Prerequisites
1. Install [apache maven](https://maven.apache.org/).
2. Install [Java 8](https://java.com/en/download/).
3. Export maven to your environment:
```bash
export PATH=$PATH:/path/to/maven/bin
```
4. Clone this repository.
5. Go inside to directory:
```bash
cd web-server
```

### Running the Application
```bash
mvn exec:java -Dexec.args="<PORT> <path_to_web_directory>"
```

### Running the Test
```bash
mvn test
```

### Building the JAR Package
```bash
mvn package
```

### Running the JAR Package
```bash
java -jar <jar_file> "<PORT>" "<path_to_web_directory>"
```

## License
GNU GPL-3.0.