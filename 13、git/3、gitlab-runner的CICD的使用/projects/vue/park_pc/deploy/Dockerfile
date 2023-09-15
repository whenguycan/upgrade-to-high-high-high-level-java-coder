FROM nginx
#将dist目录内容复制到nginx容器html内部，因为vue项目rpm run build之后生成的项目文件都在dist中，所以只要把dist复制到nginx的目录中就行了
COPY dist /usr/share/nginx/html/
COPY park.conf /etc/nginx/conf.d/
RUN rm -f /etc/nginx/conf.d/default.conf

EXPOSE 80
