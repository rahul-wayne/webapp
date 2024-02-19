#!/bin/bash

# Remove Apache PID file
rm -rf /var/run/apache2/apache2.pid

# Start Apache in the foreground
apache2ctl -D FOREGROUND


