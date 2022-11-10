
@setlocal enabledelayedexpansion
for /f "delims=" %%i in (service-acct\pom-cloud.xml) do (

set a=%%i

echo !a!>>$)

move $ service-acct\pom.xml

for /f "delims=" %%i in (service-common\pom-cloud.xml) do (

set a=%%i

echo !a!>>$)

move $ service-common\pom.xml


for /f "delims=" %%i in (service-community\pom-cloud.xml) do (

set a=%%i

echo !a!>>$)

move $ service-community\pom.xml

for /f "delims=" %%i in (service-dev\pom-cloud.xml) do (

set a=%%i

echo !a!>>$)

move $ service-dev\pom.xml

for /f "delims=" %%i in (service-fee\pom-cloud.xml) do (

set a=%%i

echo !a!>>$)

move $ service-fee\pom.xml

for /f "delims=" %%i in (service-job\pom-cloud.xml) do (

set a=%%i

echo !a!>>$)

move $ service-job\pom.xml

for /f "delims=" %%i in (service-oa\pom-cloud.xml) do (

set a=%%i

echo !a!>>$)

move $ service-oa\pom.xml

for /f "delims=" %%i in (service-order\pom-cloud.xml) do (

set a=%%i

echo !a!>>$)

move $ service-order\pom.xml

for /f "delims=" %%i in (service-report\pom-cloud.xml) do (

set a=%%i

echo !a!>>$)

move $ service-report\pom.xml

for /f "delims=" %%i in (service-store\pom-cloud.xml) do (

set a=%%i

echo !a!>>$)

move $ service-store\pom.xml

for /f "delims=" %%i in (service-user\pom-cloud.xml) do (

set a=%%i

echo !a!>>$)

move $ service-user\pom.xml

for /f "delims=" %%i in (service-scm\pom-cloud.xml) do (

set a=%%i

echo !a!>>$)

move $ service-scm\pom.xml
