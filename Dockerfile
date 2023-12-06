FROM centos:7
RUN yum -y update
RUN yum -y install \
    httpd
RUN echo "ServerName 127.0.0.1" >> /etc/httpd/conf/httpd.conf
RUN mkdir -p /var/www/styles
COPY style.css /var/www/styles/style.css
COPY index.html /var/www/html
EXPOSE 80
ADD start.sh /
RUN chmod +x /start.sh
CMD [ "/start.sh" ]