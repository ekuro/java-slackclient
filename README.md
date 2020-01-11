## Java slack command client
This is Java slack client sending command to slack app by using legacy token.  
Slack don't recommend this option.

### Quick Start
#### Compile
```
$ javac -sourcepath src -d out src/jp/ekuro/slack/Main.java
```
#### Generate jar
```
$ jar cvfm slack.jar src/META-INF/MANIFEST.MF -C out .
```
#### Execution
```
$ java -jar slack.jar [LEGACY_SLACK_API_TOKEN] [CHANNEL_ID] [COMMAND] [TEXT]
```

*Slack don't recommend to use this token.*  
Legacy token is [here](https://api.slack.com/custom-integrations/legacy-tokens).  

