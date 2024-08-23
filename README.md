# Building and Deploying Application

### Prerequisites
- Docker

### Build and Run the Application
- Open your terminal window
- Download the application from GitHub
  ```
  git clone https://github.com/rajinda1980/fairbilling.git <br>
  Ex : root@Rajinda-D-L:~/Application$ git clone https://github.com/rajinda1980/fairbilling.git
  ```
- Navigate to the "fairbilling" root folder
  ```
  cd fairbilling/ <br>
  Ex : root@Rajinda-D-L:~/Application$ cd fairbilling/
  ```
- Run the following command to build and run the application
  ```
  ./build_and_run.sh <br>
  Ex : root@Rajinda-D-L:~/Application$ ./build_and_run.sh
  ```

### How to verify application is executed correctly
- An output.txt file should be generated in the root folder (i.e., "fairbilling")
  > root@Rajinda-D-L:~/Application/fairbilling$ ls -l <br>
  > total 24 <br>
  > -rwxrwxr-x 1 root root 1211 Aug 23 14:32 build_and_run.sh <br>
  > -rw-rw-r-- 1 root root   27 Aug 23 14:33 output.txt

***
### How to change the file paths
- To update the input log file path
  > Update INPUT_FILE_PATH value of build_and_run.sh file
- To update the output text file path
  > Update OUTPUT_FILE_PATH value of build_and_run.sh file

***
##### Useful commands
- Build the command using Maven
  ```
  mvn clean package
  ```
- Run the test cases
  ```
  mvn test
  ```

***
#### Information

1. Forty test cases are written to cover the complete functionality of the application <br>
2. An empty file should be generated if no records match the criteria. The criteria are that the record should contain time, username, and session state, each followed by a space <br>
   > 14:02:03 User1 Start <br>
   > 14:02:32 User2 End