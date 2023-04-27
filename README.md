# Data-migration-from-mongo-to-postgres

Some strings with specific characters(ex: \u0000) can be stored in mongo's string type but cannot stored in postgres's string type.
Storing such string directly in postgres may have such error: "\u0000 cannot be converted to text postgres",
so we need to transfer such string into binary data first, then stored data to postgres.

## What it do
It's a tool for data migration from mongo to postgres, it solve the problem when storing some strings with specific character(ex: \u0000) by transfering such strings into binary data first, then stored data to postgres.  
ex: collection "Collection1" with two keys(str1, str2) need to be stored in binary data  
&emsp;Below is the data in collection "Collection1":  
&emsp;![image](https://user-images.githubusercontent.com/37506309/232669007-1f7d9f7a-5192-470c-a127-8f284404085c.png)  
&emsp;Below is the result after migration in postgres:  
&emsp;![image](https://user-images.githubusercontent.com/37506309/232693748-bddc2e16-3490-4015-9202-c537ad733f6d.png)




&emsp;Two keys(str1, str2) need to be stored in binary data is splitting from document

## How to use
1. Modify database connection information in MongoConnectionInf.json & PostgresConnectionInf.json.
2. Modify file paths of MongoConnectionInf.json & PostgresConnectionInf.json in launch.json.
3. Set TableWithBinarydataObj in DatamigrationApplication.java:  
ex: collection "Collection1" with two keys(str1, str2) need to be stored in binary data  
&emsp;first: create tableWithBinarydataObj: 
```java
TableWithBinarydataObj tableWithBinarydataObj = new TableWithBinarydataObj();  
String[] binaryDataColumns = { "str1", "str2" };  
tableWithBinarydataObj.setTableName("Collection1");  
tableWithBinarydataObj.setBinaryDataColumns(binaryDataColumns);
```  
&emsp;&emsp;&emsp;second: add tableWithBinarydataObj to list:  
```java
tableWithBinarydataObjList.add(tableWithBinarydataObj);
```
4. run DatamigrationApplication.java
