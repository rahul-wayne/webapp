FROM ubuntu:latest

RUN apt-get update && \
    apt-get install -y \
    apache2

RUN echo "ServerName 127.0.0.1" >> /etc/apache2/apache2.conf

RUN mkdir -p /var/www/styles
COPY style.css /var/www/styles/style.css
COPY index.html /var/www/html

EXPOSE 80

ADD start.sh /

# Make the script executable
RUN chmod +x /start.sh

# Start Apache using the script
CMD ["/start.sh"]
