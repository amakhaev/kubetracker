## Kubernetes tracker

#### How to run
* Initialize sqlite database
  * Create file named by `<your_database_name>.db3` in the root folder
  * Apply sql script to created database using any way. For example open terminal then put command 
  ```
  1. sqlite3 <your_database_name>.db3
  2. When connection established then put: .read ./src/main/resources/db/v1.0.sql
  ```
* Add config file to the root folder
  * Create file `config.properties`
  * Add content
  ```
  auth_url: <url_for_token_getting>
  master_url: <kubernetes_master_url>
  client_secret=<client_secret>
  client_id=<client_id>
  db: <your_database_name>.db3
  ```
