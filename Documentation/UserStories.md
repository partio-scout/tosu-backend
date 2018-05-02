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

## Post EventGroup
DELETE: "/eventgroup"

params: HttpSession session

Verify that session has scout. Then create eventgroup for this scout. Eventgroup is needed when scout wants create repeating event.

## Delete
DELETE: "/eventgroup/{groupId}"

params: @PathVariable Long groupId, HttpSession session

Verify that session's scout is same as the scout of the events in the eventgroup. Then delete eventgroup.
When eventgroup is deleted, it means that all its repeating events will be removed and all activities from those events will be move to the activitybuffer of the scout.


# Activity

## Delete Activity
DELETE: "/activities/{activityId}"

params: @PathVariable Long activityId, HttpSession session
 
Verify that session's scout is same as the scout of the avtivity's event or bufferzone. Then remove activity, and it's plans.
 
 ## Post Activity
POST: "/events/{eventId}/activities"

params: @PathVariable Long eventId, @RequestBody Activity jsonActivity, HttpSession session

Verify that session has scout. Then add activity to an event that matches the eventId.

## Get Activity
GET: "/activities"

params: HttpSession session

Verify that session has scout. Then return activities from this scout's events.

## Move Activity From Event To Buffer
PUT: "/activity/{activityId}/fromevent/{eventId}/tobuffer/{bufferId}"

params: @PathVariable Long activityId, @PathVariable Long eventId, @PathVariable Long bufferId, HttpSession session

First vefiry that session's scout is owner for activity, event and activitybuffer. Then move activity from event to buffer.

## moveActivityFromBufferToEvent
PUT: "/activity/{activityId}/frombuffer/{bufferId}/toevent/{eventId}"

params: @PathVariable Long activityId, @PathVariable Long bufferId, @PathVariable Long eventId, HttpSession session

First vefiry that session's scout is owner for activity, event and activitybuffer. Then move activity from activitybuffer to given event.

## Move Activity From Event To Other Event
PUT: "/activity/{activityId}/fromevent/{fromId}/toevent/{toId}"

params: HttpSession session

First vefiry that session's scout is owner for activity and event. Then change activity's event to other event.
# Plan

## Add Plan For Activity
POST: "/activity/{activityId}/plans"

params: @PathVariable Long activityId, @RequestBody Plan jsonPlan, HttpSession session

First vefiry that session's scout is owner of the activity. Then add plan to activity.

## Modify Plan
PUT: "/plans/{planId}"

params: @PathVariable Long planId, @RequestBody Plan jsonPlan, HttpSession session

First vefiry that session's scout is owner of the plan. Then edit plan.

## Delete Plan
DELETE: "/plans/{planId}"

params: @PathVariable Long planId, HttpSession session

First vefiry that session's scout is owner of the plan. Then delete plan.
