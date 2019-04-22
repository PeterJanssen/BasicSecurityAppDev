# BasicSecurityAppDev
Hybrid crypto system Basic Security Hogeschool PXL 2018-2019

## JavaFX and IntelliJ IDEA

Before we can use JavaFX in IntelliJ IDEA we need to do some simple preparations.

1) To start off we need JavaFX SDK 12 which can be downloaded here https://gluonhq.com/products/javafx/
![alt text][img_JavaFXSDKDownload]
Place this SDK in a location eg. C:\Program Files\Java
2) After this we go to File -> Project Structure -> Project and set the project SDK to 12. We also set the language level to 12.
![alt text][img_SetProjectSDK]
3) Then we add a global variable that can be used in future projects. Go to File -> Settings -> Appearance & Behavior -> Path Variables and define the name of the variable as PATH_TO_FX and browse to the lib folder of the JAVAFX SDK to set its value and click apply.
![alt text][img_GlobalVariable]
4) Then we go to Run -> Edit Configurations... and add these VM options: 
--module-path ${PATH_TO_FX} --add-modules javafx.controls,javafx.fxml
![alt text][img_VMOptions]
5) Now the project should run fine by clicking on Run.

If there are any further problems consult https://openjfx.io/openjfx-docs/#maven

[img_JavaFXSDKDownload]:Images/Java%20SDK%20download.PNG "JavaFX SDK Download"
[img_SetProjectSDK]:Images/SetProjectSDK.PNG "Project SDK"
[img_GlobalVariable]:Images/GlobalVariable.PNG "Global variable"
[img_VMOptions]:Images/VMOptions.PNG "VMOptions"