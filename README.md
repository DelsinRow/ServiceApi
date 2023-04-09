**How to launch JAR-file**

Need to generate new TOKEN with GitHub-account \
Get TOKEN here: https://www.toptal.com/developers/hastebin/documentation \


===>Launch the output in HASTEBIN service<===

To launch in Terminal enter the command: TOKEN='value' java -jar ServiceAPI-1.0-SNAPSHOT-jar-with-dependencies.jar 

To run in Docker: docker run -e TOKEN=my_value docker_image, where 'my_value' - is value of token

===>Launch the output in STORAGE service<===\
Don't enter TOKEN to launch default storage service

To launch in Terminal enter the command: java -jar ServiceAPI-1.0-SNAPSHOT-jar-with-dependencies.jar

To run in Docker: docker run -e docker_image


