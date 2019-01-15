declare -A default_port_map=(["account"]="8080" ["place"]="8081" ["zuul"]="80" ["eureka"]="8761")
echo ${!default_port_map[@]}
for key in ${!default_port_map[@]}
do
	echo "ln -s /root/java/tomcat/webapp_${default_port_map[$key]}_lib/${key}_`date +%y%m%d`.jar /root/java/tomcat/webapp_${default_port_map[$key]}/${key}.jar"
	ln -s /root/java/tomcat/webapp_${default_port_map[$key]}_lib/${key}_`date +%y%m%d`.jar /root/java/tomcat/webapp_${default_port_map[$key]}/${key}.jar
done
