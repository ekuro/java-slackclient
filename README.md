## Java slack command client

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

