#!/bin/bash 


declare -A default_port_map=(["account"]="8080" ["place"]="8081" ["zuul"]="80" ["eureka"]="8761")
echo ${!default_port_map[@]}_$#

declare -A choice_map=()

if [ $# = 0 ]; then

	for key in ${!default_port_map[@]}
	do
		choice_map[$key]=${default_port_map[$key]}		
	done

else
	for i in $@
	do
        	echo "i:"$i
        	choice_map[$i]=${default_port_map[$i]}
	done
fi #ifend

echo ${!choice_map[@]}_${choice_map[@]}

#$1 文件路径 
function newestFile() 
{     
        echo $(ls $1 | sort -r)
}   

for key in ${!choice_map[@]}
do

	file="/root/java/tomcat/webapp_${choice_map[$key]}/${key}.jar"
	if [[ -f "$file" ]]; then
		echo "file $file  exist ,del now!"
		rm -rf /root/java/tomcat/webapp_${choice_map[$key]}/${key}.jar	
	fi #ifend

	files=($(newestFile "/root/java/tomcat/webapp_${choice_map[$key]}_lib/"))
	echo "files "$files

	if [ $files ];then
		echo "ln -s /root/java/tomcat/webapp_${choice_map[$key]}_lib/${files[0]} /root/java/tomcat/webapp_${choice_map[$key]}/${key}.jar"

        ln -s /root/java/tomcat/webapp_${choice_map[$key]}_lib/${files[0]} /root/java/tomcat/webapp_${choice_map[$key]}/${key}.jar
	else 
		echo "it does not find any file in /root/java/tomcat/webapp_${choice_map[$key]}_lib/"
	fi	

done
