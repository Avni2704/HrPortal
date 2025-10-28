# 🚀 SELENIUM + CUCUMBER + ALLURE  🧮

## 📌 <ins> Plugins </ins>
1. Go to Settings -> Plugins
2. Install Cucumber

## 📂 <ins> FOLDERS </ins>
### _/drivers_
 1. Holds all the common functions. 

### _/features_
 1. Holds all the BDD Feature files.

### _/hooks_
 1. Open Browser / Close browser
 2. Before / After step & scenarios

### _/qa.utils_
 1. ExcelReader - to read data from Excel and store in memory
 2. Variable - store all variables as common variables

### _/steps_
 1. Holds all the step definitions. 

### _/testRunner_
 1. Main class that runs the entire project. 
```
 features = {"src/test/java/features/EmployeeLogin.feature"}, //declare the feature file names
 dryRun = true, //to get step definition function. & //false = to let it run
 glue = { "steps", "hooks" }, //bring together of the files
 snippets = CucumberOptions.SnippetType.CAMELCASE, //function names
 monochrome = true,
 tags = "@IHP-61-003", //to specify which sceanrio to run
 plugin = { "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm" }) //report
```
## ▶️ <ins> RUN </ins>
### 1. Update the TestRunner.java. //tags or feature files
### 2. Bash the command
```
mvn test -Dtest="testRunner.TestRunner"
```
OR
### 2. Run with tags
```
mvn test -Dtest="testRunner.TestRunner" "-Dcucumber.filter.tags=@NewTC"
```

### 3. Wait for it to complete

## 📊 <ins> Report </ins>
### 1. Generate allure report
### 2. Bash the command
```
allure serve allure-results
```