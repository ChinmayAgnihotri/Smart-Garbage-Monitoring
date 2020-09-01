
#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
 
// Set these to run example. 
#define FIREBASE_HOST "smart-parking-48505.firebaseio.com" 
#define FIREBASE_AUTH "lVB4y782Tj6gLzLA6okdrJVUUMpGTdmbP6i3fiDa" 
#define WIFI_SSID "faltu" 
#define WIFI_PASSWORD "12345678333" 

const int trigPin = D5;
const int echoPin = D6;
long duration;
int distance;
void setup() { 
  pinMode(trigPin, OUTPUT); // Sets the trigPin as an Output
  pinMode(echoPin, INPUT); // Sets the echoPin as an Input
  Serial.begin(9600); 

  // connect to wifi. 
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD); 
  Serial.print("connecting"); 
  while (WiFi.status() != WL_CONNECTED) { 
    Serial.print("."); 
    delay(500); 
  } 
  Serial.println(); 
  Serial.print("connected: "); 
  Serial.println(WiFi.localIP()); 
   
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
} 
 

void loop() { 
  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);

  // Sets the trigPin on HIGH state for 10 micro seconds
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);

  // Reads the echoPin, returns the sound wave travel time in microseconds
  duration = pulseIn(echoPin, HIGH);

  // Calculating the distance
  distance = duration * 0.034 / 2; //Sound travels at 340 metres per sec

  // Prints the distance on the Serial Monitor
  Serial.print("Distance in cm: ");
  Serial.println(distance);
  Firebase.setFloat("number", distance);
  
  // handle error 
  if (Firebase.failed()) { 
      Serial.print("setting /number failed:"); 
      Serial.println(Firebase.error());   
      return; 
  } 
  delay(5000); //Every 5 seconds
  //delay(1000); 
  
}
