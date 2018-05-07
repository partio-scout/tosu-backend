# Server setup and configuration

Server is located at Amazon Web Services EC2 instance, address is suunnittelu.partio-ohjelma.fi and ip 35.157.216.139 . Server runs Ubuntu Server 16.04 LTS, Apache is version 2.4.18 (Ubuntu), PostgreSQL version 9.5 and Java is version 9.04. Ports 22 (ssh), 80 (http redirect to 443 https), 443 (https), 3001 (backend) and 3002 (pof-service) are opened using AWS Management Consoles security groups, group name is launch-wizard-1. Ports 443, 3001 and 3002 are encrypted with Let’s Encrypt! certificate. Keys etc. can be found from directory /etc/letsencrypt/live/suunnittelu.partio-ohjelma.fi .

Server can be accessed with user deploy, which have full sudo rights and no password, so access is allowed only with proper key. PostgreSQL can be used through default user postgres, sudo -i -u postgres. 

Backend contains two services, tosu-backend and pof. Both service files are located under path /etc/systemd/system/, pof.service and tosu-backend.service. Both can be used through service interface, ie. sudo service pof status shows status of the service and sudo service tosu-backend restart restarts service. Jar-files for services are located under path /var/tosu-apps/ . 

Both frontend and backend has been pipelined, so push to master in Github repository goes through Travis CI and new version is loaded to server and reloaded. Pof-service is under the same repository as full backend, and it must be manually restarted in case of a update. It can easily be done using script in path /var/www/ , command is bash deploy_pof.sh. In the same folder you can also found script deploy_front.sh which deploys frontend from GitHub to /var/www/html. /var/www/ also contains certificate files needed by backend for SSL.
