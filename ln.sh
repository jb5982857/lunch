declare -A default_port_map=(["account"]="8080",["place"]="8081",["zuul"]="80",["eureka"]="8761")
for key in ${!default_port_array[@]}
do
    ln -s /root/java/tomcat/webapp_${default_port_array[$key]}_lib/$key_`date +%y%m%d`.jar /root/java/tomcat/webapp_${default_port_array[$key]}/$key.jar
    echo "ln -s /root/java/tomcat/webapp_${default_port_array[$key]}_lib/$key_`date +%y%m%d`.jar /root/java/tomcat/webapp_${default_port_array[$key]}/$key.jar"
done