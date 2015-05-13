## RestTemplate logging showcase

This is a showcase how to setup logging of HTTP communication using spring RestTemplate.

RestTemplate logs everything besides bodies (which makes sense). This logging is on DEBUG level.
The showcase contains how to enable it for log4j (through commons-logging) and for slf4j 
(without commons-logging) - use maven profiles to see the difference.

Moreover, there is a simple interceptor showing how could be possible to log the body assuming it is a text-based.