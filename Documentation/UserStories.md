# Scout
## Register Or LoginScout
POST: "/scout" 

params: @RequestBody ObjectNode Authorization, HttpSession session

First try to verify googleId token. Then either create new scout (if scout logs in first time) or confirm login for alredy existing scout. 
When new scout is created, create also new activitybuffer for this scout.
## Logout
POST: "/logout" 

params: HttpSession session

Invalidate session.

## Delete Scout
DELETE: "/scouts"

params: HttpSession session

Here scout can remove his account. Allso invalidate particular session.

# ActivityBuffer
## Post Activity
POST: "/activitybuffer/{bufferId}/activities/" 

params: @PathVariable Long bufferId, @RequestBody Activity activity, HttpSession session

Firt verify that session's scout is owner to the activitybuffer. Then add activity to activitybuffer.
Validate and return ResponseEntity.ok(activity) or list of errors (ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors)).
## Get Buffer Content
GET: "/activitybuffer/{id}" 

params: @PathVariable Long id, HttpSession session

Firt verify that session's scout is owner to the activitybuffer. Then return all activities from this activitybuffer.

# Event
## Get Events
DELETE: "/events"

params: HttpSession session

Verify that session has scout, what means someone is logged in. Then Return this scout's events.
 
## Post Event
POST: "/events"

params: @RequestBody Event event, HttpSession session

Verify that session has scout again. Then set that session's scout is this even's scout, and then save event.

## Edit Event
PUT: "/events/{eventId}"

params: @PathVariable Long eventId, @RequestBody Event event, HttpSession session

Verify that session's scout is same as the scout of the event to be edited. Then edit event.

## Delete Event
DELETE: "/events/{eventId}"

params: @PathVariable Long eventId, HttpSession session

Verify that session's scout is same as the scout of the event to be edited. Then delete event.
When event is deleted, all its activities will be moved to scout's activitybuffer.


# EventGroup

...

# Activity

...

# Plan

...
