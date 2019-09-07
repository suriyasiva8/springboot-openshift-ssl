Openshift Routes Types as well as Spring Boot SSL concepts
-----------------------------------------------------------
Setting Up Openshift Locally
----------------------------
we can either use minishift 
	or 
oc cluster up command which uses container image of openshift and deploy it in docker and process it by just installing docker for windows 10 .

I prefer oc cluster up which is faster and efficient than minishift.

Reference for setup
---------------------

I used oc client version 3.9 instead of latest 3.11 bcoz 3.11 is not working for me so I used 3.9
https://medium.com/@fabiojose/working-with-oc-cluster-up-a052339ea219
https://vidhyachari.wordpress.com/2018/04/14/getting-started-with-openshift-oc-cluster-up/



Spring Boot SSL
-------------------

SSL/TLS
------------

The concept is TLS is upgraded version of ssl.
It makes our http to https and secure it.
so if user interfere between the communication of client and server with https enabled then he will ge nothing more than a garbage of values.
Https exactly what happening in background pls refer from youtube channel you added it to your playlists.

FOR Https
-------------
For Coding, our application have to provide a ssl certificate we can obtain it from the Certificate Authority for paid by providing 
our organisation name, purpose and all to CA it will validate that info and provide a certifcate with public key which is encrypted by a private key and the
Certification Authority holds this private key and it will be updated in servers.

So now when the client hit the server running our application , the application has to send this certificate and browser verifies the certificate with private key if that satisfies
it will allows us inside else it will says error message like invalid cerficate/certificate expired and all.

For Developing Purpose 
-------------------
We cannot buy certificate for paid form certification authority. So we can generate our own self signed certificate by using java keytool using following command,
and the certificate can be of type jks and pkcs12 but pkcs12 is most preferred industry standard.

To generate JKS type certifcate
--------------------------------
keytool -genkeypair -alias tomcat -keyalg RSA -keysize 2048 -keystore keystore.jks -validity 3650

To generate PKCS12 type certificate--------------
----------------------------------
keytool -genkeypair -alias tomcat -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore keystore.p12 -validity 3650

For reference:
https://www.thomasvitale.com/https-spring-boot-ssl-certificate/


And we will configure ssl in our spring boot application by just adding some params in app.properties. Thats it.

Now we have backed that supplies a certificate.

To list values in keystore/certificate use following command
keytool -list -v -keystore localhost.pkcs12


Openshift Routes
----------------

Routes to enable Https ,
It provides 3 types of Tls

Before going to topic lets understand how our app will be in openshift,



 Client ---------------------->Router------------------>Container containing our app.


1) Edge.


 In this TLS type, the Router will give the certificate when the client request the url and the destined application will be insecure. Probable used when our application is without 
 ssl configuration and we want the ssl enabled externally without changing app.
 
			HTTPS/CertificateValidation
 client ---------------------->Router--------------------->Container containing our app
 
 
2) Passthrough,

In this type  our app will provide the certificate and the client request directly hits the container.

													Https				<-responds with ssl certificate
client -------------------------------->Router------------->container containing our app.


3) Rencrypt

It is combination of Edge and passthrough. Client request will be intrepreted by router with a internal certificate or we can also provide our own certificate and then
router hits the destination which has different certificate.

Here we have to pass destinationCACertificate value compulsory if our certificate is not provided by CA.
by this destinationCACertificate param we telling router to accept the certificate our app returns.

destinationCACertificate value will be in pem format,
This contains a private key needed to accept our actual keystore present in application. so we will generate a pem file for keystore present in application using following command.

keytool -exportcert -alias localhost -keystore localhost.pkcs12 -rfc -file server1.pem

If we just hitting from browse locally for testing our app we can import the certificate required to accept the ssl provided by app which can be generate using following command,

keytool -export -keystore localhost.pkcs12 -alias localhost -file myCertificate.crt


References
---------
https://docs.openshift.com/container-platform/3.7/architecture/networking/routes.html
https://stackoverflow.com/questions/24343681/how-to-convert-trust-certificate-from-jks-to-pem
https://gist.github.com/rafaeltuelho/111850b0db31106a4d12a186e1fbc53e



 
 
 
 


