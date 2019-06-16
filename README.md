## Ашигласан технологи
- Java 1.8
- Mysql 8.0
- Wildfly 16.0.0 - https://download.jboss.org/wildfly/16.0.0.Final/wildfly-16.0.0.Final.zip

## Суулгах заавар

* Wildfly server - ээ асаахын өмнө  standalone.xml ээс
```
<interface name="management">
	<inet-address value="${jboss.bind.address.management:127.0.0.1}"/>
</interface>
<interface name="public">
	<inet-address value="${jboss.bind.address:0.0.0.0}"/>
</interface>
```
хэсгийг
```
<interface name="management">
	<any-address/>
</interface>
<interface name="public">
	<any-address/>
</interface>
```
ийм болгож өөрчилнө. Энэ нь тухайн server лүү ip аар хандах эрхийг зөвшөөрч байгаа хэсэг юм.
* bin/add-user.sh file г ажиллуулж зааврын дагуу application server удирдах user үүсгэнэ.
* bin/standalone.sh file г ажиллуулснаар application server асна.
* http://###.###.###.###:9990 хандаж deployment хэсэг рүү орж build хийсэн .war file - аа upload болон deploy хийнэ.
* Татаж авсан code - оо задалж IDE(eclipse ,netbeans , intellij) руу import хийж maven build хийж pom.xml дэх
* Шинээр war file гаргах бол Татаж авсан code - оо задалж IDE(eclipse ,netbeans , intellij) руу import хийж maven build хийж pom.xml дэх library нуудыг татаж авсны дараа build хийнэ. Үүний дараа war file generate хийнэ.




## Code бүтэц


* controller package дотор view удирдах file - ууд багтана
* ApplicationController - singleton хувьсагчид буюу application 1 удаа зарлагдаж бусад хэсэгт ашиглагдах хувьсагчдыг удирдана.
* AdminController - admin цонхны хэсгийг удирдана
* HomeController -  үндсэн цонхны хэсгийг удирдана
* UserController - user цонхны хэсгийг удирдана,
- db package дотор Database тэй холбогдох file - ууд багтана
* hibernate.cfg.xml file - н
```
<property name="hibernate.connection.url">jdbc:mysql://127.0.0.1:3306/dummy?allowPublicKeyRetrieval=true</property>
<property name="hibernate.connection.username">root</property>
<property name="hibernate.connection.password">root</property>
```
энэ хэсэгт database, username, password - г тохируулна
- filter package дотор user auth хэсгийг удирдах file багтана
- model package дотор Database Table - г java class болгосон хэсэг багтана
* Arm.java нь accuload - той холбогдох хэсэг багтсан болно.
