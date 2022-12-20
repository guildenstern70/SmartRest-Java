# Smart REST - Java Edition

[![MIT license](http://img.shields.io/badge/license-MIT-brightgreen.svg)](http://opensource.org/licenses/MIT)

Are you looking for SmartREST Kotlin Edition? Here it is: https://github.com/guildenstern70/SmartREST

### Build as Docker image

    gradle clean assemble
    docker build --platform linux/amd64 -t smartrest:1.0 .

### Tag image to be uploaded to a repository

    docker tag smartrest:1.0 docker.io/[your_user]/smartrest:1.0

### Run docker image

    docker run --publish 8080:8080 --name SmartRest smartrest:1.0   



