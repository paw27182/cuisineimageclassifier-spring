# cuisine-image-classifier

# 1. What you can do

* To predict which class an image belong to by using the pre-trained model.<br>
  The classification classes consist of "salad", "sushi" and "tofu".

* (CAUTION) This repository is for beginners' learning.  The precision of the model is not so good.

<br>

<img src="cuisineimageclassifier.png">

<br>

# 2. How to use

## 2.1 Activate predict-server (Flask Web app)
* Install the prerequisite python libraries

* Get a model file from the GitHub repository of 'cuisineimageclassifier-jupyter'

  STEP1: > git clone git@github.com:paw27182/cuisineimageclassifier-jupyter.git

  STEP2: Copy the model file of 'best_model_2.18.0.keras' to the following directory.

         ~/cuisineimageclassifier-spring/predictserver/appmain/model

* Environment setting
  * Edit settings.py
    * PYTHON_EXE_FILE 

<br>

* Program start
  * cd cuisineimageclassifier-spring/predictserver
  * python.exe app.py

<br>

## 2.2 Activate file-uploader (Spring Boot 3 Web app)

### 2.2.1 Setup Eclipse
  * Install Eclipse

  * Install its plug-in: Spring Tools 4 from Eclipse Marketplace

### 2.2.2 Import spring-boot project
  * Open Eclipse with the workplace of "cuisineimageclassifier-spring/eclipseworkplace".
  
  * [File] [Import] [General] [Project from Folder or Archive] [Next button]

  * [Import source] "CuisineImageClf-1" directory [Select Folder button] [Finish button]

### 2.2.3 Run Spring Boot App 

  * Focus on the project of "CuisineImageClf-1" and mouse RIGHT click

  * [Run As] [Spring Boot App]


### 2.2.4 Open browser
  * http://localhost:8080/
  * Submit an image file:<br>
   ./cuisineimageclassifier-spring/predictserver/tests/salad.jpg

<br>

# 3. System
* OS: Windows 10
* Web Framework: Spring Boot 3
* IDE: Eclipse + Spring Tools 4 (aka Spring Tool Suite 4)
* Bootstrap 5.2.3
* jQuery 3.7.1

<br>

* Web Framework: Flask
* Python 3.12.7
* Python Libraries: See the requirements.txt file


<br>

# 4. Directories and Files Overview

| Directory/File |D/F| description |
| :------------- | :-| :---------- |
| eclipseworkspace | Dir | eclipse workspace |
| eclipseworkspace/CusisineImageClf-1 | Dir | file-uploader |
| eclipseworkspace/CusisineImageClf-1/src/main/java/com/example/demo | Dir | java classes |
| eclipseworkspace/CusisineImageClf-1/src/main/resources | Dir | static and templates |
| eclipseworkspace/CusisineImageClf-1/pom.xml | File | Maven Project Object Model |
|||
| predictserver | Dir | predict-server |
| predictserver/appmain | Dir | predict program directory |
| predictserver/tests | Dir | |
| predictserver/app.py | File | start program |
| predictserver/requirements.txt | File | prerequisite libraries |
| setting.py | File ||
| README.md | File ||
