## Open pricing tracker

#### Pre requirement
* Initialize sqlite database
  * Create file named by `<your_database_name>.db3` in the root folder
  * Apply sql script to created database using any way. For example open terminal then put commands:
   ```
  > sqlite3 <your_database_name>.db3 
  > .read ./src/main/resources/db/v1.0.sql
  > .read ./src/main/resources/db/v1.1.sql
  > .read ./src/main/resources/db/v1.3.sql
  ```
* Add config file to the `resources` folder
  * Create file `application.yml`
  * Add content
  ```
  auth: 
    url: <url_to_get_token> 
  kube: 
    master_url: <kubernetes_master_url> 
    client_secret: <kubernetes_client_secret> 
    client_id: <kubernetes_client_id> 
  db: 
    name: <path_to_sqlite_database> 
  jenkins:
    dev_smoke_test_url: <url>
    dev_e2e_test_url: <url>
    qa_smoke_test_url: <url>
    qa_e2e_test_url: <url>
    plan_folder_url: <url>
  server: 
    port: 6547
  ```
#### Build & Run

##### Spring application
* Execute the commands from root folder:
```
> gradlew assemble
> java -jar build/libs/openPricingTracker-<latest_version>.jar 
```

##### Client
* Navigate to `src/main/webapp/open-pricing-tracker`
* If it first running then need to install node modules, execute `npm install`
* Execute the command `npm start`

#### Set up application
After application ran navigate to settings page then enter your credential that will be used to retrieve authorization 
token. Then initialization of tracker completed.