artifact_id = client # set your jar name here

env:
    @sh env.sh

build:
    @mvn package

run:
    @java -jar target/${artifact_id}.jar

full:
    make build run

