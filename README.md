## Ашигласан технологи
- Java 1.8
- Mysql 8.0
- Wildfly 16.0.0 - https://download.jboss.org/wildfly/16.0.0.Final/wildfly-16.0.0.Final.zip

## Database болон table үүсгэх /query/
1. Database үүсгэх  
```
create database `accuload`;
```
2. user table үүсгэх
```

CREATE TABLE `user` (
  `userName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `adminStatus` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

```

3. Admin user нэмэх
```
insert into user (username, password, adminStatus)
values ('admin','acculoadadmin',1);
```

4. locationconfig table үүсгэх
```
CREATE TABLE `locationconfig` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `locationId` int(11) DEFAULT NULL,
  `locationIp` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

```

5. location config  нэмэх
```
insert into locationconfig (locationId, locationIp)
values (3,'http://192.168.**.***');
```

6. arm table үүсгэх
```
CREATE TABLE `arm` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `armId` int(11) DEFAULT NULL,
  `armName` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `armNo` varchar(10) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `ip` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

```

7. deliveryOrder table үүсгэх
```
CREATE TABLE `deliveryOrder` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `vehicleNo` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `driverName` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `deliveryOrderDate` datetime DEFAULT NULL,
  `trailerNo` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `compartmentSequence` int(11) DEFAULT NULL,
  `capacity` int(11) DEFAULT NULL,
  `productId` int(11) DEFAULT NULL,
  `shippedDate` datetime DEFAULT NULL,
  `tankId` int(11) DEFAULT NULL,
  `armId` int(11) DEFAULT NULL,
  `temprature` int(11) DEFAULT NULL,
  `density` float DEFAULT NULL,
  `armStartMetr` float DEFAULT NULL,
  `armEndMetr` float DEFAULT NULL,
  `deliveryOrderId` int(11) DEFAULT NULL,
  `shippedAmount` float DEFAULT NULL,
  `sentStatus` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
```

8. product table үүсгэх
```
CREATE TABLE `product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `productId` int(11) DEFAULT NULL,
  `productName` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

```

9. tank table үүсгэх
```
CREATE TABLE `tank` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tankId` int(11) DEFAULT NULL,
  `tankName` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `productId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

```

10. tankarmmap table үүсгэх
```
CREATE TABLE `tankarmmap` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tankId` int(11) DEFAULT NULL,
  `armId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

```
11. Delivery order table - d ачилт эхэлсэн эсэхийг тэмдэглэх багана нэмэх. 
```
ALTER TABLE `deliveryOrder`
ADD column `loadingStatus` int(11) default 0;
```
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
* bin/add-user.bat file г ажиллуулж зааврын дагуу application server удирдах user үүсгэнэ.  https://www.ibm.com/support/knowledgecenter/en/SSMKFH/com.ibm.apmaas.doc/install/jboss_config_agent_prereq_add_management_user.htm
* bin/standalone.bat file г ажиллуулснаар application server асна.
* http://localhost:9990 орж  deployment хэсэг рүү орж build хийсэн .war file - аа upload болон deploy хийнэ.
* Шинээр war file гаргах бол Татаж авсан code - оо задалж IDE(eclipse ,netbeans , intellij) руу import хийж maven build хийж pom.xml дэх library нуудыг татаж авна. Үүний дараа  project - оо  build хийж war file generate хийнэ.


## Code бүтэц
* controller package дотор view удирдах file - ууд багтана
* ApplicationController - singleton хувьсагчид буюу application 1 удаа зарлагдаж бусад хэсэгт ашиглагдах хувьсагчдыг удирдана.
* AdminController - admin цонхны хэсгийг удирдана
* HomeController -  үндсэн цонхны хэсгийг удирдана
* UserController - user цонхны хэсгийг удирдана,
- db package дотор Database тэй холбогдох file - ууд багтана
* hibernate.cfg.xml file - н
```
<property name="hibernate.connection.url">jdbc:mysql://127.0.0.1:3306/accuload?allowPublicKeyRetrieval=true&amp;useSSL=false</property>
<property name="hibernate.connection.username">root</property>
<property name="hibernate.connection.password">root</property>
```
энэ хэсэгт database, username, password - г тохируулна
- filter package дотор user auth хэсгийг удирдах file багтана
- model package дотор Database Table - г java class болгосон хэсэг багтана
* Arm.java нь accuload - той холбогдох хэсэг багтсан болно.
