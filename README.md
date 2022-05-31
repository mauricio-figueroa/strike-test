# Strike filesystem test

## Description
The application contains the logic of a distributed filesystem, with a small authentication system and roles-permissions.

It allows us to create a user, authenticate it, upload a file, download it, delete it, share it and unshare it, among other functionalities.

## Technologies
* JAVA 11
* SpringBoot
* Gradle
* H2 (in memory database)

## Run app
Execute
`gradle bootRun` in terminal

## Documentation


### DER
![](documentation/DER-Strike.png)

The application starts on port 8080 and the in-memory database can be accessed by going to 'http://localhost:8080/h2-console/login.jsp'. 
Credentials are:

- Username: sa
- Password: password

![](documentation/h2.png)

Entering here you can see the structure of the database.


### Considerations
- A file is related to its owner directly, but it is also related to all users who have permissions on it. The exercise did not specify the type of access, so they were not typed.
- There are only 2 public endpoints, the rest of the endpoints needs to authenticate with the 'Authorization: Bearer' header.
- In order to show the testing abilities I tested the 'FileServiceImp' class. The other classes were not tested because of the time it took to do the exercise.
- Inside the documentation folder, a collection of postman is attached to test the operation of the application. (Path /documentation/strike-postman.json).
- In order not to corrupt the files, only the name can be changed, the original extension will always be respected.
- It-s an in-memory database so as not to have external dependency to initialize the project. This means that every time we start the application, the database is cleaned.

## how to consume the services of the application

### Register
<pre><code>
curl --location --request POST 'localhost:8080/public/user/register' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=3062975F6EDB52547A9476608393E950' \
--data-raw '{
    "username":"john",
    "password":"smith"
}'
</code></pre>

### Login
<pre><code>
curl --location --request POST 'localhost:8080/public/user/login' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWyiwuVrJSyk8uyS9ILUpT0lHKTCxRsjI0MzW2NDI0M7TQUUqtKAALmBgYWBiABEqLU4vyEnNTgfqy8jPyjJRqAVRuLVFHAAAA.TWHxddqXqUA1A1Fc6K4_wzzitC_P9nAGvLlIv7_fIyg' \
--header 'Cookie: JSESSIONID=3062975F6EDB52547A9476608393E950' \
--data-raw '{
    "username":"john",
    "password":"smith"
}'
</code></pre>

### Logout
<pre><code>
curl --location --request GET 'localhost:8080/user/logout' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWyiwuVrJSyk8uyS9ILUpT0lHKTCxRsjI0MzW2NDM1NDHUUUqtKAALmBiYGpqCBEqLU4vyEnNTgfqy8jPylGoBygxwfkYAAAA.O4QYEYe9QBP-jQ-abZ4hC4yI_oiOP1gHGmqHUx91qpo'
</code></pre>

### Upload File
<pre><code>
curl --location --request PUT 'localhost:8080/file/1' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWyiwuVrJSyk8uyS9ILUpT0lHKTCxRsjI0MzW2NDM3NzTUUUqtKAALmBiYmhiCBEqLU4vyEnNTgfqy8jPylGoBeU5aG0YAAAA.HAgjw4N0k1kPKeWTJZv1M9qYqNyUSTegwXAeIYvS-hE' \
--data-raw '{
    "new_name":"saraza"
}'
</code></pre>

### Download File
<pre><code>
curl --location --request GET 'localhost:8080/file/1' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWyiwuVrJSyk8uyS9ILUpT0lHKTCxRsjI0MzW2NDYxMLHQUUqtKAALmBgYGZiABEqLU4vyEnNTgfqy8jPylGoBdaW_eEYAAAA.QVbx2rBRP-F-A4pjCNyP_fw0LvbtwLEJu27Uq0xGMhU'
</code></pre>

### Delete File
<pre><code>
curl --location --request DELETE 'localhost:8080/file/2' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWyiwuVrJSyk8uyS9ILUpT0lHKTCxRsjI0MzW2sDA2tbDQUUqtKIAIWJpZWoIESotTi_ISc1OB-rLyM_KUagHMTsnIRgAAAA.ob6V5JdRD1K6ErJ_Ns3gTvd2XArZ4fR0NGQNtY3ajCM'
</code></pre>

### Share File
<pre><code>
curl --location --request POST 'localhost:8080/file/share' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWyiwuVrJSyk8uyS9ILUpT0lHKTCxRsjI0MzW2NDYxtTDXUUqtKAALmBgYGViCBEqLU4vyEnNTgfqy8jPylGoBael4akYAAAA.G9QWe4wxrvhWGGKZ7YI-Znq8VxUMfpGc388X6h61bb8' \
--data-raw '{
    "file_id":1,
    "usernames":["user_1"]
}'
</code></pre>

### Un share File
<pre><code>
curl --location --request POST 'localhost:8080/file/unshare' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWyiwuVrJSyk8uyS9ILUpT0lHKTCxRsjI0MzW2NDYxtTDXUUqtKAALmBgYGViCBEqLU4vyEnNTgfqy8jPylGoBael4akYAAAA.G9QWe4wxrvhWGGKZ7YI-Znq8VxUMfpGc388X6h61bb8' \
--data-raw '{
    "file_id":1,
    "usernames":["user_1","user_3"]
}'
</code></pre>

### Get All Metadata
<pre><code>
curl --location --request GET 'localhost:8080/file/metadata' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWyiwuVrJSyk8uyS9ILUpT0lHKTCxRsjI0MzW2NDY2MDLQUUqtKAALmBgYWpqABEqLU4vyEnNTgfqy8jPylGoBPCjGbUYAAAA.iqZWrfIIHKW4I5VHgi70Ve1jnnKfs-Q2uPN_OkZdWmM' \
--data-raw '{
    "new_name":"test"
}'
</code></pre>

### Modify File Name
<pre><code>
curl --location --request PUT 'localhost:8080/file/1' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWyiwuVrJSyk8uyS9ILUpT0lHKTCxRsjI0MzW2NDM1NDHUUUqtKAALmBiYGpqCBEqLU4vyEnNTgfqy8jPylGoBygxwfkYAAAA.O4QYEYe9QBP-jQ-abZ4hC4yI_oiOP1gHGmqHUx91qpo' \
--data-raw '{
    "new_name":"test"
}'
</code></pre>