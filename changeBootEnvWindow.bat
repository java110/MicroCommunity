
@setlocal enabledelayedexpansion
for /f "delims=" %%i in (service-acct\pom-boot.xml) do (

set a=%%i

echo !a!>>$)

move $ service-acct\pom.xml

for /f "delims=" %%i in (service-common\pom-boot.xml) do (

set a=%%i

echo !a!>>$)

move $ service-common\pom.xml


for /f "delims=" %%i in (service-community\pom-boot.xml) do (

set a=%%i

echo !a!>>$)

move $ service-community\pom.xml

for /f "delims=" %%i in (service-dev\pom-boot.xml) do (

set a=%%i

echo !a!>>$)

move $ service-dev\pom.xml

for /f "delims=" %%i in (service-fee\pom-boot.xml) do (

set a=%%i

echo !a!>>$)

move $ service-fee\pom.xml

for /f "delims=" %%i in (service-job\pom-boot.xml) do (

set a=%%i

echo !a!>>$)

move $ service-job\pom.xml

for /f "delims=" %%i in (service-oa\pom-boot.xml) do (

set a=%%i

echo !a!>>$)

move $ service-oa\pom.xml

for /f "delims=" %%i in (service-order\pom-boot.xml) do (

set a=%%i

echo !a!>>$)

move $ service-order\pom.xml

for /f "delims=" %%i in (service-report\pom-boot.xml) do (

set a=%%i

echo !a!>>$)

move $ service-report\pom.xml

for /f "delims=" %%i in (service-store\pom-boot.xml) do (

set a=%%i

echo !a!>>$)

move $ service-store\pom.xml

for /f "delims=" %%i in (service-user\pom-boot.xml) do (

set a=%%i

echo !a!>>$)

move $ service-user\pom.xml

for /f "delims=" %%i in (service-scm\pom-boot.xml) do (

set a=%%i

echo !a!>>$)

move $ service-scm\pom.xml
