Hello World Using GCM in Android
==================================
This is a simple tutorial created for help during implementation of Push notifications in Android Development.

Requirements
-------------

First, you need register your account in Google API, and get:
* Project Number: put it in [MainActivity](https://github.com/brunogabriel/helloWorldGCMAndroid/blob/master/HelloGCM/src/br/example/hellogcm/MainActivity.java) (SERVER_SENDER);
* API KEY.

Second, put the API KEY in php mock server [MOCK SERVER](https://github.com/brunogabriel/helloWorldGCMAndroid/blob/master/mock_server.php) in API_ACCESS_KEY.

Third: you need get API_ACCESS_KEY from user mobile and register in [MOCK SERVER](https://github.com/brunogabriel/helloWorldGCMAndroid/blob/master/mock_server.php). You can get in Android resource, because during the execution of app, use LOG_TAG in console.

Please, see with attention the manifest.xml file, it's very important.


Notes
------

* If you run the php server, it'll send messages for all mobile that have registers in API_ACCESS_KEY.

* References: Google Cloud Messaging, Stack Overflow (:P) and Android Cookbook.





