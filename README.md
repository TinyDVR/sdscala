sdscala
=======

A scala client for Schedules Direct's json api

# Warning
This software is extremely beta. Much of the functionality is untested. If you can help out, please check out the [issues](https://github.com/TinyDVR/sdscala/issues) page.

# [License](https://github.com/TinyDVR/sdscala/blob/master/LICENSE)

# Getting Started

## Compiling
`sdscala` uses maven for building. You can compile it with:
```
mvn install
```

## Basic Usage

Note: `sdscala` is fully async. As such, all of the api functions return a `Future` for the result. The `Implicits` singleton pimps `Future` to have a `getResult` function for blocking, which is used in this example for convenience.

```scala
import org.tinydvr.schedulesdirect.api._
import org.tinydvr.schedulesdirect.api.Implicits._

// get an access token
val token = SchedulesDirectAuthenticator.getToken(username, password).getResult.token

// create a client
val client = new SchedulesDirectAPIClient(token)

// see the headends from your location
val headEnds = client.getHeadends("USA", zipCode).getResult

// add a lineup to your account
client.addLineup("/lineups/USA-OTA-" + zipCode).getResult

// download the lineups for your account
val lineups = client.getLineups.getResult

// download the StationID / channel mapping for a lineup
val stationMap = client.getLineup(lineups.lineups.head.uri).getResult
```
