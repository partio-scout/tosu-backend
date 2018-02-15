## Ohtupartio ##
[![Build Status](https://travis-ci.org/partio-scout/tosu-backend.svg?branch=master)](https://travis-ci.org/partio-scout/tosu-backend)

[Product Backlog etc.](https://docs.google.com/spreadsheets/d/1cA-ldx-M_ppxSicxjL06BmAjhoNi5I55M5BugoUBD98/edit?usp=drivesdk)


post event muista headeri!!!!!!!!!!!!!!!!!!!!</br>
POST</br>
http://localhost:3001/events</br>
{</br>
	"title":"weee",</br>
	"startDate":"2018-02-06",</br>
	"startTime":"11:43",</br>
	"endDate":"2018-02-09",</br>
	"endTime":"13:43",</br>
	"type":"kokous",</br>
	"information":"oooooo"</br>
}</br>
</br>
http://localhost:3001/events/1/activities</br>
{</br>
	"information":"aaaaaabbbbbbbbbbbbbbbbbaaaaaaaaaaaaaaaaaaaaaaaaaweee"</br>
}</br>
</br>
DELETE</br>
http://localhost:3001/activities/1</br>
http://localhost:3001/activities/2</br>

### Server ###

Server is Ubuntu 16.04 (xenial) at AWS, Apache/2.4.18 (Ubuntu).
